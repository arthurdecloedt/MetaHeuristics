package itsp;

import geneticalgo.Crossover;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ITSPCrossover implements Crossover<ITSPIndividual> {

    private final Random random = new Random();
    private final double chance;

    public ITSPCrossover(double chance) {
        this.chance = chance;
    }

    @Override
    public List<ITSPIndividual> crossover(ITSPIndividual p1, ITSPIndividual p2) {
        List<ITSPVisit> visits1 = p1.getVisits();
        List<ITSPVisit> visits2 = p2.getVisits();
        ITSPInstance problemInstance = p1.getProblemInstance();
        int numNodes = problemInstance.getNumNodes();

        if(visits1.isEmpty() || visits2.isEmpty())
            return Collections.emptyList();

        Map<Integer, List<VisitWrapper>> nodeMapping = getNodeMapping(problemInstance, visits1, visits2);

        int currentNode = random.nextDouble() < 0.5 ?
                random.nextDouble() < 0.5 ? visits1.get(0).getNodeId(): visits1.get(visits1.size()-1).getNodeId():
                random.nextDouble() < 0.5 ? visits2.get(0).getNodeId(): visits2.get(visits2.size()-1).getNodeId();

        List<ITSPVisit> newVisits = new ArrayList<>();
        while(nodeMapping.values().stream().anyMatch(l -> !l.isEmpty())) {
            //Select different node if this one is done
            if(currentNode < 0 || nodeMapping.get(currentNode).isEmpty())
                currentNode = pickRandom(IntStream.range(0, numNodes).filter(i -> !nodeMapping.get(i).isEmpty()).boxed().collect(Collectors.toList()));

            //Select next visit
            List<VisitWrapper> visitWrappers = nodeMapping.get(currentNode);
            VisitWrapper selected = pickRandom(visitWrappers);
            visitWrappers.remove(selected);
            newVisits.add(new ITSPVisit(currentNode, selected.visit.getTime()));

            if(selected.prev < 0)
                currentNode = selected.next;
            else if(selected.next < 0)
                currentNode = selected.prev;
            else
                currentNode = random.nextDouble() < 0.5 ? selected.prev : selected.next;
        }
        return Collections.singletonList(new ITSPIndividual(problemInstance, newVisits));
    }

    private <T> T pickRandom(List<T> l) {
        return l.get(random.nextInt(l.size()));
    }

    private Map<Integer, List<VisitWrapper>> getNodeMapping(ITSPInstance problemInstance, List<ITSPVisit> visits1, List<ITSPVisit> visits2) {
        int numNodes = problemInstance.getNumNodes();
        Map<Integer, List<VisitWrapper>> nodeMapping = getNodeMapping(visits1, new HashMap<>());
        nodeMapping = getNodeMapping(visits2, nodeMapping);

        //Select visits per node
        for(int node = 0; node < numNodes; node++) {
            List<VisitWrapper> allVisits = nodeMapping.get(node);
            List<VisitWrapper> selectedVisits = new ArrayList<>();
            int ptLeft = problemInstance.getNode(node).getProcessingTime();
            while(ptLeft > 0) {
                int sel = random.nextInt(allVisits.size());
                VisitWrapper selVisit = allVisits.get(sel);
                allVisits.remove(sel);
                if(selVisit.visit.getTime() > ptLeft)
                    selVisit = new VisitWrapper(new ITSPVisit(selVisit.visit.getNodeId(), ptLeft), selVisit.prev, selVisit.next);
                ptLeft -= selVisit.visit.getTime();
                selectedVisits.add(selVisit);
            }
            nodeMapping.put(node, selectedVisits);
        }
        return nodeMapping;
    }

    private Map<Integer, List<VisitWrapper>> getNodeMapping(List<ITSPVisit> visits, Map<Integer, List<VisitWrapper>> initial) {
        for (int i = 0; i < visits.size(); i++) {
            ITSPVisit visit = visits.get(i);
            if(!initial.containsKey(visit.getNodeId()))
                initial.put(visit.getNodeId(), new ArrayList<>());
            int prev = i > 0 ? visits.get(i-1).getNodeId() : -1;
            int next = i < visits.size()-1 ? visits.get(i+1).getNodeId() : -1;
            initial.get(visit.getNodeId()).add(new VisitWrapper(visit, prev, next));
        }
        return initial;
    }

    @Override
    public double getCrossoverChance() {
        return chance;
    }

    private class VisitWrapper {
        private final ITSPVisit visit;
        private int prev, next;

        public VisitWrapper(ITSPVisit visit, int prev, int next) {
            this.visit = visit;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return visit+", "+prev+", "+next;
        }
    }

    public static void main(String[] args) {
        ITSPInstance problem = ITSPInstance.generateRandom(7, 100, 100, 5, 50);

        ITSPIndividual ind1 = problem.generateRandomIndividual();
        ITSPIndividual ind2 = problem.generateRandomIndividual();
        System.out.println(ind1);
        System.out.println(ind2);
        List<ITSPIndividual> inds = new ITSPCrossover(1).crossover(ind1, ind2);
        System.out.println(inds.get(0));

        GuiITSP gui = new GuiITSP(problem);
        gui.showIndividual(ind1);
        gui.showIndividual(ind2);
        gui.showIndividual(inds.get(0));
    }

}
