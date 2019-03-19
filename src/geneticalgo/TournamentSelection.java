package geneticalgo;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TournamentSelection<T extends Individual> implements Selection<T> {

    private final int tournamentSize;
    private final Random random = new Random();

    public TournamentSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public List select(List<T> individuals, int amount) {
        return IntStream.range(0, amount).mapToObj(i->selectSingle(individuals)).collect(Collectors.toList());
    }

    private T selectSingle(List<T> individuals) {
        return IntStream.range(0, tournamentSize).mapToObj(i->individuals.get(random.nextInt(individuals.size())))
                .min(Comparator.comparingDouble(Individual::getFitness)).get();
    }
}
