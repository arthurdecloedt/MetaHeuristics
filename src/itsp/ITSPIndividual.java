package itsp;

import com.sun.org.apache.regexp.internal.RE;
import geneticalgo.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ITSPIndividual extends Individual {

    private final ITSPInstance problemInstance;
    private final List<ITSPVisit> visits;
    private List<Integer> waitingTimes;

    public ITSPIndividual(ITSPInstance problemInstance, List<ITSPVisit> visits){
        this.problemInstance = problemInstance;
        //Merge duplicate visits
        List<ITSPVisit> visitsNew = new ArrayList<>();
        for(ITSPVisit visit: visits) {
            if(visitsNew.isEmpty() || visitsNew.get(visitsNew.size()-1).getNodeId() != visit.getNodeId())
                visitsNew.add(visit);
            else
                visitsNew.set(visitsNew.size()-1, new ITSPVisit(visit.getNodeId(), visit.getTime()+visitsNew.get(visitsNew.size()-1).getTime()));
        }
        this.visits = visitsNew;
    }

    @Override
    protected double calculateFitness() {
        return getTravellingTime() + getProcessingTime() + getWaitingTime();
    }

    public double getTravellingTime(){
        int totalTravelled = 0;
        for (int i = 0; i < visits.size() - 1; i++) {
            totalTravelled += problemInstance.getDistance(visits.get(i).getNodeId(), visits.get(i+1).getNodeId());
        }
        return totalTravelled;
    }

    public double getProcessingTime(){
        return visits.stream().mapToInt(ITSPVisit::getTime).sum();
    }

    public double getWaitingTime(){
        return getWaitingTimes().stream().mapToInt(i->i).sum();
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
