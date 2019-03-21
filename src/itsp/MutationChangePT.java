package itsp;

import java.util.List;

public class MutationChangePT extends ITSPMutation {

    public MutationChangePT(double chance) {
        super(chance);
    }

    @Override
    public ITSPIndividual mutate(ITSPIndividual individual) {
        List<ITSPVisit> visits = individual.getVisits();
        int[] inds = selectTwoAtSameNode(visits);
        if(inds == null || inds.length != 2)
            return individual;
        int i1 = Math.min(inds[0], inds[1]);
        int i2 = Math.max(inds[0], inds[1]);

        int pt = visits.get(i1).getTime()+visits.get(i2).getTime();
        int pt1 = random.nextInt(pt-1)+1;
        int pt2 = pt-pt1;

        ITSPVisit v1 = new ITSPVisit(visits.get(i1).getNodeId(), pt1);
        ITSPVisit v2 = new ITSPVisit(visits.get(i2).getNodeId(), pt2);
        List<ITSPVisit> newVisits = replaceAt(visits, i1, i2, v1, v2);

        return new ITSPIndividual(individual.getProblemInstance(), newVisits);
    }

    public static void main(String[] args) {
        ITSPInstance problem = ITSPInstance.generateRandom(5, 100, 100, 5, 50);
        ITSPIndividual ind = problem.generateRandomIndividual();
        System.out.println(ind);
        ind = new MutationChangePT(1).mutate(ind);
        System.out.println(ind);
    }

}
