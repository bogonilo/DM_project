package src.main.java.it.unipd.dei.dm1617;

/**
 * Created by daniele on 12/05/17.
 */
public class Generi {
    String genere;
    String testo;

    public Generi (String genere, String testo){
        this.genere = genere;
        this.testo = "";
    }

    public void setGenere(String genere){
        this.genere += genere;
    }

    public void setTesto(String testo){
        this.testo += testo;
    }

    public String getTesto(){
        return testo;
    }

    public String getGenere(){
        return genere;
    }
}
