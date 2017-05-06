package src.main.java.it.unipd.dei.dm1617;

/**
 * Created by daniele on 06/05/17.
 */

import java.io.*;
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

            System.out.println(inputStream.next());
            int c = 0; int x1; int count = 0; String temp2 = "";
            // hashNext() loops line-by-line
            //while(inputStream.hasNext()){
            while(c < 1000){
                //read single line, put in string
                String data = inputStream.nextLine();
                st = new StringTokenizer(data, ",");

                while(st.hasMoreTokens()) {

                    String temp = st.nextToken();

                    if (temp.length() <= 5) {

                        try {

                            x1 = Integer.parseInt(temp);

                            if(x1 == count) {
                                System.out.print("Text : " + temp2 + "\n");
                                System.out.println("****************************************************************************");
                                temp2 = "";
                                System.out.print("Index : " + x1 + "\n");
                                System.out.print("Song : " + st.nextToken() + "\n");
                                System.out.print("Year : " + st.nextToken() + "\n");
                                System.out.print("Artist : " + st.nextToken() + "\n");
                                System.out.print("Genre : " + st.nextToken() + "\n");

                                count++;
                            }
                        } catch (NumberFormatException e) {}


                    } else  temp2 += temp;

                }
                c++;
                    //fileOut.write(st.nextToken() + "\n");
             //   fileOut.write(data + "\n");
            }
            //System.out.println(inputStream.hasNext());

            //System.out.println(inputStream.next());
            // after loop, close scanner
            inputStream.close();
            fileOut.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
