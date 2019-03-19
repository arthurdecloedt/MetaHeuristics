package geneticalgo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Generation <T extends Individual> {

    private final List<T> individuals;
    private final int size;

    public Generation(List<T> individuals){
        this.individuals = individuals;
        this.size = individuals.size();
    }

    public List<T> getIndividuals() {
        return individuals;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Generation [size "+getSize()+"]: "+
                getIndividuals().stream().sorted(Comparator.comparing(Individual::getFitness)).map(Objects::toString).collect(Collectors.joining(", "));
    }
}
