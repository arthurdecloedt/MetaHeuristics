package itsp;

import geneticalgo.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GaITSP {

  public static void main(String[] args) {
    DistanceMetric dist =new EuclidianDistance(100);
    for (int m = 0; m<5;m++) {
      double chance =.5;
      System.out.println( "____________________");
      long seed = 129948393L;
      ITSPFactory factory = new ITSPFactory();
      factory.reseed(seed);
      List<GeneticAlgorithm<ITSPIndividual>> testSuite = new ArrayList<>();
      factory.setBound(500);
      for (int j = 1; j < 100; j++) {

        double dec=0;
        ITSPInstance problemInstance = factory.instanceUniform(500, dist);
        Generation<ITSPIndividual> initial = new Generation<>(Stream.generate(problemInstance::generateRandomIndividual).limit(5*m).collect(Collectors.toList()));
        GeneticAlgorithm<ITSPIndividual> ga = new GeneticAlgorithm<>(
            initial,
            new ITSPCrossover(chance, dec),
            Arrays.asList(new MutationCombine(chance, dec), new MutationSplit(chance, dec), new MutationSwitch(chance, dec), new MutationChangePT(chance, dec)),
            new TournamentSelection<>(5),
            new FitnessSelection<>(), true
        );
        testSuite.add(ga);
      }

      for (int i = 0; i < 500; i++) {
        for (GeneticAlgorithm<ITSPIndividual> ga: testSuite
        ){
          ga.nextGeneration();

          if(i%50 == 0) {
            if (ga.getGeneration().getIndividuals().stream().map(Individual::getFitness).sorted().findFirst().isPresent()){
              System.out.print(ga.getGeneration().getIndividuals().stream().map(Individual::getFitness).sorted().findFirst().get());}
              else {
              System.out.println("NaN");
            }
            System.out.println(", ");
          }
        }
        if(i%50 == 0)
          System.out.println(';');
      }
    }

  }


  static Consumer<GeneticAlgorithm<ITSPIndividual>> csvExporter(GeneticAlgorithm<ITSPIndividual> geneticAlgorithm) {
    String suffix = Instant.now().toString();
    String path = "./out/output/log_" + suffix + ".csv";
    FileWriter csvWriter;
    try {
      csvWriter = new FileWriter(path);
      csvWriter.append("Generation,Mean Fitness,Optimal Fitness,Mean Variance,Mutationchance");
      csvWriter.append("Initial chance:,").append(String.valueOf(geneticAlgorithm.mutations.get(0).getMutationChance())).append(",Decay:,").append(String.valueOf(geneticAlgorithm.isDecay()))
          .append(",Size:,").append(Integer.toString(geneticAlgorithm.getGeneration().getSize()));

    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
      return generation -> {
      };
    }
    Consumer<GeneticAlgorithm<ITSPIndividual>> generationConsumer = new Consumer<GeneticAlgorithm<ITSPIndividual>>() {

      FileWriter csvWriter;

      @Override
      public void accept(GeneticAlgorithm<ITSPIndividual> geneticAlgorithm) {
        Generation<ITSPIndividual> generation=geneticAlgorithm.getGeneration();
        double mean = generation.getMean();
        double meanVar = generation.getMeanVar();
        double fittest = generation.getIndividuals().stream().mapToDouble(ITSPIndividual::calculateFitness).min().orElseThrow(() -> new RuntimeException("no minimum Fittest individual found"));
        double chance = geneticAlgorithm.mutations.get(0).getMutationChance();
        String[] line = {Integer.toString(generation.getGeneration()), Double.toString(mean), Double.toString(fittest), Double.toString(meanVar), "\n"};
        try {
          csvWriter.append(String.join(",", line));
          csvWriter.flush();
        } catch (IOException e) {
          System.err.println("unable to write to csv file");
        }
      }

      private Consumer<GeneticAlgorithm<ITSPIndividual>> setCsvWriter(FileWriter fileWriter) {
        this.csvWriter = fileWriter;
        return this;
      }
    }.setCsvWriter(csvWriter);
    System.out.println("Csv writer created at:");
    System.out.println(path);
    return generationConsumer;
  }


}
