package app.svg.shapes;

public class Line extends Shape {
  private final float x1;
  private final float y1;
  private final float x2;
  private final float y2;
  private String stroke = "black";
  public float stroke_width = 1;
  /**
   * between 0.0 and 1.0
   */
  public double opacity = 1.0;

  public Line(float x1, float y1, float x2, float y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  public void setColour(String colour) {
    this.stroke = colour;
  }

  public float getX1() {
    return x1;
  }

  public float getY1() {
    return y1;
  }

  public float getX2() {
    return x2;
  }

  public float getY2() {
    return y2;
  }

  @Override
  public String draw(float offsetX, float offsetY, float scale) {
    return "<line x1='" + (x1 + offsetX) * scale
        + "' y1='" + (y1 + offsetY) * scale
        + "' x2='" + (x2 + offsetX) * scale
        + "' y2='" + (y2 + offsetY) * scale
        + "' style='stroke:" + stroke
        + "; stroke-width:" + stroke_width * scale + ";"
        + (opacity==1 ? "": "opacity:" + opacity + ";")
        + "'/>";
  }
}
