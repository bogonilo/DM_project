package it.unipd.dei.dm1617;

import org.apache.spark.sql.Row;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File.*;
import java.util.Scanner;

/**
 * Created by daniele on 25/05/17.
 */
public class TrasformaVettori {
    public static void main(String args []){

        try{
            String inputPath = "resvettori.txt";
            File file = new File(inputPath);
            FileWriter fileOut;
            FileWriter fileOutFormat;

            Scanner inputStream = new Scanner(file);
            fileOut = new FileWriter("word2vec.txt");
            fileOutFormat = new FileWriter("word2vecFormatCentri.txt");
            int count = 0;
            while(inputStream.hasNext()) {
                String temp1 = inputStream.nextLine();

                //String stampa = "" + count;
                String stampa = "" + 1;
                String stampaFormat = "";
                int count2 = 1;

                String[] s1 = temp1.substring(temp1.indexOf("[") + 1, temp1.length() - 1).split(",");
                for (int i = 0; i < s1.length; i++) {
                    //Double numero = Double.parseDouble(s1[i]) + 10;
                    stampa += " " + count2 + ":" + s1[i];

                    if (i < s1.length - 1) {
                        stampaFormat += s1[i] + ",";
                    } else {
                        stampaFormat += s1[i];
                    }
                    count2++;
                }
                fileOut.write(stampa + "\n");
                fileOutFormat.write(stampaFormat + "\n");
                count++;
            }


            fileOut.flush();
            fileOut.close();
            System.out.println("------Fine scriittura------");
            System.out.println("Canzoni analizzate: " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
