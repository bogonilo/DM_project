package it.unipd.dei.dm1617;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class that represents a single Wikipedia page. Modify it at your
 * liking, but bear in mind that to support automatic conversion to
 * and from JSON file datasets this class must be a Java Bean: the
 * fields are private (and in our case have the same name as the
 * fields of the JSON objects we read from files) and for each field
 * there shuold be a pair of `getField` and `setField` methods.
 */
public class Song implements Serializable {

  public static Encoder<Song> getEncoder() {
    return Encoders.bean(Song.class);
  }

  private long index;

  private String text;

  private String genre;

  private int centro;

  public Song() { }

  public Song(long index, String text) {
    this.index = index;
    this.text = text;
  }

  public Song(long index, String text, String genre) {
    this.index = index;
    this.text = text;
    this.genre = genre;
  }

  public Song(long index, int centro, String genre) {
    this.index = index;
    this.centro = centro;
    this.genre = genre;
  }

  public Song(int centro) {
    this.centro = centro;
  }

  public long getIndex() {
    return index;
  }

  public void setIndex(long id) {
    this.index = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {this.text = text;}

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {this.genre = genre;}

  public int getCentro() {
    return centro;
  }

  public void setCentro(int centro) {this.centro = centro;}


  @Override
  public String toString() {
    return text;
  }

  public String toStringCentri() {
    return "Indice canzone: " + index + " di genere: " + genre + "ha centro: " + centro;
  }
}
