package itsp;

import java.util.ArrayList;
import java.util.List;

public class MutationSplit extends ITSPMutation {

    public MutationSplit(double chance) {
        super(chance);
    }

    @Override
    public ITSPIndividual mutate(ITSPIndividual individual) {
        List<ITSPVisit> visits = individual.getVisits();
        int i = random.nextInt(visits.size());
        if(visits.get(i).getTime() <= 1)
            return individual;
        int t1 = random.nextInt(visits.get(i).getTime()-1)+1;
        int t2 = visits.get(i).getTime()-t1;

        List<ITSPVisit> newVisits = new ArrayList<>(visits);
        newVisits.set(i, new ITSPVisit(visits.get(i).getNodeId(), t1));
        int insert = random.nextInt(newVisits.size());
        List<ITSPVisit> part1 = newVisits.subList(0, insert);
        List<ITSPVisit> part2 = newVisits.subList( insert, visits.size());

        newVisits = new ArrayList<>(part1);
        newVisits.add(new ITSPVisit(visits.get(i).getNodeId(), t2));
        newVisits.addAll(part2);

        return new ITSPIndividual(individual.getProblemInstance(), newVisits);
    }

/*
    public static void main(String[] args) {
        ITSPInstance problem = ITSPInstance.generateRandom(5, 100, 100, 5, 50);
        ITSPIndividual ind, ind2;
        int i = 0;
        do {
            ind = problem.generateRandomIndividual();
            ind2 = new MutationSplit(1).mutate(ind);
            i++;
        } while(ind.getFitness() <= ind2.getFitness());
        System.out.println(i);
        System.out.println(ind);
        System.out.println(ind2);
    }
*/

}
