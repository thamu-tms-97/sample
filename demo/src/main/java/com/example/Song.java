package com.example;

public class Song {

  private String name;
  private String artist;
  private String fileName;
  private boolean isFavorite;

  // Default constructor for Gson
  public Song() {
  }

  // serializes attributes into a string
  public String toString() {
    String s;

    // since the object is complex, we return a JSON formatted string
    s = "{ ";
    s += "name: " + name;
    s += ", ";
    s += "artist: " + artist;
    s += ", ";
    s += "isFavorite: " + isFavorite;
    s += ", ";
    s += "fileName: " + fileName;
    s += " }";

    return s;
  }

  // Display song information when playing
  public void displayInfo() {
    System.out.println("==================================");
    System.out.println("Title: " + name);
    System.out.println("Artist: " + artist);
    System.out.println("Favorite: " + (isFavorite ? "[YES]" : "[NO]"));
    System.out.println("==================================");
  }

  // getters
  public String name() {
    return this.name;
  }

  public String artist() {
    return this.artist;
  }

  public String fileName() {
    return this.fileName;
  }

  public boolean isFavorite() {
    return this.isFavorite;
  }

  // Toggle favorite status
  public void toggleFavorite() {
    this.isFavorite = !this.isFavorite;
    System.out.println(isFavorite ? "Added to favorites!" : "Removed from favorites!");
  }
}