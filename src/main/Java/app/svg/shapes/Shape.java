package app.svg.shapes;

import java.io.Serializable;

public abstract class Shape implements Serializable {
  public float opacity = 1;
  public abstract String draw(float offsetX, float offsetY, float scale);
}
