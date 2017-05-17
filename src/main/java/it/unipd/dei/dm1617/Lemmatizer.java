package it.unipd.dei.dm1617;


import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;

import it.unipd.dei.dm1617.Song;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

/**
 * Collection of functions that allow to transform texts to sequence
 * of lemmas using lemmatization. An alternative process is
 * stemming. For a discussion of the difference between stemming and
 * lemmatization see this link: https://nlp.stanford.edu/IR-book/html/htmledition/stemming-and-lemmatization-1.html
 */
public class Lemmatizer {

  /**
   * Some symbols are interpreted as tokens. This regex allows us to exclude them.
   */
  public static Pattern symbols = Pattern.compile("^[',\\.`/-_]+$");

  /**
   * A set of special tokens that are present in the Wikipedia dataset
   */
  public static HashSet<String> specialTokens =
    new HashSet<>(Arrays.asList(","));

  /**
   * Transform a single document in the sequence of its lemmas.
   */
  public static ArrayList<String> lemmatize(String doc) {
    Document d = new Document(doc.toLowerCase());
    // Count spaces to allocate the vector to the right size and avoid trashing memory
    int numSpaces = 0;
    for (int i = 0; i < doc.length(); i++) {
      if (doc.charAt(i) == ' ') {
        numSpaces++;
      }
    }
    ArrayList<String> lemmas = new ArrayList<>(numSpaces);

    for (Sentence sentence : d.sentences()) {
      for (String lemma : sentence.lemmas()) {
        // Remove symbols
        if (!symbols.matcher(lemma).matches() && !specialTokens.contains(lemma)) {
          lemmas.add(lemma);
        }
      }
    }

    return lemmas;
  }

  /**
   * Transform an RDD of strings in the corresponding RDD of lemma
   * sequences, with one sequence for each original document.
   */
  public static JavaRDD<ArrayList<String>> lemmatize(JavaRDD<String> docs) {
    return docs.map((d) -> lemmatize(d));
  }

  public static void main(String[] args) throws IOException {

    String inputPath = "analizzato.csv";

    File file = new File(inputPath);

    String delim = "text", testo = "";

    List<Song> listGeneri = new ArrayList<Song>();

    FileWriter fileOut = new FileWriter("lemma.txt");

    try {

      Scanner inputStream = new Scanner(file);

      System.out.println("++++++Inizio lemmatizzazione++++++");

      while (inputStream.hasNext()) {

        String temp1 = inputStream.nextLine();

        int index = Integer.parseInt(temp1.subSequence(temp1.indexOf("\"index\":") + 8, temp1.indexOf("\"genre\":") - 1).toString());

        String genere = temp1.subSequence(temp1.indexOf("\"genre\":") + 9, temp1.indexOf(delim) - 3).toString();

        testo = temp1.substring(temp1.indexOf(delim) + 7, temp1.length() - 2);

        ArrayList<String> temp2 = lemmatize(testo);

        listGeneri.add(new Song (index, genere, controlla(temp2).toString()));

      }

      for(int i = 0; i < listGeneri.size(); i++){
        String stampare = listGeneri.get(i).toString();
        fileOut.write((stampare.substring(1, stampare.length())));
      }

      fileOut.flush();
      fileOut.close();
      System.out.println("------Fine lemmatizzazione------");
      System.out.println("++++++Inizio Raggruppamento elementi per genere++++++");

      System.out.println("------Fine raggruppamento------");

      Map<String, List<Song>> generiGrouped = listGeneri.stream().collect(Collectors.groupingBy(w -> w.getGenres()));

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (StringIndexOutOfBoundsException e){
      e.printStackTrace();
    }

  }

  public static ArrayList<String> controlla(ArrayList<String> input) {
    ArrayList<String> output = input;
    for (int i = 0; i < input.size(); i++) {
      if (input.get(i).indexOf('-') != -1) {
       //System.out.println(input.get(i));
        input.remove(i);
      }

    }
    //System.out.println(input.get(input.size()-1));
    return input;
  }
}
