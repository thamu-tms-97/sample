package com.example;

public class Song {
  private String name;
  private String artist;
  private String fileName;
  private int year;
  private String genre;
  private boolean isFavorite;
  private String filePath;

  public Song() {}

  public Song(String name, String artist, String fileName, int year, String genre, boolean isFavorite, String filePath) {
    this.name = name;
    this.artist = artist;
    this.fileName = fileName;
    this.year = year;
    this.genre = genre;
    this.isFavorite = isFavorite;
    this.filePath = filePath;
  }

  public String name() { return name; }
  public String artist() { return artist; }
  public String fileName() { return fileName; }
  public int year() { return year; }
  public String genre() { return genre; }
  public String filePath() { return filePath; }
  public boolean isFavorite() { return isFavorite; }

  public void toggleFavorite() {
    isFavorite = !isFavorite;
    System.out.println(isFavorite ? "Marked as favorite" : "Removed from favorites");
  }

  public void displayInfo() {
    System.out.println("Title : " + name);
    System.out.println("Artist: " + artist);
    System.out.println("Year  : " + year);
    System.out.println("Genre : " + genre);
    System.out.println("File  : " + fileName);
    System.out.println("Path  : " + filePath);
    System.out.println("Fav   : " + (isFavorite ? "Yes" : "No"));
  }
}
