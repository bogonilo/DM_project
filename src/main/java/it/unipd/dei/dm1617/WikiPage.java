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
public class WikiPage implements Serializable {

  public static Encoder<WikiPage> getEncoder() {
    return Encoders.bean(WikiPage.class);
  }

  private int id;

  private String title;

  private int year;

  private String artist;

  private String[] genre;

  private String text;

  public WikiPage() { }

  public WikiPage(int id, String title, int year, String artist, String[] genre, String text) {
    this.id = id;
    this.title = title;
    this.year = year;
    this.artist = artist;
    this.genre = genre;
    this.text = text;
  }

  //metodi per ID

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  //metodi per TITLE

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  //metodi per YEAR

  public int getYear(){ return year; }

  public void setYear(int year){ this.year = year; }

  //metodi per ARTIST

  public String getArtist(){ return artist; }

  public void setArtist(){ this.artist = artist; }

  //metodi per TEXT

  public String getText() { return text; }

  public void setText(String text) {
    this.text = text;
  }

  public String[] getGenre() {
    return genre;
  }

  public void setGenre(String[] genre) {
    this.genre = genre;
  }

  @Override
  public String toString() {
    return "( " + id + " ) | " + title + " | " + year + " | " + artist + " | " + Arrays.asList(genre) + " | " + text;
  }
}
