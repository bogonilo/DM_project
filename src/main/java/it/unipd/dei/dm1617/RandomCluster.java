package it.unipd.dei.dm1617;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by adele on 30/05/17.
 */
public class RandomCluster {

    public static final void main(String... aArgs) throws IOException {
        SparkConf conf = new SparkConf()
                .setMaster("local[4]")
                .setAppName("RandomCluster");
        SparkContext sc = new SparkContext(conf);

        Random randomGenerator = new Random();

        FileWriter random=new FileWriter("random.txt");
        JavaRDD<String> rawData = sc.textFile("word2vecFormatCentri.txt",10).toJavaRDD();
        List<String> data=rawData.collect();
        List<List<String>> split=split(data, 10);
        for(List<String> s: split){
            int length=s.size();

            int randomInt = randomGenerator.nextInt(length-1);
            //generazione centri random
            random.write(String.valueOf(s.get(randomInt))+"\n");
            //  System.out.print(String.valueOf(s.get(randomInt)));
        }
        random.close();
    }



    public static List<List<String>> split(List<String> list, int size)
            throws NullPointerException, IllegalArgumentException {
        if (list == null) {
            throw new NullPointerException("The list parameter is null.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException(
                    "The size parameter must be more than 0.");
        }

        List<List<String>> result = new ArrayList<List<String>>(size);

        for (int i = 0; i < size; i++) {
            result.add(new ArrayList<String>());
        }

        int index = 0;

        for (String t : list) {
            result.get(index).add(t);
            index = (index + 1) % size;
        }

        return result;
    }

}