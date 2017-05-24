package it.unipd.dei.dm1617;

import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/**
 * An example demonstrating k-means clustering.
 * Run with
 * <pre>
 * bin/run-example ml.JavaKMeansExample
 * </pre>
 */
public class JavaKMeans {

    public static void main(String[] args){
        // Create a SparkSession.
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaKMeansExample")
                .getOrCreate();

        // $example on$
        // Loads data.
        int numK = 0;
        try{
            String inputPath = "generi.txt";
            File file = new File(inputPath);
            Scanner inputStream = new Scanner(file);

            numK = Integer.parseInt(inputStream.next());
            //System.out.println(numK);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        Dataset<Row> dataset = spark.read().format("libsvm").load("word2vec.txt");

        // Trains a k-means model.
        //Set the random seed for cluster initialization.
        KMeans kmeans = new KMeans().setK(numK).setSeed(1L);
        KMeansModel model = kmeans.fit(dataset);

        // Evaluate clustering by computing Within Set Sum of Squared Errors.
        double WSSSE = model.computeCost(dataset);
        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

        try {

            FileWriter fileOut = new FileWriter("centri.txt");
            // Shows the result.
            Vector[] centers = model.clusterCenters();
            System.out.println("Cluster Centers: ");
            for (Vector center : centers) {
                fileOut.write(center.toString() + "\n");
            }
            // $example off$
            fileOut.flush();
            fileOut.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        spark.stop();
    }
}