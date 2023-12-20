package app.engine;

import app.Main;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialsMapper;
import app.persistence.VariantsMapper;
import app.svg.SVG;
import app.svg.Direction;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Engine {
  public static int frontPillarDistance = 100;
  public static int backPillarDistance = 30;
  public static int sidePillarDistance = 35;
  public static int heightOfCarport = 330;
  public static MaterialDTO pillarsM;
  public static List<MaterialVariantDTO> pillarsMV;


  /**
   * draws a carport
   * @param items parts list
   * @param connectionPool Connectionpool for pulling data from database
   * @param beamID which materialID is requested for the beams and crossbeams
   * @param pillarID which MaterialID is requested for the pillars
   * @param width width of the carport
   * @param height height of the carport
   * @return the HTML for the SVG drawing.
   */
  public static SVG drawCarportDraft1(ArrayList<MaterialDTO> items, ConnectionPool connectionPool, float width, float height, int pillarID, int beamID) throws DatabaseException {
    //// Retrieval of information we need to build the carport.
    //// Getting Beams information.
    List<MaterialDTO> pillars = getPillars(connectionPool, pillarID);
    List<MaterialDTO> beams = getBeams(connectionPool, beamID);
    List<Float> beamLengths = beams.stream().map(b -> Float.valueOf(b.getLength())).toList();
    float maxBeamLength = beamLengths.stream().max(Float::compareTo).orElse(0F);
    if(maxBeamLength == 0F) {
      throw new DatabaseException("No beams with materialID = '" + beamID + "'");
    }
    float beamLimit = maxBeamLength / 2;
    float beamWidth = beams.stream().limit(1).map(b -> b.getWidthMm() / 10F).reduce((aFloat, aFloat2) -> aFloat).orElse(0F);


    //// should get some info on the pillars beforehand here.
    Optional<MaterialDTO> firstPillar = pillars.stream().findFirst();
    float pillarWidth;
    if(firstPillar.isPresent()){
      pillarWidth = firstPillar.get().getWidthMm()/10f;
    } else {
      pillarWidth = 10f;
      System.out.println("No pillar with materialID = '" + pillarID + "'");
      throw new DatabaseException("No pillar with materialID = '" + pillarID + "'");
    }

    int horizontalPillars = (int) Math.max(2,
        Math.ceil((width - frontPillarDistance - backPillarDistance)/Math.min(310, beamLimit)) + 1);
    int verticalPillars = (int) Math.max(2,
        Math.ceil((height - (2*sidePillarDistance))/beamLimit));


    float horizontalPillarDistance = (width - frontPillarDistance - backPillarDistance) / (horizontalPillars-1);
    float verticalPillarDistance = (height - (2*sidePillarDistance)) / (verticalPillars-1);




    SVG svg = new SVG();
    //// Frame
    svg.drawRectWithMeasurement(0, width, 0, height, 40F, 50F);

    //// Pillars
    svg.drawMeasurement(Direction.up, 0, frontPillarDistance, 10);
    svg.drawMeasurement(Direction.left, 0, sidePillarDistance, 10);
    for(float horizontal = frontPillarDistance; horizontal < width ; horizontal += horizontalPillarDistance){
      svg.drawMeasurement(Direction.up, horizontal, Math.min(width - horizontal, horizontalPillarDistance), 10);
      for(float vertical = sidePillarDistance; vertical < height; vertical += verticalPillarDistance){
        svg.drawRectCenter(horizontal,vertical, pillarWidth);
        svg.drawMeasurement(Direction.left, vertical, Math.min(height - vertical, verticalPillarDistance), 10);
        addItem(items, getBestMaterialLengthOver(pillars, heightOfCarport),1);
      }
    }

    //// Beams
    for(float vertical = sidePillarDistance; vertical < height; vertical += verticalPillarDistance){
      float currentBeam = frontPillarDistance;
      for(float horizontal = 0; horizontal < width ;){
        while(anyBigger(beams, currentBeam + horizontalPillarDistance)){
          currentBeam += horizontalPillarDistance;
        }
        if(width - horizontal > 2*horizontalPillarDistance && width - horizontal <= 3*horizontalPillarDistance){
          currentBeam = horizontalPillarDistance;
        }
        if(currentBeam > width - horizontal){
          currentBeam = width - horizontal;
        }
        svg.drawRect(horizontal, horizontal + currentBeam, vertical - beamWidth / 2, vertical + beamWidth / 2);
        addItem(items, getBestMaterialLengthOver(beams, currentBeam),1);
        horizontal += currentBeam;
        currentBeam = 0;
      }
    }

    //// Crossbeams
    for(float horizontal = frontPillarDistance; horizontal + beamWidth <= width; horizontal += horizontalPillarDistance){
      boolean odd = false;
      for(float vertical = sidePillarDistance; vertical + sidePillarDistance < height; vertical += verticalPillarDistance){
        float currentBeam = getBestMaterialLengthOver(beams, verticalPillarDistance + (2*sidePillarDistance)).getLength();
        svg.drawRect(horizontal - beamWidth + (odd ? beamWidth : 0),
            horizontal + (odd ? beamWidth : 0),
            vertical - sidePillarDistance,
            vertical + verticalPillarDistance + sidePillarDistance
        );

        addItem(items, getBestMaterialLengthOver(beams, currentBeam),1);
        odd = !odd;
      }
    }


    return svg;
  }

  private static boolean anyBigger(List<MaterialDTO> materials, float size){
    for (MaterialDTO m: materials) {
      if(m.getLength() >= size) return true;
    }
    return false;
  }

  private static List<MaterialDTO> getPillars(ConnectionPool connectionPool, int pillarID) throws DatabaseException {
    return MaterialsMapper.getMaterialInfoByType(connectionPool, Mtype.pillar).stream().filter(p -> p.getMaterialId() == pillarID).toList();
  }
  private static List<MaterialDTO> getBeams(ConnectionPool connectionPool, int beamID) throws DatabaseException {
    return MaterialsMapper.getMaterialInfoByType(connectionPool, Mtype.beam).stream().filter(p -> p.getMaterialId() == beamID).toList();
  }

  private static ArrayList<Float> getBeamLengths(int materialID){
    ////ToDo replace with mapper
    Stream.of(300F,360F,420F,480F,540F,600F).map(m -> {
      return new BeamDTO(materialID, "", Mtype.beam, 10, 10, -1, m.intValue(), 1);
    });
    return new ArrayList<Float>(Stream.of(300F,360F,420F,480F,540F,600F).toList());
  }

  private static float getBestLengthOver(ArrayList<Float> arr, float length){
    return arr.stream().dropWhile(l -> l < length).sorted().limit(1).toList().get(0);
  }
  private static MaterialDTO getBestMaterialLengthOver(List<MaterialDTO> arr, float length) throws DatabaseException {
    Optional<MaterialDTO> result = arr.stream().dropWhile(l -> l.getLength() < length).sorted().limit(1).reduce((m, m2) -> m);
    if(result.isEmpty()){
      result = arr.stream().sorted().reduce((m, m2)-> m);
    }
    if(result.isEmpty()){
      throw new DatabaseException("There's no beams!");
    }
    return result.get();
  }

  private static float getBestLengthUnder(ArrayList<Float> arr, float length){
    List<Float> list = arr.stream().dropWhile(l -> l > length).sorted().toList();
    return list.get(list.size()-1);
  }

  private static void addItem(List<MaterialDTO> arr, MaterialDTO newMat, int amount){
    if(arr.stream().anyMatch(g ->
            g.getMaterialId() == newMat.getMaterialId() && g.getMaterialVariantID() == newMat.getMaterialVariantID()
    )){
      arr.stream()
              .filter(g -> g.getMaterialId() == newMat.getMaterialId() && g.getMaterialVariantID() == newMat.getMaterialVariantID())
              .forEach(g -> g.setAmount(g.getAmount() + amount));
    } else {
      newMat.setAmount(amount);
      arr.add(newMat);
    }
  }

  //// for tests
  public static void main(String[] args){
    ArrayList<Float> testArr = getBeamLengths(1);
    System.out.println(getBestLengthOver(testArr, 450F));
    System.out.println(testArr);

    ArrayList<MaterialDTO> partsList = new ArrayList<>();
    String output;
    try {
      Optional<MaterialDTO> optPillar = MaterialsMapper.getMaterialInfoByType(Main.getConnectionPool(), Mtype.pillar).stream().sorted(Comparator.comparingInt(MaterialDTO::getMaterialId)).findFirst();
      int pillarID = 0;
      if(optPillar.isPresent()) {
        pillarID = optPillar.get().getMaterialId();
      } else {
        System.out.println("No pillars in Database");
        throw new DatabaseException("No pillars in Database");
      }
      Optional<MaterialDTO> optBeam = MaterialsMapper.getMaterialInfoByType(Main.getConnectionPool(), Mtype.beam).stream().sorted(Comparator.comparingInt(MaterialDTO::getMaterialId)).findFirst();
      int BeamID = 0;
      if(optBeam.isPresent()) {
        BeamID = optBeam.get().getMaterialId();
      } else {
        System.out.println("No pillars in Database");
        throw new DatabaseException("No pillars in Database");
      }
      SVG svg = drawCarportDraft1(partsList, Main.connectionPool, 780,600, pillarID, BeamID);
      File appdata = File.createTempFile("carport", ".txt");
      OutputStream os = new FileOutputStream(appdata);
      ObjectOutputStream oos = new ObjectOutputStream(os);
      oos.writeObject(svg);
      InputStream is = new FileInputStream(appdata);
      System.out.println("object length: " + is.readAllBytes().length + " - svg length: " + svg.toString().length());

      output = svg.toString();
    } catch (Exception e) {
      output = "Error at draw Carport: " + e.getMessage();
    }
    System.out.println(output);
    System.out.println();
    System.out.println("   -----");
    System.out.println("items in parts list:");
    partsList.forEach(s -> System.out.println(s.getAmount() + "x:" + s.getLength() + "cm - " + s.getName()));
  }
}
