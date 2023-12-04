package app.svg.shapes;

public class Rectangle implements Shape {

  private float x;
  private float y;
  private float width;
  private float height;
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
  public String draw() {
    return "<rect x='" + x + "'" +
        " y='" + y + "'" +
        " rx='" + roundX + "'" +
        " ry='" + roundY + "'" +
        " width='" + width + "'" +
        " height='" + height + "'" +
        style() +
        "/>\n";
  }

  private String style(){
    return " style='" +
        "fill:" + fill + ";" +
        "stroke:" + stroke + ";" +
        "stroke-width:" + stroke_width + ";" +
        "opacity:" + 1 + ";' ";
  }

  //// For Testing
  public static void main(String[] args){
    Shape rect = new Rectangle(50,50,150,150);
    System.out.println(rect.draw());
  }
}
