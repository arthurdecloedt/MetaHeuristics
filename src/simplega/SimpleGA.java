package simplega;

import geneticalgo.Generation;
import geneticalgo.GeneticAlgorithm;
import geneticalgo.Individual;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleGA {

    public static void main(String[] args) {
        List<Individual> initial = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            initial.add(new SimpleIndividual(random.nextDouble()*10));
        }

        double target = 5;
        System.out.println("Target: "+target);
        Function<SimpleIndividual, Double> fitness = i -> (i.getValue()-target)*(i.getValue()-target);

        GeneticAlgorithm<SimpleIndividual> alg = new GeneticAlgorithm<SimpleIndividual>(
                new Generation(initial),
                individuals -> {
                    List<SimpleIndividual> res = new ArrayList<>();
                    for (int i = 0; i < individuals.size()/2; i++) {
                        double v1 = individuals.get(i*2).getValue();
                        double v2 = individuals.get(i*2+1).getValue();
                        res.add(new SimpleIndividual(0.3*v1+0.7*v2));
                        res.add(new SimpleIndividual(0.7*v1+0.3*v2));
                    }
                    if(individuals.size()%2 != 0)
                        res.add(individuals.get(individuals.size()-1));
                    return res;
                },
                Collections.singletonList(
                        (SimpleIndividual individual) -> new SimpleIndividual(individual.getValue()+random.nextGaussian())
                ),
                (individuals, amount) -> {
                    return individuals.stream().sorted(Comparator.comparing(fitness)).limit(amount).collect(Collectors.toList());}
        );

        while(true){
            alg.getGeneration().getIndividuals().stream().sorted(Comparator.comparing(fitness)).forEach(i -> System.out.print(i.getValue()+", "));
            System.out.println();
            (new Scanner(System.in)).nextLine();
            alg.nextGeneration();
        }
    }

    private static class SimpleIndividual implements Individual {

        private final double value;

        SimpleIndividual(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }
    }

}
