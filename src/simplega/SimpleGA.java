package simplega;

import geneticalgo.*;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleGA {

    public static void main(String[] args) {
        List<SimpleIndividual> initial = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            initial.add(new SimpleIndividual(random.nextDouble()*10));
        }

        GeneticAlgorithm<SimpleIndividual> alg = new GeneticAlgorithm<SimpleIndividual>(
                new Generation<>(initial),
                (p1, p2) -> {
                    double v1 = p1.getValue();
                    double v2 = p2.getValue();
                    return Arrays.asList(new SimpleIndividual(0.3*v1+0.7*v2), new SimpleIndividual(0.7*v1+0.3*v2));
                },
                Collections.singletonList(
                        (SimpleIndividual individual) -> new SimpleIndividual(individual.getValue()+random.nextGaussian())
                ),
                new TournamentSelection<>(4),
                new FitnessSelection<>()
        );

        while(true){
            System.out.println(alg.getGeneration());
            (new Scanner(System.in)).nextLine();
            alg.nextGeneration();
        }
    }

    private static class SimpleIndividual extends Individual {

        private final double value;

        SimpleIndividual(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        protected double calculateFitness() {
            return Math.min(Math.min((2-value)*(2-value)+0.01, (8-value)*(8-value)+0.01), (5-value)*(5-value));
        }

        @Override
        public String toString() {
            return value+"";
        }
    }

}
