package geneticalgo;

import java.util.List;

public interface Crossover <T extends Individual> {

    List<T> crossover(List<T> individuals);

}
