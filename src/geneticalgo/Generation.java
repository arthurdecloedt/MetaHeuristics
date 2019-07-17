package geneticalgo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

public class Generation <T extends Individual> {

    private final List<T> individuals;
    private final int size;
    private boolean meanc=false;
    private double mean=0;
    private double meanVar =0;

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    private int generation;
    public Generation(List<T> individuals){
        this.individuals = individuals;
        this.size = individuals.size();
    }
    public double getMean(){
        if(meanc) return mean;
        List<Double> flist = this.getIndividuals().stream().map(Individual::getFitness).collect(Collectors.toList());
        this.mean = (flist.stream().reduce(0.0,Double::sum))/flist.size();
        this.meanVar = (flist.stream().map(x->pow((mean-x),2)).reduce(0.0,Double::sum)) / flist.size();
        return mean;

    }

    public double getMeanVar(){
        if(meanc) return meanVar;
        List<Double> flist = this.getIndividuals().stream().map(Individual::getFitness).collect(Collectors.toList());
        this.mean = (flist.stream().reduce(0.0,Double::sum))/flist.size();
        meanVar = (flist.stream().map(x->pow((mean-x),2)).reduce(0.0,Double::sum)) / flist.size();
        return meanVar;
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
