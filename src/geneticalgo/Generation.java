package geneticalgo;

import java.util.List;

public class Generation <T extends Individual> {

    private final List<T> individuals;

    public Generation(List<T> individuals){
        this.individuals = individuals;
    }

    public List<T> getIndividuals() {
        return individuals;
    }
}
