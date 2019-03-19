package geneticalgo;

import java.util.Optional;

public abstract class Individual {

    private Optional<Double> fitness;

    public Individual() {
        this.fitness = Optional.empty();
    }

    public double getFitness(){
        if(!fitness.isPresent())
            fitness = Optional.of(calculateFitness());
        return fitness.get();
    }

    /**
     * Calculate the individuals fitness, stored after calculation to prevent recalculating.
     * Lower fitness is better.
     * @return
     */
    protected abstract double calculateFitness();

    @Override
    public String toString() {
        return "[Ind f="+getFitness()+"]";
    }
}
