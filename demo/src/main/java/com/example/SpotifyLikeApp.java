package com.example;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

public class SpotifyLikeApp {

  private static Clip audioClip;

  
  // DIRECTORY PATH CONFIGURED  
  private static String directoryPath =
    "C:\\Users\\thmrt\\Downloads\\sample\\demo\\src\\main\\java\\com\\example";

  public static void main(final String[] args) {
    Song[] library = readAudioLibrary();

    if (library == null) {
      System.out.println("ERROR: Could not load audio library. Please check your directoryPath.");
      return;
    }

    Scanner input = new Scanner(System.in);
    String userInput = "";

    System.out.println("Welcome to SpotifyLikeApp!");

    while (!userInput.equals("q")) {
      menu();
      userInput = input.nextLine();
      userInput = userInput.toLowerCase();
      handleMenu(userInput, library, input);
    }

    if (audioClip != null) {
      audioClip.close();
    }

    input.close();
  }

  // displays the menu for the app
   
  public static void menu() {
    System.out.println("\n==================================");
    System.out.println("---- SpotifyLikeApp ----");
    System.out.println("==================================");
    System.out.println("[H]ome");
    System.out.println("[S]earch by title");
    System.out.println("[L]ibrary");
    System.out.println("[F]avorites");
    System.out.println("[Q]uit");
    System.out.println("==================================");
    System.out.print("Enter your choice: ");
  }

  // handles the user input for the app
  public static void handleMenu(String userInput, Song[] library, Scanner input) {
    switch (userInput) {
      case "h":
        showHome(library);
        break;
      case "s":
        searchByTitle(library, input);
        break;
      case "l":
        showLibrary(library, input);
        break;
      case "f":
        showFavorites(library, input);
        break;
      case "q":
        System.out.println("\n-->Quit<--");
        System.out.println("Thank you for using SpotifyLikeApp!");
        break;
      default:
        System.out.println("Invalid option. Please try again.");
        break;
    }
  }

  //Shows the home screen with stats
   
  public static void showHome(Song[] library) {
    System.out.println("\n-->Home<--");
    System.out.println("==================================");
    System.out.println("Welcome to SpotifyLikeApp!");
    System.out.println("Total songs in library: " + library.length);
    
    int favoriteCount = 0;
    for (Song song : library) {
      if (song.isFavorite()) {
        favoriteCount++;
      }
    }
    System.out.println("Favorite songs: " + favoriteCount);
    System.out.println("==================================");
  }

