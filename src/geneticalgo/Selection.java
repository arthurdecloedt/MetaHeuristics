package geneticalgo;

import java.util.List;

public interface Selection <T extends Individual> {

     List<T> select(List<T> individuals, int amount);

}
