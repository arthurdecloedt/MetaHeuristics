package itsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ITSPFactory {


  Random random = new Random();



  private int bound=50;

  public void reseed(long seed){
      random=new Random(seed);
  }

  public void setBound(int bound) {
    this.bound = bound;
  }

  public void setXval(int xval) {
    this.xval = xval;
  }

  public void setYval(int yval) {
    this.yval = yval;
  }

  int xval = 1000;
  int yval = 1000;

  public ITSPInstance instanceUniform(int nodenr, DistanceMetric distanceMetric) {
    List<ProcessingNode> nodes = new ArrayList<>();

    for (int i = 0; i < nodenr; i++) {
      double x = random.nextDouble() * xval;
      double y = random.nextDouble() * yval;
      int p = random.nextInt(bound);
      nodes.add(new ProcessingNode(i, x, y, p));
    }
    int[][] distances = getDistances(nodenr, distanceMetric, nodes);
    return new ITSPInstance(nodes, distances);



  }

  public ITSPInstance instancesNormal(int nodenr, DistanceMetric distanceMetric, double meanx, double meany, double sdev) {
    List<ProcessingNode> nodes = new ArrayList<>();

    for (int i = 0; i < nodenr; i++) {
      double x, y;
      do {
        x = random.nextGaussian() * sdev + meanx;
      } while (x <= 0 || x >= xval);
      do {
        y = random.nextDouble() * sdev + meany;
      } while (y <= 0 || y >= xval);
      int p = random.nextInt(bound);
      nodes.add(new ProcessingNode(i, x, y, p));
    }
    int[][] distances = getDistances(nodenr, distanceMetric, nodes);
    return new ITSPInstance(nodes, distances);


  }

  private int[][] getDistances(int nodenr, DistanceMetric distanceMetric, List<ProcessingNode> nodes) {
    int[][] distances = new int[nodenr][nodenr];

    for (int i = 0; i < nodenr; i++) {
      for (int j = 0; j < nodenr; j++) {
        if (i == j) {
          distances[i][j] = 0;
        } else if (i > j) {
          distances[i][j] = distances[j][i];
        } else {
          distances[i][j] = distanceMetric.distanceBetween(nodes.get(i), nodes.get(j));
        }
      }
    }
    return distances;
  }


}
