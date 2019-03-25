package itsp;

public class ManhattanDistance implements DistanceMetric {
  /**
   * Calculates the manhattan distance between two nodes, expressed in timesteps.
   *
   * @param n1
   * @param n2
   * @return
   */
  @Override
  public int distanceBetween(ProcessingNode n1, ProcessingNode n2) {

    return (int) (Math.abs(n1.getX()-n2.getX())+Math.abs(n1.getY()-n2.getY()));
  }
}
