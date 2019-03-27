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
        Random rnd=new Random();
        long seed = rnd.nextLong();
        ITSPFactory factory=new ITSPFactory();
        factory.setBound(50);
        for (int j = 1; j<5; j++) {
            double itspcrossover=j*0.05;
            factory.reseed(seed);
            ITSPInstance problemInstance = factory.instanceUniform(100,new EuclidianDistance(100));
            Generation<ITSPIndividual> initial = new Generation<>(Stream.generate(problemInstance::generateRandomIndividual).limit(100).collect(Collectors.toList()));

            GeneticAlgorithm<ITSPIndividual> ga = new GeneticAlgorithm<ITSPIndividual>(
                    initial,
                    new ITSPCrossover(.15),
                    Arrays.asList(new MutationCombine(.15), new MutationSplit(.15), new MutationSwitch(.15), new MutationChangePT(.15)),
                    new TournamentSelection<>(j),
                    new FitnessSelection<>()
            );

            //GuiITSP gui = new GuiITSP(problemInstance);

            //System.out.println(problemInstance.getNodes().stream().mapToInt(ProcessingNode::getProcessingTime).sum());
            //System.out.println("Nodes:"+ nodenr +"Uniform ");
            int ln = 500;
            Double[] prog = new Double[ln];
            for (int i = 0; i < ln ; i++) {

    //            System.out.println(ga.getGeneration().getIndividuals().stream().sorted(Comparator.comparingDouble(ITSPIndividual::calculateFitness)).findFirst().get());
                if (i%10==0) {

                    //gui.showGeneration(ga.getGeneration());
                    //(new Scanner(System.in)).nextLine();
                }
                prog[i]=ga.getGeneration().getIndividuals().stream().mapToDouble(ITSPIndividual::calculateFitness).summaryStatistics().getAverage();
                ga.nextGeneration();
            }
            System.out.println(Arrays.stream(prog).map(Object::toString).collect(Collectors.joining(",")));
        }
    }
}