  // Search for songs by title
  public static void searchByTitle(Song[] library, Scanner input) {
    System.out.println("\n-->Search by title<--");
    System.out.print("Enter song title (or part of it): ");
    String searchTerm = input.nextLine().toLowerCase();

    ArrayList<Song> results = new ArrayList<>();
    ArrayList<Integer> indices = new ArrayList<>();

    for (int i = 0; i < library.length; i++) {
      if (library[i].name().toLowerCase().contains(searchTerm)) {
        results.add(library[i]);
        indices.add(i);
      }
    }

    if (results.isEmpty()) {
      System.out.println("No songs found matching: " + searchTerm);
    } else {
      System.out.println("\nFound " + results.size() + " song(s):");
      for (int i = 0; i < results.size(); i++) {
        Song song = results.get(i);
        System.out.println((i + 1) + ". " + song.name() + " - " + song.artist() + 
                         (song.isFavorite() ? " [FAVORITE]" : ""));
      }

      System.out.print("\nEnter song number to play (0 to cancel): ");
      try {
        int choice = Integer.parseInt(input.nextLine());
        if (choice > 0 && choice <= results.size()) {
          int songIndex = indices.get(choice - 1);
          playSong(library, songIndex, input);
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input.");
      }
    }
  }

  // Shows all songs in the library
  public static void showLibrary(Song[] library, Scanner input) {
    System.out.println("\n-->Library<--");
    System.out.println("All songs:");
    
    for (int i = 0; i < library.length; i++) {
      Song song = library[i];
      System.out.println((i + 1) + ". " + song.name() + " - " + song.artist() + 
                       (song.isFavorite() ? " [FAVORITE]" : ""));
    }

    System.out.print("\nEnter song number to play (0 to cancel): ");
    try {
      int choice = Integer.parseInt(input.nextLine());
      if (choice > 0 && choice <= library.length) {
        playSong(library, choice - 1, input);
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid input.");
    }
  }

  // Shows only favorite songs

  public static void showFavorites(Song[] library, Scanner input) {
    System.out.println("\n-->Favorites<--");
    
    ArrayList<Song> favorites = new ArrayList<>();
    ArrayList<Integer> favoriteIndices = new ArrayList<>();

    for (int i = 0; i < library.length; i++) {
      if (library[i].isFavorite()) {
        favorites.add(library[i]);
        favoriteIndices.add(i);
      }
    }

    if (favorites.isEmpty()) {
      System.out.println("You have no favorite songs yet!");
      System.out.println("Play songs from the library and mark them as favorites!");
      return;
    }

    System.out.println("Your favorite songs:");
    for (int i = 0; i < favorites.size(); i++) {
      Song song = favorites.get(i);
      System.out.println((i + 1) + ". " + song.name() + " - " + song.artist());
    }

    System.out.print("\nEnter song number to play (0 to cancel): ");
    try {
      int choice = Integer.parseInt(input.nextLine());
      if (choice > 0 && choice <= favorites.size()) {
        int songIndex = favoriteIndices.get(choice - 1);
        playSong(library, songIndex, input);
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid input.");
    }
  }

  //Play a song with full controls including favorite toggle
  public static void playSong(Song[] library, int songIndex, Scanner input) {
    Song song = library[songIndex];

    System.out.println("\n-->Play<--");
    song.displayInfo();

    if (!play(song)) {
      System.out.println("ERROR: Could not play song. Check file path.");
      return;
    }

    System.out.println("\nNow playing: " + song.name() + " by " + song.artist());

    boolean keepPlaying = true;
    while (keepPlaying) {
      System.out.println("\n--- Playback Controls ---");
      System.out.println("[S]top");
      System.out.println("Fa[V]orite/Unlike");
      System.out.println("[I]nfo - Show song details");
      System.out.println("[B]ack to menu");
      System.out.print("Enter control: ");
      
      String control = input.nextLine().toLowerCase();

      switch (control) {
        case "s":
          stopSong();
          System.out.println("Song stopped.");
          keepPlaying = false;
          break;
        case "v":
          song.toggleFavorite();
          break;
        case "i":
          song.displayInfo();
          break;
        case "b":
          System.out.println("Returning to menu...");
          keepPlaying = false;
          break;
        default:
          System.out.println("Invalid control.");
          break;
      }
    }
  }

  // plays an audio file
  public static boolean play(Song song) {
    final String filename = song.fileName();
    final String filePath = directoryPath + "/wav/" + filename;
    final File file = new File(filePath);

    if (!file.exists()) {
      System.out.println("ERROR: File not found at: " + filePath);
      return false;
    }

    if (audioClip != null) {
      audioClip.close();
    }

    try {
      audioClip = AudioSystem.getClip();
      final AudioInputStream in = AudioSystem.getAudioInputStream(file);
      audioClip.open(in);
      audioClip.setMicrosecondPosition(0);
      audioClip.loop(Clip.LOOP_CONTINUOUSLY);
      return true;
    } catch (Exception e) {
      System.out.println("ERROR playing audio: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  //Stop the song
  public static void stopSong() {
    if (audioClip == null) {
      return;
    }
    audioClip.stop();
    audioClip.close();
  }

  //read the audio library of music
  public static Song[] readAudioLibrary() {
    final String jsonFileName = "audio-library.json";
    final String filePath = directoryPath + "/" + jsonFileName;

    Song[] library = null;
    try {
      System.out.println("Reading the file " + filePath);
      JsonReader reader = new JsonReader(new FileReader(filePath));
      library = new Gson().fromJson(reader, Song[].class);
      System.out.println("Successfully loaded " + library.length + " songs!");
    } catch (Exception e) {
      System.out.printf("ERROR: unable to read the file %s\n", filePath);
      System.out.println("Please check that the directoryPath variable is set correctly.");
      System.out.println();
    }

    return library;
  }
}