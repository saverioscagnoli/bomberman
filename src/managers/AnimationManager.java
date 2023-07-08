package managers;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import util.Consts;
import util.Utils;

/**
 * 
 * The AnimationManager class is responsible for pre-loading and managing sprite
 * animations to prevent performance issues.
 * 
 * It pre-loads all the sprite images for various entities and provides a method
 * to access the sprites.
 * 
 * This class follows the singleton design pattern to ensure only one instance
 * is created.
 */
public class AnimationManager {
  private static AnimationManager instance = null;
  public HashMap<String, BufferedImage> spriteMap = new HashMap<>();

  /**
   * 
   * Private constructor to enforce the singleton pattern.
   * Initializes the sprite map by pre-loading all the sprite images.
   */
  private AnimationManager() {
    spriteMap = new HashMap<>();
    this.buildspriteMap();
  }

  /**
   * 
   * Retrieves the singleton instance of AnimationManager.
   * 
   * @return The AnimationManager instance.
   */
  public static synchronized AnimationManager build() {
    if (instance == null) {
      instance = new AnimationManager();
    }
    return instance;
  }

  /**
   * 
   * Retrieves the sprite image associated with the given sprite name.
   * 
   * @param spriteName The name of the sprite.
   * @return The sprite image, or null if the sprite is not found.
   */
  public BufferedImage getSprite(String spriteName) {
    return this.spriteMap.get(spriteName);
  }

  /**
   * 
   * Pre-loads all the sprite images and adds them to the sprite map.
   */
  private void buildspriteMap() {
    spriteMap.put("bomberman", Utils.loadImage("assets/bomberman.png"));
    spriteMap.put("bomb", Utils.loadImage(Consts.bombPath + "bomb.png"));
    spriteMap.put("explosion", Utils.loadImage(Consts.bombPath + "explosion.png"));
    spriteMap.put("enemy-1", Utils.loadImage(Consts.enemiesPath + "enemy-1.png"));
    spriteMap.put("wd-1", Utils.loadImage(Consts.tilesPath + "wd-1.png"));
    spriteMap.put("basic-1-edge", Utils.loadImage(Consts.tilesPath + "basic-1-edge.png"));
    spriteMap.put("basic-1", Utils.loadImage(Consts.tilesPath + "basic-1.png"));
    spriteMap.put("basic-1-shadow", Utils.loadImage(Consts.tilesPath + "basic-1-shadow.png"));
    spriteMap.put("w-b", Utils.loadImage(Consts.tilesPath + "w-b.png"));
    spriteMap.put("w-bl", Utils.loadImage(Consts.tilesPath + "w-bl.png"));
    spriteMap.put("w-br", Utils.loadImage(Consts.tilesPath + "w-br.png"));
    spriteMap.put("w-l", Utils.loadImage(Consts.tilesPath + "w-l.png"));
    spriteMap.put("w-r", Utils.loadImage(Consts.tilesPath + "w-r.png"));
    spriteMap.put("w-t", Utils.loadImage(Consts.tilesPath + "w-t.png"));
    spriteMap.put("w-tl", Utils.loadImage(Consts.tilesPath + "w-tl.png"));
    spriteMap.put("w-tr", Utils.loadImage(Consts.tilesPath + "w-tr.png"));
    spriteMap.put("w-center", Utils.loadImage(Consts.tilesPath + "w-center.png"));
    spriteMap.put("clown-mask", Utils.loadImage(Consts.enemiesPath + "clown-mask.png"));
    spriteMap.put("clown-bullet", Utils.loadImage(Consts.enemiesPath + "clown-bullet.png"));
    spriteMap.put("enemy-explosion", Utils.loadImage(Consts.enemiesPath + "enemy-explosion.png"));
    spriteMap.put("hatch", Utils.loadImage(Consts.tilesPath + "hatch.png"));
    spriteMap.put("bomberman-hatch", Utils.loadImage("assets/bomberman-hatch.png"));
    spriteMap.put("bomb+1", Utils.loadImage(Consts.powerupsPath + "bomb+1.png"));
    spriteMap.put("radius+1", Utils.loadImage(Consts.powerupsPath + "radius+1.png"));
    spriteMap.put("pass-through", Utils.loadImage(Consts.powerupsPath + "pass-through.png"));
    spriteMap.put("lives+1", Utils.loadImage(Consts.powerupsPath + "lives+1.png"));
    spriteMap.put("Rollers", Utils.loadImage(Consts.powerupsPath + "Rollers.png"));
    spriteMap.put("AcidRain", Utils.loadImage(Consts.powerupsPath + "AcidRain.png"));
    spriteMap.put("Vest", Utils.loadImage(Consts.powerupsPath + "Vest.png"));
    spriteMap.put("Skull", Utils.loadImage(Consts.powerupsPath + "Skull.png"));
    spriteMap.put("buttonToggle", Utils.loadImage("assets/" + "buttonToggle.png"));
    spriteMap.put("denkyun", Utils.loadImage(Consts.enemiesPath + "denkyun.png"));
    spriteMap.put("nuts-star", Utils.loadImage(Consts.enemiesPath + "nuts-star.png"));
    spriteMap.put("pakupa", Utils.loadImage(Consts.enemiesPath + "pakupa.png"));
    spriteMap.put("Faralsboss", Utils.loadImage(Consts.enemiesPath + "Faralsboss.png"));
    spriteMap.put("cuppen", Utils.loadImage(Consts.enemiesPath + "cuppen.png"));
  }
}