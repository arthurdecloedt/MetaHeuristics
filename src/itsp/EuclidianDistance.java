package itsp;

public class EuclidianDistance implements DistanceMetric {

    private final double speed;

    /**
     * Calculate using euclidean distance provided distance per timestep.
     *
     * @param speed
     */
    public EuclidianDistance(double speed) {
        this.speed = speed;
    }

    @Override
    public int distanceBetween(ProcessingNode n1, ProcessingNode n2) {
        double dx = n2.getX()-n1.getX();
        double dy = n2.getY()-n1.getY();
        return (int) Math.ceil(Math.sqrt(dx*dx+dy*dy)/speed);
    }
}
