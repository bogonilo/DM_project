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

  private String genres;

  private String text;

  public Song() { }

  public Song(long index, String title,String genres) {
    this.index = index;
    this.text = text;
    this.genres = genres;
  }

  public long getIndex() {
    return index;
  }

  public void setIndex(long id) {
    this.index = id;
  }

  public String getGenres() {
    return genres;
  }

  public void setGenres(String genres) {
    this.genres = genres;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }


  @Override
  public String toString() {
    return "(" + index + ") " + genres +" " + text;
  }
}
