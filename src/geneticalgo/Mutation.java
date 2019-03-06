package geneticalgo;

public interface Mutation <T extends Individual> {

    T mutate(T individual);

}
