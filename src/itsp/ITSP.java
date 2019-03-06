package itsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ITSP {

    private final ArrayList<ProcessingNode> nodes;
    private final int numNodes;
    private final double[][] distances;

    private ITSP(List<ProcessingNode> nodes, DistanceMetric distanceMetric){
        this.nodes = new ArrayList<>(nodes);
        this.numNodes = nodes.size();

        distances = new double[numNodes][numNodes];

        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                if(i == j)
                    distances[i][j] = 0;
                else if(i > j)
                    distances[i][j] = distances[j][i];
                else
                    distances[i][j] = distanceMetric.distanceBetween(nodes.get(i), nodes.get(j));
            }
        }
    }

    public ProcessingNode getNode(int index) {
        return nodes.get(index);
    }

    public double getDistance(int i1, int i2) {
        return distances[i1][i2];
    }

    public static ITSP generateRandom(int numNodes, double width, double height, int minProcessing, int maxProcessing){
        List<ProcessingNode> nodes = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numNodes; i++) {
            double x = random.nextDouble()*width;
            double y = random.nextDouble()*height;
            int p = random.nextInt(maxProcessing-minProcessing)+minProcessing;
            nodes.add(new ProcessingNode(x, y, p));
        }

        return new ITSP(nodes, new EuclidianDistance());
    }

}
