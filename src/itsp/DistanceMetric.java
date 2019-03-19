package itsp;

public interface DistanceMetric {

    /**
     * Calculates the distance between two nodes, expressed in timesteps.
     *
     * @param n1
     * @param n2
     * @return
     */
    public int distanceBetween(ProcessingNode n1, ProcessingNode n2);

}
