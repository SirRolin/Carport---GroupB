package app.svg;

public class Engine {
  public static void drawCarportDraft1(StringBuilder svg){
    svg.setLength(0);
    svg.append("svg");
  }
  public static void main(String[] args){
    StringBuilder text = new StringBuilder("hello");
    System.out.println(text);
    drawCarportDraft1(text);
    System.out.println(text);
  }
}
