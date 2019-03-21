package itsp;

import java.util.ArrayList;
import java.util.List;

public class MutationSwitch extends ITSPMutation {

    public MutationSwitch(double chance) {
        super(chance);
    }

    @Override
    public ITSPIndividual mutate(ITSPIndividual individual) {
        List<ITSPVisit> visits = individual.getVisits();
        if(visits.isEmpty())
            return individual;
        int r1 = random.nextInt(visits.size());
        int r2 = random.nextInt(visits.size());
        if(r1 == r2)
            return individual;
        int i1 = Math.min(r1, r2);
        int i2 = Math.max(r1, r2);

        List<ITSPVisit> newVisits = replaceAt(visits, i1, i2, visits.get(i2), visits.get(i1));

        return new ITSPIndividual(individual.getProblemInstance(), newVisits);
    }

    public static void main(String[] args) {
        ITSPInstance problem = ITSPInstance.generateRandom(5, 100, 100, 5, 50);
        ITSPIndividual ind = problem.generateRandomIndividual();
        System.out.println(ind);
        ind = new MutationSwitch(1).mutate(ind);
        System.out.println(ind);
    }

}
