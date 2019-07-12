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
        Generation<ITSPIndividual> initial = new Generation<>(Stream.generate(problemInstance::generateRandomIndividual).limit(100).collect(Collectors.toList()));
        double decay=0.0001;
        GeneticAlgorithm<ITSPIndividual> ga = new GeneticAlgorithm<ITSPIndividual>(
                initial,
                new ITSPCrossover(0.75,decay),
                Arrays.asList(new MutationCombine(0.75,decay), new MutationSplit(0.75,decay), new MutationSwitch(0.55,decay), new MutationChangePT(0.75,decay)),
                new TournamentSelection<ITSPIndividual>(5),
                new FitnessSelection<ITSPIndividual>(),
                true

        );

        //GuiITSP gui = new GuiITSP(problemInstance);

        System.out.println(problemInstance.getNodes().stream().mapToInt(ProcessingNode::getProcessingTime).sum());
        while(true){
            System.out.println(ga.getGeneration().getIndividuals().stream().sorted(Comparator.comparingDouble(ITSPIndividual::calculateFitness)).findFirst().get());
            /* System.out.println(ga.getGeneration().getIndividuals().stream()
             .mapToDouble(ITSPIndividual::calculateFitness).sorted()
             .mapToObj(Objects::toString).collect(Collectors.joining(", "))); */
            //gui.showGeneration(ga.getGeneration());
            //(new Scanner(System.in)).nextLine();
            System.out.println(ga.getMeanVar());
            System.out.println(ga.meanFitness());
            ga.nextGeneration();

        }
    }

}
