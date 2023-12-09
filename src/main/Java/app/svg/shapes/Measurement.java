package app.svg.shapes;

public class Measurement implements Shape {
  private Triangle firstArrowhead;
  private Triangle secondArrowhead;
  private Line line;
  private Line firstSide;
  private Line secondSide;
  private Text text;
  private final BiDirection biDirection;
  public static String defaultArrowColour = "black";
  public static String defaultAssistLineColour = "green";
  public static String defaultTextColour = "black";

  public Measurement(BiDirection direction, float measureDistance, float x, float y, float length, String text, String arrowColour, String assistLineColour, String textColour) {

    //// Direction
    switch (direction){
      case vertical -> {
        this.line = new Line(x-measureDistance, y, x-measureDistance, y + length);
        this.firstArrowhead = new Triangle(x-measureDistance-5, y, 10, Direction.up);
        this.secondArrowhead = new Triangle(x-measureDistance-5, y+length-10, 10, Direction.down);
        this.firstSide = new Line(x,y,x-measureDistance, y);
        this.secondSide = new Line(x,y+length,x-measureDistance, y+length);
        this.text = new Text(x-measureDistance-2, y+length/2, text, Direction.left);
      }
      case horizontal -> {
        this.line = new Line(x, y-measureDistance, x + length, y-measureDistance);
        this.firstArrowhead = new Triangle(x, y-measureDistance-5, 10, Direction.left);
        this.secondArrowhead = new Triangle(x+length-10, y-measureDistance-5, 10, Direction.right);
        this.firstSide = new Line(x, y, x, y-measureDistance);
        this.secondSide = new Line(x+length,y,x+length, y-measureDistance);
        this.text = new Text(x+length/2, y-measureDistance-2, text, Direction.up);
      }
    }
    this.biDirection = direction;

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
  public Measurement(BiDirection direction, float measureDistance, float x, float y, float length, String text) {
    this(direction, measureDistance, x, y, length, text, defaultArrowColour, defaultAssistLineColour, defaultTextColour);
  }

  public boolean isSameMeasuremeant(BiDirection biDirection, float start, float length) {
    if(this.biDirection!=biDirection) return false;
    switch (biDirection){
      case vertical -> {
        return ((line.getY2() - line.getY1()) == length) && (start == line.getY1());
      }
      case horizontal -> {
        return ((line.getX2() - line.getX1()) == length) && (start == line.getX1());
      }
    }
    return false;
  }

  @Override
  public String draw(double offsetX, double offsetY) {
    return line.draw(offsetX,offsetY) + "\n" + firstArrowhead.draw(offsetX, offsetY) + "\n" + secondArrowhead.draw(offsetX, offsetY) + "\n" + firstSide.draw(offsetX, offsetY) + "\n" + secondSide.draw(offsetX, offsetY) + "\n" + text.draw(offsetX, offsetY);
  }
}
