package geneticalgo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneticAlgorithm <T extends Individual> {

    private boolean decay = false;
    private List<Generation<T>> generations;
    private int currentGeneration;
    private final int generationSize;
    private final Crossover<T> crossover;
    private final List<Mutation<T>> mutations;
    private final Selection<T> parentSelection, survivorSelection;
    private final Random random = new Random();
    private Predicate<Generation<T>> constraint;

    public GeneticAlgorithm(Generation<T> initialGeneration, Crossover<T> crossover, List<Mutation<T>> mutations, Selection<T> parentSelection, Selection<T> survivorSelection){
        generations = new ArrayList<>();
        generations.add(initialGeneration);
        currentGeneration = 0;
        generationSize = initialGeneration.getSize();
        this.crossover = crossover;
        this.mutations = mutations;
        this.parentSelection = parentSelection;
        this.survivorSelection = survivorSelection;
        this.constraint= tGeneration -> false;
    }
    public GeneticAlgorithm(Generation<T> initialGeneration, Crossover<T> crossover, List<Mutation<T>> mutations, Selection<T> parentSelection, Selection<T> survivorSelection,boolean decay) {
        this(initialGeneration, crossover, mutations, parentSelection, survivorSelection);
        this.decay = decay;
    }
    public void addConstraint(Predicate<Generation<T>> constraint){this.constraint=constraint;

    }

    public void nextGeneration() {
        Generation<T> current = getGeneration();

        List<T> parents = parentSelection.select(current.getIndividuals(), generationSize);

        List<T> crossed = new ArrayList<>();
        for (int i = 0; i < parents.size()/2; i++) {
            if(random.nextDouble() < crossover.getCrossoverChance()) {
                crossed.addAll(crossover.crossover(parents.get(i * 2), parents.get(i * 2 + 1)));
            } else {
                crossed.add(parents.get(i * 2));
                crossed.add(parents.get(i * 2 + 1));
            }
        }

        Stream<T> mStream = crossed.stream();
        for(Mutation<T> mutation: mutations) {
            if(random.nextDouble() < mutation.getMutationChance()) {
                mStream = mStream.map(mutation::mutate);
            }
        }
        List<T> mutated = mStream.collect(Collectors.toList());


        mutated.addAll(current.getIndividuals());

        List<T> survivors = survivorSelection.select(mutated, generationSize);

        currentGeneration++;
        generations.add(new Generation<T>(survivors));
        if (decay){
            this.crossover.decayParameters();
            this.mutations.forEach(Mutation::decayParameters);
            System.out.println("Mutation chance" + this.mutations.get(0).getMutationChance());
        }
    }
    public void untilComplete(){
        int n=0;
        while(!constraint.test(getGeneration())){
            nextGeneration();
            if (n++ == 500){
                break;
            }

        }
    }
    public double meanFitness(){
        return getGeneration().getMean();
    }
    public double getMeanVar(){
        return getGeneration().getMeanVar();
    }

    public Generation<T> getGeneration(int i) {
        return generations.get(i);
    }

    public Generation<T> getGeneration() {
        return getGeneration(currentGeneration);
    }

}
