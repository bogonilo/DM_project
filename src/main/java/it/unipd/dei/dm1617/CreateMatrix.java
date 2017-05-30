package it.unipd.dei.dm1617;

/**
 * Created by adele on 23/05/17.
 */

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import java.io.*;
import java.util.List;
import java.util.Scanner;


/**
 * Creates a 2D matrix
 */
public class CreateMatrix {
    public static void main(String args[]) throws IOException {
       /* double[][] matrix = new double[10000][10000];
        int x = 0, y = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader("word2vecFormatCentri.txt"));    //reading files in specified directory
            String line;
            while ((line = in.readLine()) != null)    //file reading
            {
                String[] values = line.split(",");
                for (String str : values) {
                    double str_double = Double.parseDouble(str);
                    matrix[x][y] = str_double;
                   //  System.out.print(matrix[x][y] + " ");
                }
                y++;
            }
            // System.out.println("");
            x++;
            in.close();
        } catch (IOException ioException) {
        }
        for(int i=0;i<matrix.length;i++) {
            System.out.print((listaV.get(i)).toString());
        }*/

        SparkConf conf = new SparkConf()
                .setMaster("local[4]")
                .setAppName("word2vecFormatCentri");
        SparkContext sc = new SparkContext(conf);

        JavaRDD<String> data = sc.textFile("word2vecFormatCentri.txt", 0).toJavaRDD();
        JavaRDD<double[]> whatYouWantRdd = data.map(new Function<String, double[]>() {
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
        List<double[]> whatYouWant = whatYouWantRdd.collect();

        FileWriter center = new FileWriter("c.txt");
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
        DeleteLastWhiteLine("c.txt");



        JavaRDD<String> c = sc.textFile("c.txt", 0).toJavaRDD();

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
        //  System.out.print("centro0"+ Arrays.toString(centerD.get(0)));
        //System.out.print(Arrays.toString(whatYouWant.get(0)));
        FileWriter appartenenza = new FileWriter("appartenenzaCentri.txt");

        int index = 0;
        for (int i = 0; i < whatYouWant.size(); i++) {
            double temp = -1;
            double var;

            for (int j = 0; j < centerD.size(); j++) {

                var = cosineSimilarity(whatYouWant.get(i), centerD.get(j));
                if (temp < var) {
                    temp = var;
                    index = j;
                }

            }
            //  System.out.print("canzone num: "+i+" centro "+index+" "+temp+"\n");
            appartenenza.write(index + "\n");


        }
        appartenenza.flush();
        appartenenza.close();
        DeleteLastWhiteLine("appartenenzaCentri.txt");


        FileWriter appartEuclidea = new FileWriter("appartEuclidea.txt");
        int indexEuclidian = 0;
        for (int i = 0; i < whatYouWant.size(); i++) {
            double var2;
            double temp = 0;
            for (int j = 0; j < centerD.size(); j++) {

                var2 = distance(whatYouWant.get(i), centerD.get(j));
                if (j == 0) {
                    temp = var2;
                }
                if (temp > var2) {
                    temp = var2;
                    indexEuclidian = j;

                }
            }
            System.out.print(indexEuclidian + "\n");
            appartEuclidea.write(indexEuclidian + "\n");

            // System.out.print("canzone num: "+i+" centro "+indexEuclidian+" "+temp+"\n");

        }
        appartEuclidea.flush();
        appartEuclidea.close();
        DeleteLastWhiteLine("appartEuclidea.txt");
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

  /*  public static List mode(double[][] arr) {
        List<double[]> list = new ArrayList<>();
        List<Double> newL = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            // tiny change 1: proper dimensions
            double [] vector=new double[arr.length];
            for (int j = 0; j < arr[i].length; j++) {
                // tiny change 2: actually store the values
                newL.add(arr[i][j]);
            }
            vector[i] = newL.get(i);
            list.add(vector);
            vector=null;
        }return list;
    }*/
}

