package it.unipd.dei.dm1617;

/**
 * Created by adele on 17/05/17.
 */
// $example on$
import org.apache.spark.ml.feature.Word2Vec;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class JavaWord2Vec {
    public static void main(String[] args) {
        String inputPath = "lemma.txt";
        File file = new File(inputPath);

        ArrayList<Song> testi = new ArrayList<>();
        try {
            Scanner inputStream = new Scanner(file);
            int count = 0;
            while (inputStream.hasNext()) {
            //while (count < 10) {
                String temp1 = inputStream.nextLine();

                int index = Integer.parseInt(temp1.subSequence(temp1.indexOf("{\"index\":") + 9, temp1.indexOf(",\"genre\":")).toString());

                String testo = temp1.substring(temp1.indexOf(",\"text\":[") + 9, temp1.length() - 2);

                testi.add(new Song(index, testo));
                count++;
            }
            Word2Vector(testi);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void Word2Vector(ArrayList<Song> input) {
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWord2VecExample")
                .getOrCreate();

        // $example on$
        // Input data: Each row is a bag of words from a sentence or document.

        String temp1 = "";

        Long[] indici = new Long[input.size()];
        Row[] r1 = new Row[input.size()];
        for (int i = 0; i < input.size(); i++) {
            temp1 = input.get(i).getText();
            indici[i] = input.get(i).getIndex();
            r1[i] = RowFactory.create(Arrays.asList(temp1.split(", ")));

        }

        System.out.println("++++++Inizio Word2Vec++++++");

        List<Row> data = Arrays.asList(r1);

        StructType schema = new StructType(new StructField[]{
                new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
        });
        Dataset<Row> documentDF = spark.createDataFrame(data, schema);

        // Learn a mapping from words to Vectors.
        Word2Vec word2Vec = new Word2Vec()
                .setInputCol("text")
                .setOutputCol("result")
                .setVectorSize(300)
                .setMinCount(0);

        Word2VecModel model = word2Vec.fit(documentDF);
        Dataset<Row> result = model.transform(documentDF);
        System.out.println("------Fine Word2Vec------");
        System.out.println("------Inizio scrittura word2vec.txt------");

        try {
            FileWriter fileOut = new FileWriter("word2vec.txt");

            int count = 0;
            for (Row row : result.collectAsList()) {
                List<String> text = row.getList(0);
                org.apache.spark.ml.linalg.Vector vector = (org.apache.spark.ml.linalg.Vector) row.get(1);
                //System.out.println("Text: " + text + " => \nVector: " + vector + "\n");
                String stampa = "" + indici[count];
                int count2 = 1;
                String vettore = vector.toString();
                String[] s1 = vettore.substring(1, vettore.length() - 1).split(",");
                for (int i = 0; i < s1.length; i++) {
                    stampa += " " + count2 + ":" + s1[i];
                    count2++;
                }

                fileOut.write(stampa + "\n");
                count++;

            }
            // $example off$

            spark.stop();
            fileOut.flush();
            fileOut.close();
            System.out.println("------Fine scrittura------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}