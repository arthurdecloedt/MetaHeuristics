package geneticalgo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneticAlgorithm <T extends Individual> {

    private List<Generation<T>> generations;
    private int currentGeneration;
    private final int generationSize;
    private final Crossover<T> crossover;
    private final List<Mutation<T>> mutations;
    private final Selection<T> parentSelection, survivorSelection;

    public GeneticAlgorithm(Generation<T> initialGeneration, Crossover<T> crossover, List<Mutation<T>> mutations, Selection<T> parentSelection, Selection<T> survivorSelection){
        generations = new ArrayList<>();
        generations.add(initialGeneration);
        currentGeneration = 0;
        generationSize = initialGeneration.getSize();

        this.crossover = crossover;
        this.mutations = mutations;
        this.parentSelection = parentSelection;
        this.survivorSelection = survivorSelection;
    }

    public void nextGeneration() {
        Generation<T> current = getGeneration();

        List<T> parents = parentSelection.select(current.getIndividuals(), generationSize);

        List<T> crossed = new ArrayList<>();
        for (int i = 0; i < parents.size()/2; i++) {
            crossed.addAll(crossover.crossover(parents.get(i*2), parents.get(i*2+1)));
        }

        //Create one function combining all mutations and then applying it to all new offspring.
        Function<T, T> combinedMutations =
                mutations.stream().map(m -> ((Function<T, T>) m::mutate)).reduce(Function::andThen).get();
        List<T> mutated = crossed.stream().map(combinedMutations).collect(Collectors.toList());

        mutated.addAll(current.getIndividuals());

        List<T> survivors = survivorSelection.select(mutated, generationSize);

        currentGeneration++;
        generations.add(new Generation<T>(survivors));
    }

    public Generation<T> getGeneration(int i) {
        return generations.get(i);
    }

    public Generation<T> getGeneration() {
        return getGeneration(currentGeneration);
    }

}
