package app.engine;

import app.svg.SVG;
import app.svg.Direction;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Engine {
  public static int frontPillarDistance = 100;
  public static int backPillarDistance = 30;
  public static int sidePillarDistance = 35;


  /**
   * draws a carport
   * @param items styklist
   * @param width width of the carport
   * @param height height of the carport
   * @return the HTML for the SVG drawing.
   */
  public static SVG drawCarportDraft1(ArrayList<Object> items, float width, float height) throws Exception {
    //// Retrieval of information we need to build the carport.
    //// Getting Beams information.
    ArrayList<Float> beamLengths = getBeamLengths("");
    float beamLimit = beamLengths.stream().max(Float::compareTo).orElse(0F);
    if(beamLimit == 0F) throw new Exception("An error has occurred, there's no beams.");


    //// should get some info on the pillars beforehand here.
    float pillarWidth = 27;
    int horizontalPillars = (int) Math.max(2,
        Math.ceil((width - frontPillarDistance - backPillarDistance)/Math.min(310, beamLimit)) + 1);
    int verticalPillars = (int) Math.max(2,
        Math.ceil((height - (2*sidePillarDistance))/beamLimit));

    /*
    int horizontalPilarDistance = (int) getBestLengthOver(beamLengths, (width - frontPillarDistance - backPillarDistance) / (horizontalPillars-1));
    int verticalPilarDistance = (int) getBestLengthOver(beamLengths, (height - (2*sidePillarDistance)) / (verticalPillars-1));*/

    float horizontalPilarDistance = (width - frontPillarDistance - backPillarDistance) / (horizontalPillars-1);
    float verticalPilarDistance = (height - (2*sidePillarDistance)) / (verticalPillars-1);




    SVG svg = new SVG();
    //// Frame
    svg.drawRectWithMeasurement(0, width, 0, height, 40F, 40F);

    //// Pillars
    svg.drawMeasurement(Direction.up, 0, frontPillarDistance, 10);
    svg.drawMeasurement(Direction.up, width-backPillarDistance, backPillarDistance, 10);
    svg.drawMeasurement(Direction.left, 0, 35, 10);
    svg.drawMeasurement(Direction.left, height-35, 35, 10);
    for(int horizontal = frontPillarDistance; horizontal < width ; horizontal += horizontalPilarDistance){
      svg.drawMeasurement(Direction.up, horizontal, horizontalPilarDistance, 10);
      for(int vertical = 35; vertical < height; vertical += verticalPilarDistance){
        svg.drawRectCenter(horizontal,vertical, pillarWidth);
        svg.drawMeasurement(Direction.left, vertical, verticalPilarDistance, 10);
        items.add("Pillar"); //// TODO change to pillarDTO.
      }
    }
    /*
    for(int horizontal = frontPillarDistance; horizontal < width ; horizontal += horizontalPilarDistance) {
      svg.drawRectCenter(horizontal,height - 35, pillarWidth);
    }
    svg.drawRectCenter(width-backPillarDistance, 35, pillarWidth);
    svg.drawMeasurement(Direction.left, height - 35, 35, 10);
    items.add("Pillar"); //// TODO change to pillarDTO.*/

    //// Beams


    return svg;
  }

  private static ArrayList<Float> getBeamLengths(String materialID){
    ////ToDo replace with mapper
    return new ArrayList<Float>(Stream.of(300F,360F,420F,480F,540F,600F).toList());
  }

  private static float getBestLengthOver(ArrayList<Float> arr, float length){
    return arr.stream().dropWhile(l -> l <= length).limit(1).toList().get(0);
  }

  private static float getBestLengthUnder(ArrayList<Float> arr, float length){
    return arr.stream().dropWhile(l -> l >= length).limit(1).toList().get(0);
  }

  //// for tests
  public static void main(String[] args){
    ArrayList<Float> testArr = getBeamLengths("");
    System.out.println(getBestLengthOver(testArr, 450F));
    System.out.println(testArr);


    ArrayList<Object> partsList = new ArrayList<>();
    String output;
    try {
      SVG svg = drawCarportDraft1(partsList, 780,600);
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
    partsList.forEach(System.out::println);
  }
}
