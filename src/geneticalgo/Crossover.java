package geneticalgo;

import java.util.List;

public interface Crossover <T extends Individual> {

    List<T> crossover(T p1, T p2);

    double getCrossoverChance();

  void decayParameters();
}
