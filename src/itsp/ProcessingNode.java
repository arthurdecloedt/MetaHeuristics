package itsp;

public class ProcessingNode {

    private final double x, y;
    private final int processingTime;

    public ProcessingNode(double x, double y, int processingTime){
        this.x = x;
        this.y = y;
        this.processingTime = processingTime;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getProcessingTime() {
        return processingTime;
    }

}
