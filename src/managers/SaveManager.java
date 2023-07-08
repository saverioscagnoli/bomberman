package managers;

import java.io.InputStreamReader;
import java.util.HashMap;

import util.Consts;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * The SaveManager class is responsible for managing saved data such as wins,
 * losses, and progress.
 * 
 * It provides methods to read and write progress data, increment levels, wins,
 * losses, and scores, as well as set the player's name.
 * 
 * This class is abstract and cannot be instantiated.
 */
public abstract class SaveManager {

  /**
   * 
   * Reads the progress data from the saved file.
   * 
   * @return A HashMap containing the progress data.
   */
  public static HashMap<String, String> readProgress() {
    HashMap<String, String> data = new HashMap<String, String>();
    try {
      FileInputStream fs = new FileInputStream(Consts.progressPath);
      BufferedReader br = new BufferedReader(new InputStreamReader(fs));
      String line;
      while ((line = br.readLine()) != null) {
        String[] splitted = line.split("=");
        data.put(splitted[0], splitted[1]);
      }
      fs.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }

  /**
   * 
   * Writes the progress data to the save file.
   * 
   * @param data The progress data to be written.
   */
  public static void writeProgress(HashMap<String, String> data) {
    try {
      FileWriter writer = new FileWriter(Consts.progressPath);
      data.forEach((k, v) -> {
        try {
          writer.write(k + "=" + v + "\n");
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   * Increments the current level in the progress data.
   */
  public static void incrementLevel() {
    HashMap<String, String> data = SaveManager.readProgress();
    int lvl = Integer.parseInt(data.get("level"));
    data.put("level", "" + (lvl + 1));
    SaveManager.writeProgress(data);
  }

  /**
   * 
   * Resets the level to 1 in the progress data.
   */
  public static void resetLevel() {
    HashMap<String, String> data = SaveManager.readProgress();
    data.put("level", "1");
    SaveManager.writeProgress(data);
  }

  /**
   * 
   * Increments the number of wins in the progress data.
   */
  public static void incrementWins() {
    HashMap<String, String> data = SaveManager.readProgress();
    int wins = Integer.parseInt(data.get("wins"));
    data.put("wins", "" + (wins + 1));
    SaveManager.writeProgress(data);
  }

  /**
   * 
   * Increments the number of losses in the progress data.
   */
  public static void incrementLosses() {
    HashMap<String, String> data = SaveManager.readProgress();
    int losses = Integer.parseInt(data.get("losses"));
    data.put("losses", "" + (losses + 1));
    SaveManager.writeProgress(data);
  }

  /**
   * 
   * Increments the score in the progress data by the specified amount.
   * 
   * @param score The amount to increment the score by.
   */
  public static void incrementScore(int score) {
    HashMap<String, String> data = SaveManager.readProgress();
    int oldscore = Integer.parseInt(data.get("score"));
    data.put("score", "" + (oldscore + score));
    SaveManager.writeProgress(data);
  }

  /**
   * 
   * Sets the player's name in the progress data.
   * 
   * @param name The player's name to be set.
   */
  public static void setName(String name) {
    HashMap<String, String> data = SaveManager.readProgress();
    data.put("name", "" + name);
    SaveManager.writeProgress(data);
  }
}