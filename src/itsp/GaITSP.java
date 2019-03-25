package itsp;

import geneticalgo.FitnessSelection;
import geneticalgo.Generation;
import geneticalgo.GeneticAlgorithm;
import geneticalgo.TournamentSelection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GaITSP {

    public static void main(String[] args) {
        ITSPFactory factory=new ITSPFactory();
        ITSPInstance problemInstance = factory.instancesNormal(100,new EuclidianDistance(10),50,50,6);
        Generation<ITSPIndividual> initial = new Generation<>(Stream.generate(problemInstance::generateRandomIndividual).limit(100).collect(Collectors.toList()));

        GeneticAlgorithm<ITSPIndividual> ga = new GeneticAlgorithm<ITSPIndividual>(
                initial,
                new ITSPCrossover(0.75),
                Arrays.asList(new MutationCombine(0.5), new MutationSplit(0.5), new MutationSwitch(0.5), new MutationChangePT(0.5)),
                new TournamentSelection<ITSPIndividual>(5),
                new FitnessSelection<ITSPIndividual>()
        );


        GuiITSP gui = new GuiITSP(problemInstance);

        System.out.println(problemInstance.getNodes().stream().mapToInt(ProcessingNode::getProcessingTime).sum());
        while(true){
            System.out.println(ga.getGeneration().getIndividuals().stream().sorted(Comparator.comparingDouble(ITSPIndividual::calculateFitness)).findFirst().get());
            System.out.println(ga.getGeneration().getIndividuals().stream()
                    .mapToDouble(ITSPIndividual::calculateFitness).sorted()
                    .mapToObj(Objects::toString).collect(Collectors.joining(", ")));
            gui.showGeneration(ga.getGeneration());
            (new Scanner(System.in)).nextLine();
            ga.nextGeneration();
        }
    }

}
