package itsp;

import com.sun.org.apache.regexp.internal.RE;
import geneticalgo.Individual;

import java.util.List;
import java.util.stream.Collectors;

public class ITSPIndividual extends Individual {

    private final ITSPInstance problemInstance;
    private final List<ITSPVisit> visits;
    private List<Integer> waitingTimes;

    public ITSPIndividual(ITSPInstance problemInstance, List<ITSPVisit> visits){
        this.problemInstance = problemInstance;
        this.visits = visits;
    }

    @Override
    protected double calculateFitness() {
        int totalTravelled = 0;
        for (int i = 0; i < visits.size() - 1; i++) {
            totalTravelled += problemInstance.getDistance(visits.get(i).getNodeId(), visits.get(i+1).getNodeId());
        }
        return totalTravelled + visits.stream().mapToInt(ITSPVisit::getTime).sum() + getWaitingTimes().stream().mapToInt(i->i).sum();
    }

    @Override
    public String toString() {
        return "ITSPIndividual f="+getFitness()+":\n"+visits.stream().map(v->"\t"+v+"\n").collect(Collectors.joining());
    }

    public List<Integer> getWaitingTimes() {
        if(waitingTimes == null) {
            waitingTimes = problemInstance.calculateWaitingTimes(this);
        }
        return waitingTimes;
    }

    public List<ITSPVisit> getVisits() {
        return visits;
    }

    public ITSPInstance getProblemInstance(){
        return problemInstance;
    }
}
