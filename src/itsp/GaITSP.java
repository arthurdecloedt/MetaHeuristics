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
        ITSPInstance problemInstance = ITSPInstance.generateRandom(20, 20, 20, 5, 50);
        Generation<ITSPIndividual> initial = new Generation<>(Stream.generate(problemInstance::generateRandomIndividual).limit(20).collect(Collectors.toList()));

        GeneticAlgorithm<ITSPIndividual> ga = new GeneticAlgorithm<ITSPIndividual>(
                initial,
                (p1, p2)->Arrays.asList(p1, p2),
                Arrays.asList(new MutationCombine(), new MutationSplit(), new MutationSwitch(), new MutationChangePT()),
                new TournamentSelection<ITSPIndividual>(4),
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
