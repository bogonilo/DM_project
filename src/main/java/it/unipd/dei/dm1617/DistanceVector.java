package it.unipd.dei.dm1617;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by adele on 17/05/17.
 */
// $example on$

public class DistanceVector{

    public static void main(String[] args) throws IOException {
        String path = "centri.txt";
        File file = new File(path);
        SparkConf sparkConf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("tif");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);


        int numPartitions = sc.defaultParallelism();
        System.err.println("Splitting data from " + path + " in " + numPartitions + " partitions");



        List<String> list = new ArrayList<>();
        String content;
        BufferedReader reader = new BufferedReader(new FileReader("centri.txt"));
        // readLine() and close() may throw errors, so they require you to catch it…
        try {
            while ((content = reader.readLine()) != null) {
                content=content.substring(1,content.length()-1);
                list.add(content);

            }
            reader.close();
        } catch (IOException e) {
            // This just prints the error log to the console if something goes wrong
            e.printStackTrace();
        }

        // Now proceed with your list, e.g. retrieve first item and split

        int i = 0;
        List<Vector<Double>> vettore = new ArrayList<>();
        Vector<Double> vv = new Vector<Double>();

        while (i<list.size()) {
            String[] parts = list.get(i). split(",");

        // You can simplify the for loop like this,
        // you call this for each:
            for (String s : parts) {
                double x = Double.parseDouble(s);
                vv.add(x);
            }
            vettore.add(vv);
            System.out.print(vettore.get(i)+"\n");

            vv.removeAllElements();
            i++;

        }




    }



    public static double [] distance(Double  n1, Double n2){
        double result[]=new double[2];
        for(int i=0;i<2;i++){
            double p=Math.sqrt(Math.pow((n1-n2),2));
            result[i]=p;
            }

            return result;
    }






}