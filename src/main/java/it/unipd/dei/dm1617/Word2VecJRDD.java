package it.unipd.dei.dm1617;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;

import java.util.ArrayList;
import java.util.List;
import java.text.BreakIterator;
import java.lang.*;

import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.BLAS;
import org.apache.spark.mllib.linalg.Vector;



public class Word2VecJRDD {

    //settare la dimensione dei vettori del modello
    private static int numDim=10;

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf()
                .setMaster("local[4]")
                .setAppName("tif");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        String inputPath = "wiki-sample.txt";

        //faccio un RDD che ha come collezione le stringhe che sono le righe del testo
        JavaRDD<String> text=sc.textFile(inputPath,10);

        /* Test
        List<String> a = text.collect();
        for (int i=0; i<10; i++){
            System.out.println(a.get(i));
        }*/

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



        //Alleno il modello con il Java RDD di Liste di stringhe
        Word2Vec word2Vec = new Word2Vec()
                .setVectorSize(numDim)
                .setMinCount(0);
        Word2VecModel model = word2Vec.fit(dWords);

        //testo il modello
       System.out.println( model.transform("road"));


        //faccio diventare il JRDD di Liste di stringhe un JRDD di vettori
        // che rappresentano la media dei vettori delle singole parole della lista
        JavaRDD<Vector> vettori =  dWords
                .map((song) -> {
                            return average(song,model);
                            });

        //Testo se vengono vettori unici
        List<Vector> vet = vettori.collect();
       for (int i=0; i<10; i++){
           System.out.println(vet.get(i));
       }


        sc.stop();
    }


    //faccio funzione media valori di una serie di parole
    //@input lista di stringhe che rappresenta in questo caso la canzone
    //@output Vettore che rappresenta la lista mediata per una singola canzone

    public static Vector average(List<String> song, Word2VecModel modello){

        //inizializzo un vettore di zeri
      Vector somma = Vectors.zeros(numDim);

      //faccio la sommatoria di tutti i vettori
      for (int i=0; i<song.size(); i++)
          BLAS.axpy(1, modello.transform(song.get(i)), somma);

     //divido per la grandezza delle parole della canzone
      BLAS.scal((double)1/song.size(),somma);

      return somma;
    }
}