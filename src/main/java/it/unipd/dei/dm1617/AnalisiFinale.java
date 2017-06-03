package it.unipd.dei.dm1617;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static it.unipd.dei.dm1617.DistanceEstimation.DeleteLastWhiteLine;

/**
 * Created by Song-Group on 10/05/17.
 */

public class AnalisiFinale {
    public static void main(String args[]) throws IOException {
        String inputPath = "lemma.txt";
        // String inputPath1 = "appartenenzaCentri.txt";
        String inputPath1="appartEuclidea.txt";
        String inputPath2="appartenenzaRandom.txt";

        FileWriter fileOutfinale = new FileWriter("analisiFinale.txt");
        FileWriter fileOut = new FileWriter("GenereCentroEntropia.txt");
        //generazione file finali usando il nostro cluster
        analisi(inputPath,inputPath1,fileOut,fileOutfinale);


        FileWriter fileOutfinale2 = new FileWriter("analisiFinaleRandom.txt");
        FileWriter fileOut2 = new FileWriter("GenereCentroEntropiaRandom.txt");
        //generazione file finali usando il cluster random
        analisi(inputPath,inputPath2,fileOut2,fileOutfinale2);
        DeleteLastWhiteLine("GenereCentroEntropia.txt");
        DeleteLastWhiteLine("GenereCentroEntropiaRandom.txt");

    }

    public static int numeroElementi(String path) {

        String inputPath = path;
        File file = new File(inputPath);
        int i=0;
        try {
            Scanner inputStream = new Scanner(file);
            return Integer.parseInt(inputStream.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void analisi(String inputPath,String inputPath1,FileWriter fileOut, FileWriter fileOutfinale) {
        File file = new File(inputPath);
        File file1 = new File(inputPath1);


        String delim = "text";

        int numeroCanzoni = numeroElementi("canzoni.txt") - 3;
        Song[] lista = new Song[numeroCanzoni];

        ArrayList<String> listaGeneri = new ArrayList<>();

        int i = 0;

        try {
            Scanner inputStream = new Scanner(file);
            Scanner inputStream1 = new Scanner(file1);

            while (inputStream1.hasNext() && inputStream.hasNext()) {

                String temp1 = inputStream.nextLine();

                int index = Integer.parseInt(temp1.subSequence(temp1.indexOf("\"index\":") + 8, temp1.indexOf("\"genre\":") - 1).toString());

                String genere = temp1.subSequence(temp1.indexOf("\"genre\":") + 8, temp1.indexOf(",\"text\":")).toString();
                if(listaGeneri.indexOf(genere) == -1)
                    listaGeneri.add(genere);



                int centro = Integer.parseInt(inputStream1.nextLine());
                fileOut.write(genere + " "+ centro+"\n");
                lista[i] = new Song(index, centro, genere);
                i++;
            }
            fileOut.flush();
            fileOut.close();



            int numeroGeneri = numeroElementi("generi.txt");
            int[] contatore2 = new int[numeroGeneri];

            for (int j1 = 0; j1 < lista.length-1; j1++){
                contatore2[listaGeneri.indexOf(lista[j1].getGenre())]++;
            }
            fileOutfinale.write("Contatore dei generi delle canzoni: \n");
            for (int j1 = 0; j1 < numeroGeneri; j1++){
                fileOutfinale.write(listaGeneri.get(j1) + " : " + contatore2[j1] + "\n");
            }

            int[] contatore;
            int numeroCentro = 0;
            int contCanzoni = 0;

            while(numeroCentro < numeroGeneri){
                contatore = new int[numeroGeneri];
                contCanzoni = 0;
                for (int j1 = 0; j1 < lista.length-1; j1++){

                    if(lista[j1].getCentro() == numeroCentro) {
                        contatore[listaGeneri.indexOf(lista[j1].getGenre())]++;
                        contCanzoni++;
                    }
                }

                fileOutfinale.write("******************* Nel centro: -"+ numeroCentro + "- ci sono: \n");
                fileOutfinale.write("Num canzoni: " + contCanzoni + "\n");
                for(int i1 = 0; i1 < contatore.length; i1++){
                        fileOutfinale.write(listaGeneri.get(i1) + " : " + contatore[i1] + " su: " + contatore2[i1] + "\n");

                }
                fileOutfinale.write("\n ");
                numeroCentro++;
            }
            fileOutfinale.flush();
            fileOutfinale.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("--------------ANALISI FINALE COMPLETATA-----------------");

    }
}