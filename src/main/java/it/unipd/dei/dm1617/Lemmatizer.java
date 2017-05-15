package it.unipd.dei.dm1617;


import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import org.apache.spark.api.java.JavaRDD;
import src.main.java.it.unipd.dei.dm1617.Generi;
import src.main.java.it.unipd.dei.dm1617.Song;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    FileWriter fileOut = new FileWriter("lemmatizzato.csv");

    String delim = "text", testo = "";

    //ArrayList<String> lemmatizzato = new ArrayList<String>();


    //int count = 0;

    //String[] listaGeneri = creaGeneri(file, delim);

    //ArrayList<Generi> generiArray = new ArrayList<Generi>();


    //for(int i = 0; i < listaGeneri.length; i++)
    //    generiArray.add(new Generi((String) listaGeneri[i]));

    //  System.out.println(generiArray.toString());
    List<Song> listGeneri = new ArrayList<Song>();

    try {
      // -read from filePooped with Scanner class
      Scanner inputStream = new Scanner(file);

      System.out.println("++++++Inizio lemmatizzazione++++++");

      while (inputStream.hasNext()) {

        String temp1 = inputStream.nextLine();

        int index = Integer.parseInt(temp1.subSequence(temp1.indexOf("\"index\":") + 8, temp1.indexOf("\"genre\":") - 1).toString());

        String genere = (String) temp1.subSequence(temp1.indexOf("\"genre\":") + 9, temp1.indexOf(delim) - 3);

        testo = temp1.substring(temp1.indexOf(delim) + 6);

        listGeneri.add(new Song (index, genere, lemmatize(testo).toString()));

      }

      System.out.println("------Fine lemmatizzazione------");
      System.out.println("++++++Inizio Raggruppamento elementi per genere++++++");

      Map<String, List<Song>> generiGrouped = listGeneri.stream().collect(Collectors.groupingBy(w -> w.getGenres()));

      System.out.println("------Fine raggruppamento------");

      //generiGrouped.forEach((genre, textLemma) -> System.out.format("Genere %s \n", textLemma));

      //JavaRDD<String> songRDD = listGeneri.stream().collect(Collectors.groupingBy(p -> p.gen));

      //lemmatize(testi);
      //ArrayList<String> tempLemma = lemmatize(temp);
      //fileOut.write(tempLemma.toString());

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (StringIndexOutOfBoundsException e){
      e.printStackTrace();
    }


  }

  public static String[] creaGeneri (File file, String delim){

    int count = 0, countGenere = 0;
    String[] finale = new String[0];

      List<String> listaGeneri = new ArrayList<String>();

    try {
        // -read from filePooped with Scanner class
        Scanner inputStream = new Scanner(file);

        while (inputStream.hasNext()) {
            countGenere = 0;

            String temp1 = inputStream.nextLine();

            String genere = (String) temp1.subSequence(temp1.indexOf("\"genre\":") + 9, temp1.indexOf(delim) - 3);


            for (int i = 0; i < listaGeneri.size(); i++) {

                if (!(genere.equalsIgnoreCase(listaGeneri.get(i))))
                    countGenere++;

            }

            if (countGenere == listaGeneri.size())
                listaGeneri.add(genere);

        }

        finale = listaGeneri.toArray(new String[listaGeneri.size()]);

    }catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return finale;
  }

  public static int getPosizione (String stringaDaCercare, ArrayList<Generi> lista){
    int posizione = -1;

    for (int i=0; i<lista.size();i++){
      if (lista.get(i).getGenere().equalsIgnoreCase(stringaDaCercare)){
        posizione = i;
      }
    }

    return posizione;
  }

}
