package app.svg.shapes;

public class Triangle implements Shape {
  private final float x;
  private final float y;
  private final float sizeX;
  private final float sizeY;
  private final Direction direction;
  private String colour = "black";

  public Triangle(float x, float y, float size, Direction direction) {
    this.x = x;
    this.y = y;
    this.sizeX = size;
    this.sizeY = size;
    this.direction = direction;
  }

  public Triangle(float x, float y, float sizeX, float sizeY, Direction direction) {
    this.x = x;
    this.y = y;
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.direction = direction;
  }

  public void setColour(String colour) {
    this.colour = colour;
  }

  @Override
  public String draw(double offsetX, double offsetY) {
    String points = ""; 
    double x2 = x + offsetX;
    double y2 = y + offsetY;
    switch (direction){
      case right -> {
        points = x2 + "," + y2 + " " + (x2 + sizeX) + "," + (y2 + sizeY/2) + " " + x2 + "," + (y2 + sizeY);
      }
      case left -> {
        points = (x2 + sizeX) + "," + y2 + " " + x2 + "," + (y2 + sizeY/2) + " " + (x2 + sizeX) + "," + (y2 + sizeY);
      }
      case down -> {
        points = x2 + "," + y2 + " " + (x2 + sizeX) + "," + y2 + " " + (x2 + sizeX/2) + "," + (y2 + sizeY);
      }
      case up -> {
        points = (x2 + sizeX/2) + "," + y2 + " " + (x2 + sizeX) + "," + (y2 + sizeY) + " " + x2 + "," + (y2 + sizeY);
      }
      case lowerLeft -> {
        points = x2 + "," + y2 + " " + (x2 + sizeX) + "," + (y2 + sizeY) + " " + x2 + "," + (y2 + sizeY);
      }
      case lowerRight -> {
        points = (x2 + sizeX) + "," + y2 + " " + (x2 + sizeX) + "," + (y2 + sizeY) + " " + x2 + "," + (y2 + sizeY);
      }
      case upperLeft -> {
        points = x2 + "," + y2 + " " + (x2 + sizeX) + "," + y2 + " " + x2 + "," + (y2 + sizeY);
      }
      case upperRight -> {
        points = x2 + "," + y2 + " " + (x2 + sizeX) + "," + (y2 + sizeY) + " " + (x2 + sizeX) + "," + y2;
      }
    }
    return "<polyline points='" + points + "' fill='" + colour + "' stroke='" + colour + "' />";
  }
}
