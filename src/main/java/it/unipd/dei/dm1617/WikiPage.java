package it.unipd.dei.dm1617;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Class that represents a single Wikipedia page. Modify it at your
 * liking, but bear in mind that to support automatic conversion to
 * and from JSON file datasets this class must be a Java Bean: the
 * fields are private (and in our case have the same name as the
 * fields of the JSON objects we read from files) and for each field
 * there shuold be a pair of `getField` and `setField` methods.
 */
public class WikiPage implements Serializable{

  public static Encoder<WikiPage> getEncoder() {
    return Encoders.bean(WikiPage.class);
  }

  private String id;

  private String song;

  private String year;

  private String artist;

  private String genre;

  private String lyrics;

  public WikiPage() { }

  public WikiPage(String id, String song, String year, String artist) {
    this.id = id;
    this.song = song;
    this.year = year;
    this.artist = artist;
  }

  public WikiPage(String id, String song, String year, String artist, String genre, String lyrics) {
    this.id = id;
    this.song = song;
    this.year = year;
    this.artist = artist;
    this.genre = genre;
    this.lyrics = lyrics;
  }

  public WikiPage(String genre, String lyrics) {
    this.genre = genre;
    this.lyrics = lyrics;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSong() { return song; }

  public void setSong(String song) {
    this.song = song;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getLyrics() {
    return lyrics;
  }

  public void setLyrics(String Lyrics) {
    this.lyrics = lyrics;
  }

  @Override
  public String toString() {
    return "{ " + id + " || " + song + " || " + year + " || " + artist + " || " + genre + " || " + lyrics + " }";
  }
}
