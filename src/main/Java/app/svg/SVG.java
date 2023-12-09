package app.svg;

import app.svg.shapes.*;

import java.util.ArrayList;

public class SVG {
  private final ArrayList<Shape> svgContent = new ArrayList<>();
  private final ArrayList<Measurement> measures = new ArrayList<>();
  private float minX = 0;
  private float maxX = 0;
  private float startX;
  private float minY = 0;
  private float maxY = 0;
  private float startY;
  private float unitPerCentiPixel = 100;

  /**
   * Default Constructor, a 100 by 100 window.
   * You use this to draw shapes, once done use toString to get the code.
   */
  public SVG() {
    setWindow(0, 100, 0, 100);
  }

  /**
   * Constructor with windows size defined.
   * You use this to draw shapes, once done use toString to get the code.
   * @param sizeX
   * Horizontal Size.
   * @param sizeY
   * Vertical Size.
   */
  public SVG(float sizeX, float sizeY) {
    setWindow(0, sizeX, 0, sizeY);
  }

  /**
   * resizes the window, without changing the starting position.
   * @param startX
   * Horizontal left limit
   * @param endX
   * Horizontal right limit
   * @param startY
   * Vertical upper limit
   * @param endY
   * Vertical lower limit
   */
  public void setWindow(float startX, float endX, float startY, float endY) {
    this.startX = startX;
    if(startX < minX) minX = startX;
    if(endX > maxX) maxX = endX;
    this.startY = startY;
    if(startY < minY) minY = startY;
    if(endY > maxY) maxY = endY;
  }

  /**
   *
   * @param upp
   */
  public void setUnitPerCentiPixel(float upp){
    unitPerCentiPixel = upp;
  }

  private float upp(){
    return 100 / unitPerCentiPixel;
  }

  public void drawRect(float x, float x2, float y, float y2) {
    svgContent.add(new Rectangle(startX + (x * upp()), startY + (y * upp()),(x2-x) * upp(),(y2-y) * upp()));
  }

  public void drawRectCenter(float x, float y, float size) {
    drawRect(x - size / 2, x + size / 2,y - size / 2, y + size / 2);
  }

  public void drawRectWithMeasurement(float x, float x2, float y, float y2, float measurementDistance){
    drawRect(x, x2, y, y2);
    if(x-10<minX){
      minX = x - 10;
    }
    if(y-10<minY){
      minY = y - 10;
    }

    //// Horizontal
    boolean newHorizontal = true;
    for (Measurement measurement : measures) {
      if (measurement.isSameMeasuremeant(BiDirection.horizontal, x, x2 - x)) {
        newHorizontal = false;
        break;
      }
    }
    if(newHorizontal) {
      measures.add(new Measurement(BiDirection.horizontal, measurementDistance, x, y, x2 - x, String.valueOf((x2 - x) / unitPerCentiPixel)));
    }

    //// Vertical
    boolean newVertical = true;
    for (Measurement measurement : measures) {
      if (measurement.isSameMeasuremeant(BiDirection.vertical, y, y2 - y)) {
        newVertical = false;
        break;
      }
    }
    if(newVertical) {
      measures.add(new Measurement(BiDirection.vertical, measurementDistance, x, y, y2-y, String.valueOf((y2-y)/unitPerCentiPixel)));
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    svgContent.forEach(shape -> {
      sb.append(shape.draw(-startX, -startY));
    });
    measures.forEach(measurement -> {
      sb.append(measurement.draw(-startX, -startY));
    });
    return "<svg" +
        " width='" + (maxX - minX) + "'" +
        " height='" + (maxY - minY) + "'" +
        ">" + sb
        + "\nSorry your browser does not support inline svg, we cannot show a drawing in this browser.\n</svg>";
  }

  //// For Testing
  public static void main(String[] args){
    SVG svg = new SVG(200, 200);
    svg.drawRectWithMeasurement(50,200, 50, 200, 10);
    svg.drawRectWithMeasurement(50,200, 50, 100, 30);
    svg.drawRectWithMeasurement(50,150, 50, 200, 30);
    System.out.println(svg);
  }
}
