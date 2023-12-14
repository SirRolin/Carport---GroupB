package app.svg.shapes;

public abstract class Shape {
  public float opacity = 1;
  public abstract String draw(float offsetX, float offsetY, float scale);
}
