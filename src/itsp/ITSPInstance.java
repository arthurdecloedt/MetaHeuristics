package itsp;

import geneticalgo.Individual;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ITSPInstance {

    private final ArrayList<ProcessingNode> nodes;
    private final int numNodes;
    private final int[][] distances;
    private static final Random random = new Random();

    private ITSPInstance(List<ProcessingNode> nodes, DistanceMetric distanceMetric){
        this.nodes = new ArrayList<>(nodes);
        this.numNodes = nodes.size();

        distances = new int[numNodes][numNodes];

        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                if(i == j)
                    distances[i][j] = 0;
                else if(i > j)
                    distances[i][j] = distances[j][i];
                else
                    distances[i][j] = distanceMetric.distanceBetween(nodes.get(i), nodes.get(j));
            }
        }
    }

    public ProcessingNode getNode(int index) {
        return nodes.get(index);
    }

    public int getDistance(int i1, int i2) {
        return distances[i1][i2];
    }

    public int getNumNodes() {
        return numNodes;
    }

    public ArrayList<ProcessingNode> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return "ITSP instance:\n" + nodes.stream().map(ProcessingNode::toString).map(s->"\t"+s+"\n").collect(Collectors.joining());
    }

    public static ITSPInstance generateRandom(int numNodes, double width, double height, int minProcessing, int maxProcessing){
        List<ProcessingNode> nodes = new ArrayList<>();

        for (int i = 0; i < numNodes; i++) {
            double x = random.nextDouble()*width;
            double y = random.nextDouble()*height;
            int p = random.nextInt(maxProcessing-minProcessing)+minProcessing;
            nodes.add(new ProcessingNode(i, x, y, p));
        }

        return new ITSPInstance(nodes, new EuclidianDistance(1));
    }

    List<Integer> calculateWaitingTimes(ITSPIndividual individual) {
        List<ITSPVisit> visits = individual.getVisits();
        List<Integer> waitingTimes = new ArrayList<>(individual.getVisits().size());
        int[] temps = new int[numNodes];
        Arrays.fill(temps, Temperature.MINTEMP);
        for (int i = 0; i < visits.size(); i++) {
            int n = visits.get(i).getNodeId();
            int pt = visits.get(i).getTime();
            int wt = Temperature.getWaitingTime(temps[n], pt);
            waitingTimes.add(wt);

            if(i == visits.size()-1)
                break;

            int tt = getDistance(n, visits.get(i+1).getNodeId());
            for (int j = 0; j < temps.length; j++) {
                if(j==n)
                    temps[j] = Temperature.getCoolingTempAfter(Temperature.getWorkingTempAfter(temps[j], pt), tt);
                else
                    temps[j] = Temperature.getCoolingTempAfter(temps[j], pt+wt+tt);
            }
        }
        return waitingTimes;
    }

    public ITSPIndividual generateRandomIndividual() {
        double earlyStopChance = 0.25;
        List<ProcessingNode> nodesCopy = new ArrayList<>(nodes);
        List<ITSPVisit> visits = new ArrayList<>();

        while (true) {
            int[] remaining = IntStream.range(0, nodesCopy.size()).filter(n -> nodesCopy.get(n).getProcessingTime() > 0).toArray();
            if (remaining.length == 0)
                break;

            int i = remaining[random.nextInt(remaining.length)];
            ProcessingNode node = nodesCopy.get(i);

            int pt = node.getProcessingTime() > 1 && random.nextDouble() < earlyStopChance ? random.nextInt(node.getProcessingTime()-1)+1 : node.getProcessingTime();
            nodesCopy.set(i, new ProcessingNode(node.getId(), node.getX(), node.getY(), node.getProcessingTime() - pt));

            visits.add(new ITSPVisit(i, pt));
        }

        return new ITSPIndividual(this, visits);
    }

    public static void main(String[] args) {
        ITSPInstance problem = generateRandom(20, 100, 100, 5, 50);
        System.out.println(Arrays.toString(Arrays.stream(problem.distances).map(Arrays::toString).toArray()));
        System.out.println(problem);
        List<ITSPIndividual> inds = Stream.generate(problem::generateRandomIndividual).limit(100).sorted(Comparator.comparingDouble(Individual::getFitness)).collect(Collectors.toList());
        System.out.println(inds.get(0));
        System.out.println(inds.get(inds.size()-1));
        System.out.println(inds.stream().sorted(Comparator.comparingInt(i->i.getVisits().size())).findFirst().get());
    }

}
