package itsp;

import geneticalgo.Mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ITSPMutation implements Mutation<ITSPIndividual> {

    /**
     * Helper method for mutation
     *
     * @param visits
     * @return
     */
    protected int[] selectTwoAtSameNode(List<ITSPVisit> visits){
        Map<Integer, List<ITSPVisit>> perNode = visits.stream().collect(Collectors.groupingBy(ITSPVisit::getNodeId));

        int[] indices = IntStream.range(0, visits.size()).filter(i->perNode.get(visits.get(i).getNodeId()).size()>1).toArray();
        if(indices.length == 0)
            return null;

        //Pick first visit
        int i1 = indices[random.nextInt(indices.length)];
        int n1 = visits.get(i1).getNodeId();

        indices = IntStream.of(indices).filter(i -> i != i1 && visits.get(i).getNodeId() == n1).toArray();
        if(indices.length == 0)
            return null;

        //Pick second visit
        int i2 = indices[random.nextInt(indices.length)];
        return new int[]{i1, i2};
    }

    /**
     * Helper method for mutation
     *
     * @param visits
     * @param i1
     * @param i2
     * @return
     */
    protected List<ITSPVisit> replaceAt(List<ITSPVisit> visits, int i1, int i2, ITSPVisit v1, ITSPVisit v2) {
        List<ITSPVisit> part1 = visits.subList(0, i1);
        List<ITSPVisit> part2 = visits.subList( i1+1, i2);
        List<ITSPVisit> part3 = visits.subList( i2+1, visits.size());

        List<ITSPVisit> newVisits = new ArrayList<>();
        newVisits.addAll(part1);
        if(v1 != null)
            newVisits.add(v1);
        newVisits.addAll(part2);
        if(v2 != null)
            newVisits.add(v2);
        newVisits.addAll(part3);

        return newVisits;
    }

    protected Random random = new Random();

}
