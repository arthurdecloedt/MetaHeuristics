package itsp;

import com.sun.javaws.exceptions.InvalidArgumentException;

public class EuclidianDistance implements DistanceMetric {

    private final double speed;

    public double getSpeed() {
        return speed;
    }

    /**
     * Calculate using euclidean distance provided distance per timestep.
     *
     * @param speed
     */
     public EuclidianDistance(double speed) {
        if(speed<=0) throw new IllegalArgumentException("speed should be positive");
        this.speed = speed;
    }

    @Override
    public int distanceBetween(ProcessingNode n1, ProcessingNode n2) {
        double dx = n2.getX()-n1.getX();
        double dy = n2.getY()-n1.getY();
        return (int) Math.ceil(Math.sqrt(dx*dx+dy*dy)/speed);
    }
}
