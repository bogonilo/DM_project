package it.unipd.dei.dm1617;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;
import org.apache.spark.mllib.linalg.BLAS;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Word2VecJRDD {

    //settare la dimensione dei vettori del modello
    private static int numDim=10;

    public static void main(String[] args) throws IOException {

        SparkConf sparkConf = new SparkConf()
                .setMaster("local[4]")
                .setAppName("tif");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);



        FileWriter lemmaText= new FileWriter("lemmaText.txt");
        String inputPath = "lemma.txt";
        File file = new File(inputPath);
        //estrazione del testo lemmatizzato di ogni singola canzone
        try{

            Scanner inputStream = new Scanner(file);
            while (inputStream.hasNext()) {
                String temp1 = inputStream.nextLine();
                String text = temp1.substring(temp1.indexOf(",\"text\":[") + 9, temp1.length() - 2);
                lemmaText.write(text+"\n");
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        lemmaText.close();



        String input = "lemmaText.txt";
        //faccio un RDD che ha come collezione le stringhe che sono le righe del testo
        JavaRDD<String> text=sc.textFile(input,10);

        //Ogni riga del testo diventa una lista di stringhe
        JavaRDD<List<String>> dWords =  text
                .map((doc) -> {
                    List<String> words = new ArrayList<String>();
                    BreakIterator breakIterator = BreakIterator.getWordInstance();
                    breakIterator.setText(doc);
                    int lastIndex = breakIterator.first();
                    while (BreakIterator.DONE != lastIndex) {
                        int firstIndex = lastIndex;
                        lastIndex = breakIterator.next();
                        if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(doc.charAt(firstIndex))) {
                            words.add(doc.substring(firstIndex, lastIndex));
                        }
                    }
                    return words;
                });

        System.out.print("---start wordtovec-----");
        //Alleno il modello con il Java RDD di Liste di stringhe
        Word2Vec word2Vec = new Word2Vec()
                .setVectorSize(100)
                .setMinCount(0);

    String path="result.txt";
    //FileWriter newf=new FileWriter("wordResult.txt");

        Word2VecModel model = word2Vec.fit(dWords);
        model.save(sc.sc(),path);

        //faccio diventare il JRDD di Liste di stringhe un JRDD di vettori
        // che rappresentano la media dei vettori delle singole parole della lista

        JavaRDD<Vector> vettori =  dWords
                .map((song) -> {
                            return average(song,model);
                            });

        FileWriter res2= new FileWriter("resvettori.txt");
        //Testo se vengono vettori unici
        List<Vector> vet = vettori.collect();
        int count=0;
       for (int i=0; i<vet.size(); i++) {
           res2.write(String.valueOf(vet.get(i)) + "\n");
           //System.out.println(vet.get(i));

       }
        res2.close();

        sc.stop();
    }


    //faccio funzione media valori di una serie di parole
    //@input lista di stringhe che rappresenta in questo caso la canzone
    //@output Vettore che rappresenta la lista mediata per una singola canzone

    public static Vector average(List<String> song, Word2VecModel modello){

        //inizializzo un vettore di zeri
      Vector somma = Vectors.zeros(100);

      //faccio la sommatoria di tutti i vettori
      for (int i=0; i<song.size(); i++)
          BLAS.axpy(1, modello.transform(song.get(i)), somma);

     //divido per la grandezza delle parole della canzone
      BLAS.scal((double)1/song.size(),somma);

      return somma;
    }
}