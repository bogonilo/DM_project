package it.unipd.dei.dm1617;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Song-Group on 10/05/17.
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
            int counterrore = 0;
            while(inputStream.hasNext()) {
                String temp1 = inputStream.nextLine();

                    //String stampa = "" + count;
                    String stampa = "" + 1;
                    String stampaFormat = "";
                    int count2 = 1;

                    String[] s1 = temp1.substring(temp1.indexOf("[") + 1, temp1.length() - 1).split(",");
                    for (int i = 0; i < s1.length; i++) {
                        Double numero = Double.parseDouble(s1[i]);
                        stampa += " " + count2 + ":" + arrotonda(numero, 15);



                        if (i < s1.length - 1) {
                            stampaFormat += s1[i] + ",";
                        } else {
                            stampaFormat += s1[i];
                        }
                        count2++;
                    }

                    if(count2 == 101)
                        fileOut.write(stampa + "\n");
                    else
                        counterrore ++;
                    fileOutFormat.write(stampaFormat + "\n");

                count++;
            }


            fileOut.flush();
            fileOut.close();
            System.out.println("------Fine scriittura------");
            System.out.println("Canzoni analizzate: " + count);
            System.out.println("Canzoni sbagliate: " + counterrore);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ARROTONDAMENTO CLASSICO
    public static double arrotonda(double value, int numCifreDecimali) {
        double temp = Math.pow(10, numCifreDecimali);
        return Math.round(value * temp) / temp;
    }
}
