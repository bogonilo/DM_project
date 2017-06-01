package it.unipd.dei.dm1617;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Entropia {




    public static List<Double> entropy(String inputPath,int nCluster) {

        SparkConf sparkConf = new SparkConf()
                .setMaster("local[4]")
                .setAppName("entropy");

        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<String> text = sc.textFile(inputPath, 10);

        // inizializzo una lista
        List<Tuple2<String, Integer>> pointClu= new ArrayList<>();


        //conto quanti punti ci sono per ogni Cluster
        JavaPairRDD<String, Integer> pre = text
                .mapToPair((line) -> {
                            Scanner st = new Scanner(line);
                            String genere = st.next();
                            int clu = Integer.parseInt(st.next());
                            return (new Tuple2<>(genere, 1));
                        }

                )
                .reduceByKey((x, y) -> x + y);

        pointClu = pre.collect();
        for(int i=0; i<pointClu.size(); i++){
            System.out.println("Genere "+(pointClu.get(i))._1+ " ha "+(pointClu.get(i))._2 +" punti");
        }

        //questo per ogni genere di ogni cluster quante canzoni ci sono
        JavaPairRDD<String, Iterable<Tuple2<Integer,Integer>>> array = text
                .mapToPair((line) -> {
                            Scanner st = new Scanner(line);
                            String genere = st.next();
                            int clu = Integer.parseInt(st.next());
                            Tuple2<String,Integer> coppia = new Tuple2<>(genere,clu);
                            return (new Tuple2<>(coppia, 1));
                        }

                )
                .reduceByKey((x, y) -> x + y)
                .mapToPair((line) ->{
                    Tuple2<Integer,Integer> clu = new Tuple2<>(line._1._2,line._2);
                    Tuple2<String,Tuple2<Integer,Integer>> kv = new Tuple2<>(line._1._1,clu);
                    return kv;
                })
                .groupByKey();





        System.out.println(array.collect());

        List<Tuple2<String,Iterable<Tuple2<Integer,Integer>>>> finale = array.collect();
        //ordino l'array con i cluster secondo criterio
        List<Tuple2<Integer,Integer>> interna = new ArrayList<>();
        int e=0;
        List<Double> entropy= new ArrayList<>();





        double mcival=0;
        double somma=0;
        double rap=0;

        for(int i=0; i<finale.size(); i++){
            List<Integer> mci= new ArrayList<>();
            List<Integer> clu= new ArrayList<>();
            (finale.get(i)._2).forEach((item)-> mci.add(item._2)  );
            (finale.get(i)._2).forEach((item)-> clu.add(item._1)  );
            // System.out.println(clu);
            // System.out.println(mci);
            for(int c=0; c < nCluster ; c++){
                e=0;
                //trovo il valore del cluster corrispondente e lo salvo in un intero
                while( (clu.get(e) != c) && ( e < clu.size()-1) ){
                    e++;

                }
                if(clu.get(e) == c) {
                    mcival = mci.get(e);
                }
                else mcival=pointClu.get(i)._2 ;
                rap=mcival/pointClu.get(i)._2;
                somma = somma -((double)(rap)*(Math.log10(rap))/ (Math.log10(2)));
                //System.out.println(somma);
            }

            entropy.add(i,somma);
            somma=0;
        }
        sc.stop();
        return entropy;
    }

    public static void main(String[] args){

        System.out.println("entropia nostro cluster \n"+entropy("GenereCentroEntropia.txt",10)+"\n"+"entropia random \n"+entropy("GenereCentroEntropiaRandom.txt",10));
    }

}
