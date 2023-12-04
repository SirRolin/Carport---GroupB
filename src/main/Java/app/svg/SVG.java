package app.svg;

public class SVG {
  private final StringBuilder svgContent = new StringBuilder();
  private int minX = 0;
  private int maxX = 0;
  private int startX;
  private int endX;
  private int minY = 0;
  private int maxY = 0;
  private int startY;
  private int endY;
  private float unitPerProcent = 100;

  public SVG() {
    setWindow(0, 100, 0, 100);
  }

  public SVG(int sizeX, int sizeY) {
    setWindow(0, sizeX, 0, sizeY);
  }

  public void setWindow(int startX, int endX, int startY, int endY) {
    this.startX = startX;
    if(startX < minX) minX = startX;
    this.endX = endX;
    if(endX > maxX) maxX = endX;
    this.startY = startY;
    if(startY < minY) minY = startY;
    this.endY = endY;
    if(endY > maxY) maxY = endY;
  }
  public void setUnitPerProcent(float upp){
    unitPerProcent = upp;
  }

  private float upp(){
    return 100/unitPerProcent;
  }

  public void drawRect(int x, int x2, int y, int y2) {
    svgContent.append("<rect x='").append(startX + x * upp()).append("'");
    svgContent.append(" y='").append(startY + y * upp()).append("'");
    svgContent.append(" rx='0'").append(" ry='0'");
    svgContent.append(" width='").append(endX - startX + x2 * upp()).append("'");
    svgContent.append(" height='").append(endY - startY + y2 * upp()).append("'");
    svgContent.append(" style='fill:none;stroke:black;stroke-width:1;opacity:1;/>\n");
  }

  public void drawRectCenter(int x, int y, int size) {
    drawRect(x - size << 1, x + size << 1,y - size << 1, y + size << 1);
  }

  public void addTextCenter(int x, int y, int fontSize, String text) {

  }

  @Override
  public String toString() {
    return "<svg" +
        " width='" + (maxX - minX) + "'" +
        " height='" + (maxY - minY) + "'" +
        ">" + svgContent + "</svg>";
  }
}
