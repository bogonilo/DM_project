package it.unipd.dei.dm1617;

/**
 * Created by adele on 23/05/17.
 */

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;


/**
 * Creates a 2D matrix
 */
public class DistanceEstimation {
    public static void main(String args[]) throws IOException {
         SparkConf conf = new SparkConf()
                .setMaster("local[4]")
                .setAppName("DistanceEstimation");
        SparkContext sc = new SparkContext(conf);

        JavaRDD<String> data = sc.textFile("word2vecFormatCentri.txt", 0).toJavaRDD();
        //inserimento dei vettori delle canzoni in una struttura di tipo List<double[]>
        JavaRDD<double[]> VettoricanzoniRDD = data.map(new Function<String, double[]>() {
            @Override
            public double[] call(String row) throws Exception {
                return splitStringtoDoubles(row);
            }

            private double[] splitStringtoDoubles(String s) {
                String[] splitVals = s.split(",");
                double[] vals = new double[splitVals.length];
                for (int i = 0; i < splitVals.length; i++) {
                    vals[i] = Double.parseDouble(splitVals[i]);
                }
                return vals;
            }

        });
        List<double[]> Vettoricanzoni = VettoricanzoniRDD.collect();


        //estrazione dei centri dal file
        DeleteLastWhiteLine("centriFormat.txt");


        //inserimento dei vettori dei centri in una struttura di tipo List<double[]>
        JavaRDD<String> c = sc.textFile("centriFormat.txt", 0).toJavaRDD();

        JavaRDD<double[]> centerRDD = c.map(new Function<String, double[]>() {
            @Override
            public double[] call(String row) throws Exception {
                return splitStringtoDoubles(row);
            }

            private double[] splitStringtoDoubles(String s) {
                String[] splitVals = s.split(",");
                double[] vals = new double[splitVals.length];
                for (int i = 0; i < splitVals.length; i++) {

                    vals[i] = Double.parseDouble(splitVals[i]);
                    //  System.out.println(vals[i]);
                }
                return vals;
            }

        });
        List<double[]> centerD = centerRDD.collect();



        //usando la distanza euclidea, viene determinato il cluster di appartenenza di ogni canzone
        //ad ogni canzone viene assegnato il centro con distanza euclidea minina da esso
        FileWriter appartEuclidea = new FileWriter("appartEuclidea.txt");
        int indexEuclidian = 0;
        for (int i = 0; i < Vettoricanzoni.size(); i++) {
            double var2;
            double temp = 0;
            for (int j = 0; j < centerD.size(); j++) {

                var2 = distance(Vettoricanzoni.get(i), centerD.get(j));
                if (j == 0) {
                        temp = var2;
                }
               if (temp >= var2 ) {
                    temp = var2;
                    indexEuclidian = j;

                }
            }
            //System.out.print(indexEuclidian + "\n");
            appartEuclidea.write(indexEuclidian + "\n");

            // System.out.print("canzone num: "+i+" centro "+indexEuclidian+" "+temp+"\n");

        }
        appartEuclidea.flush();
        appartEuclidea.close();
        DeleteLastWhiteLine("appartEuclidea.txt");



        //inserimento dei vettori random dei centri in una struttura di tipo List<double[]>
        DeleteLastWhiteLine("random.txt");
        JavaRDD<String> rand = sc.textFile("random.txt", 0).toJavaRDD();

        JavaRDD<double[]> centerRandom = rand.map(new Function<String, double[]>() {
            @Override
            public double[] call(String row) throws Exception {
                return splitStringtoDoubles(row);
            }

            private double[] splitStringtoDoubles(String s) {
                String[] splitVals = s.split(",");
                double[] vals = new double[splitVals.length];
                for (int i = 0; i < splitVals.length; i++) {

                    vals[i] = Double.parseDouble(splitVals[i]);
                    //  System.out.println(vals[i]);
                }
                return vals;
            }

        });
        List<double[]> centerR = centerRandom.collect();


        //usando la distanza euclidea, viene determinato il cluster di appartenenza di ogni canzone
        //ad ogni canzone viene assegnato il centro con distanza euclidea minina da esso
        FileWriter appartenenzaR= new FileWriter("appartenenzaRandom.txt");
        int indexR = 0;
        for (int i = 0; i < Vettoricanzoni.size(); i++) {
            double var2;
            double temp = 0;
            for (int j = 0; j < centerR.size(); j++) {

                var2 = distance(Vettoricanzoni.get(i), centerR.get(j));
                if (j == 0) {
                    temp = var2;
                }
                if (temp >= var2 ) {
                    temp = var2;
                    indexR = j;

                }
            }
            System.out.print(indexR + "\n");
            appartenenzaR.write(indexR + "\n");

            // System.out.print("canzone num: "+i+" centro "+indexEuclidian+" "+temp+"\n");

        }
        appartenenzaR.flush();
        appartenenzaR.close();
        DeleteLastWhiteLine("appartenenzaRandom.txt");
    }



    public static double distance(double[] a, double[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);}


    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }


    public static void DeleteLastWhiteLine(String x) throws FileNotFoundException {
        RandomAccessFile raf= new RandomAccessFile(x, "rw");;
        try{
            long length = raf.length();
            //System.out.println("File Length="+raf.length());
            //supposing that last line is of 8
            raf.setLength(length - 1);
            //  System.out.println("File Length="+raf.length());
            raf.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


}

