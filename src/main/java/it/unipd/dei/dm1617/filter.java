package it.unipd.dei.dm1617;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by adele on 31/05/17.
 */
public class filter {
    public static void main(String[]args) throws IOException {
        String path1="lemma.txt";
        String path2="word2vecFormatCentri.txt";
        String path3="word2vec.txt";
        //dolvesalvarevet--> in formato vettore (word2vec)
        //dovesalvareSong--> in formato dataset song


        //file senza canzoni di genere rock --> senzaRock.txt
      /* filtro(path1,path2,path3,"Rock","senzaRockvet.txt","senzaRockSong.txt","senzaRockw2v.txt");
        FileWriter file=new FileWriter("generi.txt");
        file.write("9");
        file.close();
        */


       //file senza canzoni di genere pop -->senzaPop.txt
      /*  filtro(path1,path2,path3,"Pop","senzaPopvet.txt","senzaPopSong.txt","senzaPopw2v.txt");
        FileWriter file=new FileWriter("generi.txt");
        file.write("9");
        file.close();
        */



       //file senza rock e pop --> senzaRockPop.txt

       /* filtro("senzaRockSong.txt","senzaRockvet.txt","senzaRockw2v.txt","Pop","senzaPopRockvet.txt","senzaPopRockSong.txt","senzaPopRockw2v.txt");
        FileWriter file=new FileWriter("generi.txt");
        file.write("8");
        file.close();
*/


       //file senza rock e folk-> senzaRockFolk
     /*   filtro("senzaRockSong.txt","senzaRockvet.txt","senzaRockw2v.txt","Folk","senzaRockFolkvet.txt","senzaRockFolkSong.txt","senzaRockFolkw2v.txt");
        FileWriter file=new FileWriter("generi.txt");
        file.write("8");
        file.close();
       */


        //file senza rock,pop,folk -> senzaRockFolkPop
        filtro("senzaPopRockSong.txt","senzaPopRockvet.txt","senzaPopRockw2v.txt","Folk","senzaRockPopFolkvet.txt","senzaRockPopFolkSong.txt","senzaRockPopFolkw2v.txt");
        FileWriter file=new FileWriter("generi.txt");
        file.write("7");
        file.close();

    }

    public static void filtro(String path1,String path2, String path3, String Escludigenere,String doveSalvareVet,String doveSalvareSong,String dovesalvarew2vRowFormat) throws IOException {
        FileWriter filtrati1=new FileWriter(doveSalvareVet);
        FileWriter filtrati2=new FileWriter(doveSalvareSong);
        FileWriter filtrati3=new FileWriter(dovesalvarew2vRowFormat);
        FileWriter numcanzoni=new FileWriter("canzoni.txt");
        File file1 = new File(path1);
        File file2=new File(path2);
        File file3=new File(path3);
        int count=0;

        try{
            Scanner inputStream1 = new Scanner(file1);
            Scanner inputStream2 = new Scanner(file2);
            Scanner inputStream3= new Scanner(file3);
            while (inputStream1.hasNext()&& inputStream2.hasNext()) {

                String temp1 = inputStream1.nextLine();
                String temp2= inputStream2.nextLine();
                String temp3=inputStream3.nextLine();
                String genere = temp1.substring(temp1.indexOf("\"genre\":") + 8, temp1.indexOf(",\"text\":"));
        //        int index = Integer.parseInt(temp1.subSequence(temp1.indexOf("\"index\":") + 8, temp1.indexOf("\"genre\":") - 1).toString());
        //        String testo = temp1.substring(temp1.indexOf("\"text\":[") + 7, temp1.length() - 2);
                if (!genere.equalsIgnoreCase(Escludigenere)) {
                    filtrati1.write(temp2+"\n");
                    filtrati2.write(temp1+"\n");
                    filtrati3.write(temp3+"\n");
                    count++;
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        numcanzoni.write(""+ count);
        numcanzoni.close();
        filtrati1.close();

        filtrati2.close();
        filtrati3.close();

    }
}

