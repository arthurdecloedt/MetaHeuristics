package itsp;

public class ProcessingNode {

    private final int id;
    private final double x, y;
    private final int processingTime;

    public ProcessingNode(int id, double x, double y, int processingTime){
        this.id = id;
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

    @Override
    public String toString() {
        return String.format("<Node [%.2f, %.2f] t=%d>", getX(), getY(), getProcessingTime());
    }

    public int getId() {
        return id;
    }
}
