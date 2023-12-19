package app.svg.shapes;

public class Rectangle extends Shape {

  private final float x;
  private final float y;
  private final float width;
  private final float height;
  public float roundX = 0;
  public float roundY = 0;
  public String fill = "none";
  public String stroke = "black";
  public float stroke_width = 1;

  public Rectangle(float x, float y, float width, float height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public String draw(float offsetX, float offsetY, float scale) {
    return "<rect x='" + (x + offsetX) * scale + "'" +
        " y='" + (y + offsetY) * scale + "'" +
        " rx='" + roundX * scale + "'" +
        " ry='" + roundY * scale + "'" +
        " width='" + width * scale + "'" +
        " height='" + height * scale + "'" +
        style() +
        "/>";
  }

  private String style(){
    return " style='" +
        "fill:" + fill + ";" +
        "stroke:" + stroke + ";" +
        "stroke-width:" + stroke_width + ";" +
        ((opacity != 1) ? "opacity:" + opacity + ";": "")
        + "' ";
  }
}
