package itsp;

public class ITSPVisit {

    private final int nodeId;
    private final int time;

    public ITSPVisit(int nodeId, int time){
        this.nodeId = nodeId;
        this.time = time;
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("Visit [n=%d t=%d]", nodeId, time);
    }
}
