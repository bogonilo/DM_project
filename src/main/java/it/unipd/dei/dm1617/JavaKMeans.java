package it.unipd.dei.dm1617;

import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
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
 * Created by Song-Group on 13/05/17.
 */

public class JavaKMeans {

    public static void main(String[] args) throws IOException {
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
        KMeans kmeans;
        KMeansModel model = null;

//        int num= Integer.parseInt(model.k().toString());
  //      System.out.print(num);

        try {
            kmeans = new KMeans().setK(numK).setMaxIter(20);
            model = kmeans.fit(dataset);
        }catch (Exception e){
            e.printStackTrace();
        }

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


        FileWriter center = new FileWriter("centriFormat.txt");
        String inputPath = "centri.txt";
        File file = new File(inputPath);
        try {

            Scanner inputStream = new Scanner(file);
            while (inputStream.hasNext()) {
                String temp1 = inputStream.nextLine();
                String centri = temp1.substring(temp1.indexOf("[") + 1, temp1.length() - 1);
                center.write(centri + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        center.close();
    }
}