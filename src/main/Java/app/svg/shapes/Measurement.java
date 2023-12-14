package app.svg.shapes;

public class Measurement extends Shape {
  private float startX;
  private float startY;
  private Triangle firstArrowhead;
  private Triangle secondArrowhead;
  private Line line;
  private Line firstSide;
  private Line secondSide;
  private Text text;
  private final Direction direction;
  public static String defaultArrowColour = "black";
  public static String defaultAssistLineColour = "green";
  public static String defaultTextColour = "black";
  public static String defaultTextSize = "1em";
  public static int decimals = 2;

  public Measurement(Direction direction, float measureDistance, float x, float y, float length, String text, String arrowColour, String assistLineColour, String textColour) {
    startX = x;
    startY = y;
    //// Direction
    switch (direction){
      case left -> {
        this.line = new Line(x-measureDistance, y, x-measureDistance, y + length);
        this.firstArrowhead = new Triangle(x-measureDistance-5, y, 10, Direction.up);
        this.secondArrowhead = new Triangle(x-measureDistance-5, y+length-10, 10, Direction.down);
        this.firstSide = new Line(x,y,x-measureDistance, y);
        this.secondSide = new Line(x,y+length,x-measureDistance, y+length);
        this.text = new Text(x-measureDistance-2, y+length/2, text, Direction.left);
      }
      case up -> {
        this.line = new Line(x, y-measureDistance, x + length, y-measureDistance);
        this.firstArrowhead = new Triangle(x, y-measureDistance-5, 10, Direction.left);
        this.secondArrowhead = new Triangle(x+length-10, y-measureDistance-5, 10, Direction.right);
        this.firstSide = new Line(x, y, x, y-measureDistance);
        this.secondSide = new Line(x+length,y,x+length, y-measureDistance);
        this.text = new Text(x+length/2, y-measureDistance-2, text, Direction.up);
      }
    }
    this.direction = direction;
    this.text.size = defaultTextSize;
    this.firstSide.opacity = 0.5;
    this.secondSide.opacity = 0.5;

    //// Arrows including both heads and the line.
    line.setColour(arrowColour);
    firstArrowhead.setColour(arrowColour);
    secondArrowhead.setColour(arrowColour);

    //// The lines from the end of the arrows to the box.
    firstSide.setColour(assistLineColour);
    secondSide.setColour(assistLineColour);

    //// Text
    this.text.setColour(textColour);
  }
  public Measurement(Direction direction, float measureDistance, float x, float y, float length, String text) {
    this(direction, measureDistance, x, y, length, text, defaultArrowColour, defaultAssistLineColour, defaultTextColour);
  }

  public boolean isSameMeasuremeant(Direction direction, float startX, float startY, float start, float length) {
    if(this.direction!=direction) return false;
    switch (direction){
      case left -> {
        return ((line.getY2() - line.getY1()) == length)
            && (start == line.getY1())
            && ((this.startX == startX) && (this.startY == startY));
      }
      case up -> {
        return ((line.getX2() - line.getX1()) == length) && (start == line.getX1()) && ((this.startX == startX) && (this.startY == startY));
      }
    }
    return false;
  }

  @Override
  public String draw(float offsetX, float offsetY, float scale) {
    return line.draw(offsetX,offsetY, scale) + "\n"
        + firstArrowhead.draw(offsetX, offsetY, scale) + "\n"
        + secondArrowhead.draw(offsetX, offsetY, scale) + "\n"
        + firstSide.draw(offsetX, offsetY, scale) + "\n"
        + secondSide.draw(offsetX, offsetY, scale) + "\n"
        + text.draw(offsetX, offsetY, scale);
  }
}
