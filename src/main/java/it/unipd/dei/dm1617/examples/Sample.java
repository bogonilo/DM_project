package it.unipd.dei.dm1617.examples;

import it.unipd.dei.dm1617.InputOutput;
import it.unipd.dei.dm1617.WikiPage;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Extract a sample of pages from the given dataset, writing it in the
 * given datase.
 */
public class Sample {

  public static void main(String[] args) {
    String inputPath = args[0];
    String outputPath = args[1];
    double fraction = Double.parseDouble(args[2]);

    // The usual Spark setup
    SparkConf conf = new SparkConf(true).setAppName("Sampler");
    JavaSparkContext sc = new JavaSparkContext(conf);



    JavaRDD<String> pages = sc.textFile(inputPath);

    // JavaRDD<WikiPage> pages1 = InputOutput.read(sc, inputPath);
    // Read the pages from the path provided as the first argument.

    // Sample, without replacement, the desired fraction of pages.
    JavaRDD<String> sample = pages.sample(false, fraction);

    // Write the sampled pages to the given output path.
    InputOutput.write(pages, outputPath);

  }

}
