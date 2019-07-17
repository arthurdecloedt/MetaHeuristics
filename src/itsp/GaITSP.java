package itsp;

import geneticalgo.FitnessSelection;
import geneticalgo.Generation;
import geneticalgo.GeneticAlgorithm;
import geneticalgo.TournamentSelection;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GaITSP {

  public static void main(String[] args) {
    Random rnd = new Random();
    long seed = rnd.nextLong();
    ITSPFactory factory = new ITSPFactory();
    factory.setBound(50);
    for (int j = 1; j < 5; j++) {
      double itspcrossover = j * 0.05;
      factory.reseed(seed);
      ITSPInstance problemInstance = factory.instanceUniform(100, new EuclidianDistance(100));
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
      Consumer<Generation<ITSPIndividual>> consumer = csvExporter();
      for (int i = 0; i < ln; i++) {
        consumer.accept(ga.getGeneration());

        prog[i] = ga.getGeneration().getIndividuals().stream().mapToDouble(ITSPIndividual::calculateFitness).summaryStatistics().getAverage();
        ga.nextGeneration();

      }
      //System.out.println(Arrays.stream(prog).map(Object::toString).collect(Collectors.joining(",")));
    }
  }


    public static Consumer<Generation<ITSPIndividual>> csvExporter(){
      Random  rand = new Random();
      String suffix = Integer.toString(rand.nextInt(100000));
      String path = "./out/output/log_" + suffix+ ".csv";
      FileWriter csvWriter;
      try {
        csvWriter = new FileWriter(path);
        csvWriter.append("Generation,Optimal Fitness,Mean Fitness,Mean Variance\n");

      } catch (IOException e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
        return generation -> { };
      }
      Consumer<Generation<ITSPIndividual>> generationConsumer = new Consumer<Generation<ITSPIndividual>>() {

        FileWriter csvWriter;
        @Override
        public void accept(Generation<ITSPIndividual> generation) {
          double mean =generation.getMean();
          double meanVar = generation.getMeanVar();
          double fittest = generation.getIndividuals().stream().mapToDouble(ITSPIndividual::calculateFitness).min().orElseThrow(() -> new RuntimeException("no minimum Fittest individual found"));
          String[] line = {Integer.toString(generation.getGeneration()),Double.toString(mean),Double.toString(fittest),Double.toString(meanVar),"\n"};
          try {
            System.out.println(csvWriter.getEncoding());
            csvWriter.append(String.join(",",line));
            csvWriter.flush();
          } catch (IOException e) {
            System.err.println("unable to write to csv file");
          }
        }
        private Consumer<Generation<ITSPIndividual>> setCsvWriter(FileWriter fileWriter){
          this.csvWriter=fileWriter;
          return this;
        }
      }.setCsvWriter(csvWriter);
      System.out.println("Csv writer created at:");
      System.out.println(path);
      return generationConsumer;
    }

}
