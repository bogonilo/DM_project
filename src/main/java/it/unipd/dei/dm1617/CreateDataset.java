package it.unipd.dei.dm1617;

/**
 * Created by daniele on 06/05/17.
 */

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class CreateDataset {

    public static void main(String args[]){
        String inputPath = "sorgente-originale.csv";

        File file = new File(inputPath);

        try{
            // -read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);

            FileWriter fileOut = new FileWriter("analizzato.csv");

            StringTokenizer st;

            int numcanzoni = 0;
            String canzone = "";
            String genere = "";

            System.out.println(inputStream.next());
            int x1; int count = 0; String temp2 = "";
            // hashNext() loops line-by-line
            while(inputStream.hasNext()){
            //while(count < 150001){
                //read single line, put in string
                String data = inputStream.nextLine();
                st = new StringTokenizer(data, ",");

                while(st.hasMoreTokens()) {

                    String temp = st.nextToken();

                    if (temp.length() <= 6) {

                        try {

                            x1 = Integer.parseInt(temp);

                            if(x1 == count) {
                                if(count != 0) {

                                    if(!(temp2.equalsIgnoreCase("") ||
                                            genere.equalsIgnoreCase("other") ||
                                            genere.equalsIgnoreCase("not available") ||
                                            temp2.equalsIgnoreCase("[Instrumental]") ||
                                            temp2.equalsIgnoreCase("\"[Patterson][instrumental]\"") ||
                                            temp2.equalsIgnoreCase("\"[Patterson] [instrumental]\"") ||
                                            temp2.equalsIgnoreCase("Instrumental") ||
                                            temp2.equalsIgnoreCase("(Instrumental)") ||
                                            temp2.equalsIgnoreCase("[Lyrics not available]"))){

                                                if(temp2.charAt(0) == '"' || temp2.charAt(1) == '"' )
                                                    canzone += "\"text\":" + temp2.trim();
                                                else
                                                    canzone += "\"text\":\"" + temp2.trim();

                                                if(temp2.charAt(temp2.length()-1) == '"' )
                                                    canzone += "}\n";
                                                else
                                                    canzone += "\"}\n";

                                                fileOut.write(canzone);
                                                numcanzoni ++;
                                    }
                                    temp2 = "";
                                }
                                canzone = "{\"index\":" + x1 + "," ;
                                st.nextToken() ;
                                st.nextToken() ;
                                st.nextToken() ;
                                genere = st.nextToken();
                                canzone += "\"genre\":\"" + genere + "\",";

                                count++;

                            }
                        } catch (NumberFormatException e) {}


                    } else{
                        if(temp2.length() != 0)
                            temp2 += " " + temp;
                        else
                            temp2 = temp;
                    }

                }
                //fileOut.write(st.nextToken() + "\n");
                //   fileOut.write(data + "\n");
            }
            //System.out.println(inputStream.hasNext());

            //System.out.println(inputStream.next());
            // after loop, close scanner
            inputStream.close();
            fileOut.close();
            System.out.println("Indice ultima canzone analizzata: " + (count-1));
            System.out.println("Canzoni estratte: " + numcanzoni);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NoSuchElementException e) {
            e.printStackTrace();
        }

    }
}