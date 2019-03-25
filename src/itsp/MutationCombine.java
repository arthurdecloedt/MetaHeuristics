package itsp;

import java.util.List;

public class MutationCombine extends ITSPMutation {

    public MutationCombine(double chance) {
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

        ITSPVisit combined = new ITSPVisit(visits.get(i1).getNodeId(), visits.get(i1).getTime()+visits.get(i2).getTime());

        List<ITSPVisit> newVisits = replaceAt(visits, i1, i2, combined, null);

        return new ITSPIndividual(individual.getProblemInstance(), newVisits);
    }
/*
    public static void main(String[] args) {
        ITSPInstance problem = ITSPInstance.generateRandom(5, 100, 100, 5, 50);
        ITSPIndividual ind = problem.generateRandomIndividual();
        System.out.println(ind);
        ind = new MutationCombine(1).mutate(ind);
        System.out.println(ind);
    }*/

}
