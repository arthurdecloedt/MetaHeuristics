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
        factory.setBound(50);
        for (int j = 1; j<9; j++) {
            double itspcrossover=j*0.1;
            ITSPInstance problemInstance = factory.instanceUniform(50,new EuclidianDistance(100));
            Generation<ITSPIndividual> initial = new Generation<>(Stream.generate(problemInstance::generateRandomIndividual).limit(100).collect(Collectors.toList()));

            GeneticAlgorithm<ITSPIndividual> ga = new GeneticAlgorithm<ITSPIndividual>(
                    initial,
                    new ITSPCrossover(itspcrossover),
                    Arrays.asList(new MutationCombine(0.5), new MutationSplit(0.5), new MutationSwitch(0.5), new MutationChangePT(0.5)),
                    new TournamentSelection<>(5),
                    new FitnessSelection<>()
            );

            //GuiITSP gui = new GuiITSP(problemInstance);

            //System.out.println(problemInstance.getNodes().stream().mapToInt(ProcessingNode::getProcessingTime).sum());
            //System.out.println("Nodes:"+ nodenr +"Uniform ");
            for (int i = 0; i < 250 ; i++) {

    //            System.out.println(ga.getGeneration().getIndividuals().stream().sorted(Comparator.comparingDouble(ITSPIndividual::calculateFitness)).findFirst().get());
                if (i%10==0) {

                    //gui.showGeneration(ga.getGeneration());
                    //(new Scanner(System.in)).nextLine();
                }
                ga.nextGeneration();
            }
            System.out.println( ga.getGeneration().getIndividuals().stream()
                .mapToDouble(ITSPIndividual::calculateFitness).sorted()
                .mapToObj(Objects::toString).collect(Collectors.joining(",")));


        }
    }
}
