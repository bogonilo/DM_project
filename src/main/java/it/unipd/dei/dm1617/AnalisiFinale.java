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

        int numeroCanzoni = numeroElementi("canzoni.txt");
        Song[] lista = new Song[numeroCanzoni];

        ArrayList<String> listaGeneri = new ArrayList<>();

        int i = 0;

        try {
            FileWriter fileOut = new FileWriter("analisi.txt");
            Scanner inputStream = new Scanner(file);
            Scanner inputStream1 = new Scanner(file1);

            while (inputStream1.hasNext() && inputStream.hasNext()) {

                String temp1 = inputStream.nextLine();

                int index = Integer.parseInt(temp1.subSequence(temp1.indexOf("\"index\":") + 8, temp1.indexOf("\"genre\":") - 1).toString());

                String genere = temp1.subSequence(temp1.indexOf("\"genre\":") + 8, temp1.indexOf(",\"text\":")).toString();
                if(listaGeneri.indexOf(genere) == -1)
                    listaGeneri.add(genere);

                //System.out.println(genere);

                int centro = Integer.parseInt(inputStream1.nextLine());

                lista[i] = new Song(index, centro, genere);
                i++;
            }
            FileWriter fileOutfinale = new FileWriter("analisiFinale.txt");
            int numeroGeneri = numeroElementi("generi.txt");
            int[] contatore2 = new int[numeroGeneri];
            int contCanzoni2 = 0;
            //System.out.println("bene1");
            for (int j1 = 0; j1 < lista.length-1; j1++){
                //System.out.println(listaGeneri.indexOf(lista[j1].getGenre()));
                    contatore2[listaGeneri.indexOf(lista[j1].getGenre())]++;
            }
            fileOutfinale.write("Contatore dei generi delle canzoni: \n");
            for (int j1 = 0; j1 < numeroGeneri; j1++){
                fileOutfinale.write(listaGeneri.get(j1) + " : " + contatore2[j1] + "\n");
            }
            //System.out.println("bene2");
            int[] contatore;
            int numeroCentro = 0;
            int contCanzoni = 0;

            while(numeroCentro < numeroGeneri*1000){
                contatore = new int[numeroGeneri];
                contCanzoni = 0;
                for (int j1 = 0; j1 < lista.length-1; j1++){
                    //System.out.println(listaGeneri.indexOf(lista[j1].getGenre()));

                    if(lista[j1].getCentro() == numeroCentro) {
                        contatore[listaGeneri.indexOf(lista[j1].getGenre())]++;
                        contCanzoni++;
                    }
                }
                //System.out.println("bene3");
                fileOutfinale.write("******************* Nel centro: -"+ numeroCentro + "- ci sono: \n");
                fileOutfinale.write("Num canzoni: " + contCanzoni + "\n");
                for(int i1 = 0; i1 < contatore.length; i1++){
                    double d = Math.round(((double)contatore[i1]*100)/contCanzoni);
                    if(contatore[i1] != 0)
                    fileOutfinale.write(listaGeneri.get(i1) + " : " + contatore[i1] + " su: " + contatore2[i1] + "\n");

                }
                fileOutfinale.write("\n ");
                numeroCentro++;
            }
            fileOutfinale.flush();
            fileOutfinale.close();
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("HAI FINITOOOOO!! BRAVVVVVVOOOOOOOOOO!!!!");

    }

    public static int numeroElementi(String path) {

        String inputPath = path;
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
