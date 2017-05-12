package it.unipd.dei.dm1617.examples;

import it.unipd.dei.dm1617.InputOutput;
import it.unipd.dei.dm1617.WikiPage;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import src.main.java.it.unipd.dei.dm1617.Song;

/**
 * Extract a sample of pages from the given dataset, writing it in the
 * given datase.
 */
public class Sample {

  public static void main(String[] args) {
    String inputPath = "analizzato.csv";
    String outputPath = "analizz-lemma.csv";
    double fraction = Double.parseDouble(String.valueOf(0.1));

    // The usual Spark setup
    SparkConf conf = new SparkConf(true).setAppName("Sampler");
    JavaSparkContext sc = new JavaSparkContext(conf);

    // Read the pages from the path provided as the first argument.
    JavaRDD<Song> pages = InputOutput.read(sc, inputPath);



    // Sample, without replacement, the desired fraction of pages.
    JavaRDD<Song> sample = pages.sample(false, fraction);

    // Write the sampled pages to the given output path.
    InputOutput.write(sample, outputPath);
  }

}
