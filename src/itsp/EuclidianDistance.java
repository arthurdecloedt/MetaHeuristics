package itsp;

public class EuclidianDistance implements DistanceMetric {


    @Override
    public double distanceBetween(ProcessingNode n1, ProcessingNode n2) {
        double dx = n2.getX()-n1.getX();
        double dy = n2.getY()-n1.getY();
        return Math.sqrt(dx*dx+dy*dy);
    }
}
