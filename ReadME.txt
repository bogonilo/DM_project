Per eseguire il nostro progetto, bisogna avere a disposizione il file "sorgente-originale.csv". Dopodiché bisogna eseguire le classi nel seguente ordine sull'IDE intellij:


-CreateDataset        ---> genera il file analizzato.csv 
-Lemmatizer           ---> genera il file lemma (con tutti i testi lemmatizzati)
-Word2VecjRDD         ---> genera il modello e il file resvettori.txt (contenente tutti i vettori corrispondenti alle canzoni lemmatizzate)
-TrasformaVettori     --->
-JavaKMeans           ---> calcola i centri dei clusters
-RandomCluster        ---> partendo dal wordtovec, crea dei clusters RandomCluster
-DistanceEstimation   ---> implementa la distanza euclidea e associa ogni vettore al proprio centro (sia per il nostro clustering che per il cluster random)
-AnalisiFinale        ---> genera un quadro parziale della distribuzione delle canzoni nei vari clusters
-Entropia             ---> misura la bontà della clusterizzazione

-filter               ---> la classe filter è stata usata per impostare delle soglie e filtrare determinati generi (nel nostro caso, quelli con più di 30mila brani
                           e quelli con meno di 3 mila brani)
