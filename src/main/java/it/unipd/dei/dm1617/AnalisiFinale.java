package it.unipd.dei.dm1617;

import scala.Int;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by daniele on 24/05/17.
 */
public class AnalisiFinale {
    public static void main(String args[]) {

        String inputPath = "lemma.txt";
        String inputPath1 = "appartenenzaCentri.txt";

        File file = new File(inputPath);
        File file1 = new File(inputPath1);

        String delim = "text";

        //Song[] lista = new Song[numeroElementi()];
        Song[] lista = new Song[];
        int i = 0;

        try {
            //FileWriter fileOut = new FileWriter("analisi.txt");
            Scanner inputStream = new Scanner(file);
            Scanner inputStream1 = new Scanner(file1);

//Restituzione delle informazioni

            while (inputStream.hasNext()) {

                String temp1 = inputStream.nextLine();

                int index = Integer.parseInt(temp1.subSequence(temp1.indexOf("\"index\":") + 8, temp1.indexOf("\"genre\":") - 1).toString());

                String genere = temp1.subSequence(temp1.indexOf("\"genre\":") + 9, temp1.indexOf(delim) - 3).toString();

                int centro = Integer.parseInt(inputStream1.nextLine());

                lista[i] = new Song(index, centro, genere);
                i++;

            }
            //fileOut.flush();
            //fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int j = 0; j < lista.length; j++){
            System.out.println(lista[j].toStringCentri());
        }

    }

    public static int numeroElementi() {

        String inputPath = "canzoni.txt";
        File file = new File(inputPath);

        try {
            Scanner inputStream = new Scanner(file);
            return Integer.parseInt(inputStream.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
