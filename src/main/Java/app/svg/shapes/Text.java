package app.svg.shapes;

import app.svg.Direction;

public class Text extends Shape {
  private final float x;
  private final float y;
  private final String txt;
  public String size = "1em";
  private String colour = "black";
  private String baseline;
  private String anchor;

  public Text(float x, float y, String txt, Direction direction) {
    switch (direction){
      case left -> {
        baseline = "middle";
        anchor = "end";
      }
      case right -> {
        baseline = "middle";
        anchor = "start";
      }
      case up -> {
        baseline = "text-after-edge";
        anchor = "middle";
      }
      case down -> {
        baseline = "text-before-edge";
        anchor = "middle";
      }
    }
    this.x = x;
    this.y = y;
    this.txt = txt;
  }

  public void setColour(String colour){
    this.colour = colour;
  }

  @Override
  public String draw(float offsetX, float offsetY, float scale) {
    return "<text x='" + (x + offsetX) * scale
        + "' y='" + (y + offsetY) * scale
        + "' dominant-baseline='" + baseline
        + "' text-anchor='" + anchor
        + "' style='font-size:" + size + ";"
        + " fill:" + colour + ";"
        + ((opacity != 1) ? "opacity:" + opacity: "")
        + "'>" + txt + "</text>";
  }
}
