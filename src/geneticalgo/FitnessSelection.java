package geneticalgo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FitnessSelection<T extends Individual> implements Selection<T> {
    @Override
    public List<T> select(List<T> individuals, int amount) {
        return individuals.stream().sorted(Comparator.comparing(Individual::getFitness)).limit(amount).collect(Collectors.toList());
    }
}
