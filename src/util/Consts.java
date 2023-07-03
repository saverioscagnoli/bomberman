package util;

import managers.TileManager;

/*
 * If you have to define global variables / constats DEFINE THEM HERE
 * THIS WILL BE ACCESSIBLE IN EVERY PART OF THE PROJECT
 */

public abstract class Consts {
  public static final int screenWidth = 48 * 17 + 15;
  public static final int screenHeight = 48 * 15 + 15;
  public static final int tileDims = 48;
  public static final int gridHeight = TileManager.build().grid.length;
  public static final int gridWidth = TileManager.build().grid[0].length;
  public static final String progressPath = "data/progress.bmb";
  public static final String tilesPath = "assets/tiles/";
  public static final String soundPath = "assets/sounds/";
  public static final String bombPath = "assets/bomb/";
  public static final String enemiesPath = "assets/enemies/";
  public static final String powerupsPath = "assets/powerups/";
}
