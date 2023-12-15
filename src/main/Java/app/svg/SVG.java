package app.svg;

import app.svg.shapes.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class SVG implements Serializable {
  //// a set of invalid directions to try and draw measurements with.
  private static Direction[] validDirections = new Direction[]{Direction.up, Direction.left};

  //// the SVG content
  private final ArrayList<Shape> svgContent = new ArrayList<>();
  private final ArrayList<Measurement> measures = new ArrayList<>();

  //// window information for drawing
  private float minX = 0;
  private float maxX = 0;
  private float minY = 0;
  private float maxY = 0;

  //// start of the current drawing
  private float startX;
  private float startY;

  //// scaling
  public float scaling = 1;

  //// Settings we can tweak to see better results with texts.
  static public float extraTextRoomX = 20;
  static public float extraTextRoomY = 10;

  //// Text formatting
  public static String textFormat = "%.2f";

  /**
   * The SVG class can be used to draw with shapes and measurement which automatically expands as needed.
   */
  public SVG() {
    setWindow(0, 0);
  }

  /**
   * Changes the start position of the drawing, allowing multiple measurement positions.
   * @param startX
   * Horizontal start
   * @param startY
   * Vertical start
   */
  public void setWindow(float startX, float startY) {
    this.startX = startX;
    this.startY = startY;
  }

  /**
   * Draws a rectangle at the given cordinates (after scaling)
   * @param x left offset from the start
   * @param x2 right offset from the start
   * @param y upper offset from the start
   * @param y2 lower offset from the start
   */
  public void drawRect(float x, float x2, float y, float y2) {
    if(x + startX - 10 < minX){
      minX = (x) + startX - 10;
    }
    if(y + startY - 10 < minY){
      minY = (y) + startY - 10;
    }
    if(x2 + startX > maxX){
      maxX = (x2) + startX + 5;
    }
    if(y2 + startY > maxY){
      maxY = (y2) + startY + 5;
    }
    svgContent.add(new Rectangle(startX + (x), startY + (y),(x2-x),(y2-y)));
  }

  /**
   * Draws an equal sided square with the center at x, y.
   * @param x Horizontal offset from the start
   * @param y Vertical offset from the start
   * @param size diameter of square
   */

  public void drawRectCenter(float x, float y, float size) {
    drawRect(x - (size / 2), x + (size / 2),y - (size / 2), y + (size / 2));
  }

  /**
   * Draws a Measurement if it doesn't already exist without adding anything else to the drawing.
   * @param direction
   * @param startOffset
   * @param length
   * @param measurementDistance
   * @throws IllegalArgumentException throws an error if an invalid (not implemented) direction is input.
   */
  public void drawMeasurement(Direction direction,float startOffset, float length, float measurementDistance) throws IllegalArgumentException {
    if(!Arrays.stream(validDirections).anyMatch(dir -> dir == direction)){
      throw new IllegalArgumentException("direction can be up or left, cause I don't want to implement the other unless needed."); //// though I did start a bit.
    }
    boolean alreadyExists = false;
    for (Measurement measurement : measures) {
      if (measurement.isSameMeasurement(direction, startX, startY, startOffset, length)) {
        alreadyExists = true;
        break;
      }
    }
    if (!alreadyExists) {
      switch (direction){
        case left -> {
          if(startX - measurementDistance - 20 < minX){
            minX = startX - measurementDistance - 20;
          }
        }
        case up -> {
          if(startY - measurementDistance - 20 < minY){
            minY = startY - measurementDistance - 20;
          }
        }
        case right -> {
          if(startX + measurementDistance + 20 > maxX){
            maxX = startX + measurementDistance + 20;
          }
        }
        case down -> {
          if(startY + measurementDistance + 20 < maxY){
            maxY = startY + measurementDistance + 20;
          }
        }
      }
      measures.add(
          new Measurement(direction,
              measurementDistance,
              startX + ((direction == Direction.up || direction == Direction.down) ? startOffset : 0),
              startY + ((direction == Direction.left || direction == Direction.right) ? startOffset : 0),
              length,
              String.format(textFormat,length / 100)
          )
      );
    }
  }

  /**
   * Combines Drawing a rectangle and drawing its measurements in 1 call.
   * @param x left offset from the start
   * @param x2 right offset from the start
   * @param y upper offset from the start
   * @param y2 lower offset from the start
   * @param measurementHorizontal null for no measurement, otherwise changes the distance the measurements are drawn at from the main starting point.
   * @param measurementVertical null for no measurement, otherwise changes the distance the measurements are drawn at from the main starting point.
   */
  public void drawRectWithMeasurement(float x, float x2, float y, float y2, Float measurementHorizontal, Float measurementVertical){
    drawRect(x, x2, y, y2);
    if(measurementHorizontal!=null) drawMeasurement(Direction.up, x, x2 - x, measurementHorizontal);
    if(measurementVertical!=null) drawMeasurement(Direction.left, y, y2 - y, measurementVertical);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    measures.forEach(measurement -> {
      sb.append(measurement.draw(-minX, -minY, scaling)).append("\n");
    });
    svgContent.forEach(shape -> {
      sb.append(shape.draw(-minX, -minY, scaling)).append("\n");
    });
    return "<svg" +
        " width='" + ((maxX - minX) * scaling) + "'" +
        " height='" + ((maxY - minY) * scaling) + "'" +
        ">\n" + sb
        + "Sorry your browser does not support inline svg, we cannot show a drawing in this browser.\n</svg>";
  }

  //// For Testing
  //// Draws a rectangle with measurement + 3 measurements spanning over it
  public static void main(String[] args){
    SVG svg = new SVG();
    svg.drawRectWithMeasurement(0,100, 0, 50, 30F, 10F);
    svg.drawMeasurement(Direction.up, 100 / 3F * 0, 100 / 3F, 10F);
    svg.drawMeasurement(Direction.up, 100 / 3F * 1, 100 / 3F, 10F);
    svg.drawMeasurement(Direction.up, 100 / 3F * 2, 100 / 3F, 10F);
    System.out.println(svg);
  }
}
