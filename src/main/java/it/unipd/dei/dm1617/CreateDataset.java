package it.unipd.dei.dm1617;

/**
 * Created by daniele on 06/05/17.
 */

import edu.stanford.nlp.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateDataset {

    public static void main(String args[]){
        String inputPath = "sorgente-originale.csv";

        File file = new File(inputPath);
        int[] n_canzoni_genere = new int[10];

        try{
            //read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);

            FileWriter fileOut = new FileWriter("analizzato.csv");
            FileWriter fileOut2 = new FileWriter("canzoni.txt");


            List<String> deutsch = new ArrayList<String>();
            deutsch.add("Ich");deutsch.add("ich");deutsch.add("nein");deutsch.add("dich");
            List<String> spanish = new ArrayList<String>();
            spanish.add("nada");spanish.add("siempre");spanish.add("todo");spanish.add("para");spanish.add("Que");spanish.add("que");

            String patternStringDeutsch = "\\b(" + StringUtils.join(deutsch, "|") + ")\\b";
            Pattern patternDeutsch = Pattern.compile(patternStringDeutsch);
            Matcher matcherDeutsch;

            String patternStringSpanish = "\\b(" + StringUtils.join(spanish, "|") + ")\\b";
            Pattern patternSpain = Pattern.compile(patternStringSpanish);
            Matcher matcherSpanish;

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

                                        matcherDeutsch = patternDeutsch.matcher(temp2);
                                        matcherSpanish = patternSpain.matcher(temp2);

                                        if(!matcherDeutsch.find()) {
                                            if(!matcherSpanish.find()) {

                                                if (temp2.charAt(0) == '"' || temp2.charAt(1) == '"')
                                                    canzone += "\"text\":" + temp2.trim();
                                                else
                                                    canzone += "\"text\":\"" + temp2.trim();

                                                if (temp2.charAt(temp2.length() - 1) == '"')
                                                    canzone += "}\n";
                                                else
                                                    canzone += "\"}\n";

                                                fileOut.write(canzone);
                                                numcanzoni++;

                                                switch (genere){
                                                    case "Rock":
                                                        n_canzoni_genere[0]++;
                                                        break;
                                                    case "Jazz":
                                                        n_canzoni_genere[1]++;
                                                        break;
                                                    case "Metal":
                                                        n_canzoni_genere[2]++;
                                                        break;
                                                    case "Pop":
                                                        n_canzoni_genere[3]++;
                                                        break;
                                                    case "Hip-Hop":
                                                        n_canzoni_genere[4]++;
                                                        break;
                                                    case "Electronic":
                                                        n_canzoni_genere[5]++;
                                                        break;
                                                    case "R&B":
                                                        n_canzoni_genere[6]++;
                                                        break;
                                                    case "Indie":
                                                        n_canzoni_genere[7]++;
                                                        break;
                                                    case "Country":
                                                        n_canzoni_genere[8]++;
                                                        break;
                                                    case "Folk":
                                                        n_canzoni_genere[9]++;
                                                        break;
                                                }

                                            }
                                        }
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
            System.out.println("Canzoni estratte: " + (numcanzoni-1));
            for(int i = 0 ; i<n_canzoni_genere.length; i++){
                System.out.println("N canzoni per genere " + (i+1) + ": " + n_canzoni_genere[i]);
            }
            fileOut2.write("" +numcanzoni);
            fileOut2.flush();
            fileOut2.close();

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