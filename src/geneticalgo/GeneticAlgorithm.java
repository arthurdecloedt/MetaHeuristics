package geneticalgo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeneticAlgorithm <T extends Individual> {

    private List<Generation<T>> generations;
    private int currentGeneration;
    private final Crossover<T> crossover;
    private final List<Mutation<T>> mutations;
    private final Selection<T> selection;

    public GeneticAlgorithm(Generation<T> initialGeneration, Crossover<T> crossover, List<Mutation<T>> mutations, Selection<T> selection){
        generations = new ArrayList<>();
        generations.add(initialGeneration);
        currentGeneration = 0;

        this.crossover = crossover;
        this.mutations = mutations;
        this.selection = selection;
    }

    public void nextGeneration() {
        Generation<T> current = getGeneration();

        List<T> crossed = crossover.crossover(current.getIndividuals());

        Function<T, T> combinedMutations =
                mutations.stream().map(m-> ((Function<T, T>) m::mutate))
                .collect(Collectors.reducing((m1, m2)->m1.andThen(m2))).get();
        List<T> mutated = crossed.stream().map(combinedMutations).collect(Collectors.toList());
        mutated.addAll(current.getIndividuals());

        List<T> selected = selection.select(mutated, current.getIndividuals().size());

        currentGeneration++;
        generations.add(new Generation<T>(selected));
    }

    public Generation<T> getGeneration(int i) {
        return generations.get(i);
    }

    public Generation<T> getGeneration() {
        return getGeneration(currentGeneration);
    }

}
