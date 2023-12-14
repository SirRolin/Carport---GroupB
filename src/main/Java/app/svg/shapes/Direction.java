package app.svg.shapes;

public enum Direction {
  left,
  right,
  up,
  down,
  upperLeft,
  upperRight,
  lowerLeft,
  lowerRight;
  public Direction opposite(){
    switch(this){
      case left -> {
        return right;
      }
      case right -> {
        return left;
      }
      case up -> {
        return down;
      }
      case down -> {
        return up;
      }
      case upperLeft -> {
        return lowerRight;
      }
      case upperRight -> {
        return lowerLeft;
      }
      case lowerLeft -> {
        return upperRight;
      }
      case lowerRight -> {
        return upperLeft;
      }
    }
    //// should never be able to get here, unless we add another state.
    return null;
  }
}
