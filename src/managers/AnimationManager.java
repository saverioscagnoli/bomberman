package managers;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

public class AnimationManager {
  private static AnimationManager instance = null;
  public static HashMap<String, HashMap<String, SpriteAnimation>> animations;
  public static HashMap<String, BufferedImage> spritesheets = new HashMap<>();

  private AnimationManager() {
    animations = new HashMap<>();
    spritesheets = new HashMap<>();
  }

  public static synchronized AnimationManager getInstance() {
    if (instance == null) {
      instance = new AnimationManager();
    }
    return instance;
  }

  public void init() {
    this.buildSpritesheets();
    this.buildBombs();
    this.buildTiles();
    this.buildBomberman();
    this.buildEnemies();
  }

  private void buildSpritesheets() {
    spritesheets.put("bomberman", Utils.loadImage("assets/bomberman.png"));
    spritesheets.put("bomb", Utils.loadImage(Consts.bombPath + "bomb.png"));
    spritesheets.put("explosion", Utils.loadImage(Consts.bombPath + "explosion.png"));
    spritesheets.put("enemy-1", Utils.loadImage(Consts.enemiesPath + "enemy-1.png"));
    spritesheets.put("wd-1", Utils.loadImage(Consts.tilesPath + "wd-1.png"));
    spritesheets.put("basic-1-edge", Utils.loadImage(Consts.tilesPath + "basic-1-edge.png"));
    spritesheets.put("basic-1", Utils.loadImage(Consts.tilesPath + "basic-1.png"));
    spritesheets.put("basic-1-shadow", Utils.loadImage(Consts.tilesPath + "basic-1-shadow.png"));
    spritesheets.put("w-b", Utils.loadImage(Consts.tilesPath + "w-b.png"));
    spritesheets.put("w-bl", Utils.loadImage(Consts.tilesPath + "w-bl.png"));
    spritesheets.put("w-br", Utils.loadImage(Consts.tilesPath + "w-br.png"));
    spritesheets.put("w-l", Utils.loadImage(Consts.tilesPath + "w-l.png"));
    spritesheets.put("w-r", Utils.loadImage(Consts.tilesPath + "w-r.png"));
    spritesheets.put("w-t", Utils.loadImage(Consts.tilesPath + "w-t.png"));
    spritesheets.put("w-tl", Utils.loadImage(Consts.tilesPath + "w-tl.png"));
    spritesheets.put("w-tr", Utils.loadImage(Consts.tilesPath + "w-tr.png"));
    spritesheets.put("w-center", Utils.loadImage(Consts.tilesPath + "w-center.png"));
  }

  private void buildBombs() {
    SpriteAnimation bombAnim = new SpriteAnimation("idle", 4, 0);
    HashMap<String, SpriteAnimation> bombMap = new HashMap<>();
    bombMap.put("idle", bombAnim);
    animations.put("bomb", bombMap);

    int max = 9;
    SpriteAnimation up = new SpriteAnimation("up", max, 0);
    SpriteAnimation central = new SpriteAnimation("central", max, 1);
    SpriteAnimation vertical = new SpriteAnimation("vertical", max, 2);
    SpriteAnimation down = new SpriteAnimation("down", max, 3);
    SpriteAnimation left = new SpriteAnimation("left", max, 4);
    SpriteAnimation right = new SpriteAnimation("right", max, 5);
    SpriteAnimation horizontal = new SpriteAnimation("horizontal", max, 6);

    HashMap<String, SpriteAnimation> exMap = new HashMap<>();
    exMap.put("up", up);
    exMap.put("central", central);
    exMap.put("vertical", vertical);
    exMap.put("down", down);
    exMap.put("left", left);
    exMap.put("right", right);
    exMap.put("horizontal", horizontal);
    animations.put("explosion", exMap);
  }

  private void buildTiles() {
    HashMap<String, SpriteAnimation> tileMap = new HashMap<>();
    SpriteAnimation idle = new SpriteAnimation("idle", 4, 0);
    SpriteAnimation idleEdge = new SpriteAnimation("idle-edge", 4, 1);
    SpriteAnimation death = new SpriteAnimation("death", 6, 2);
    tileMap.put("idle", idle);
    tileMap.put("idle-edge", idleEdge);
    tileMap.put("death", death);
    animations.put("wd-1", tileMap);
  }

  private void buildBomberman() {
    HashMap<String, SpriteAnimation> bombermanMap = new HashMap<>();
    SpriteAnimation left = new SpriteAnimation("left", 3, 0);
    SpriteAnimation down = new SpriteAnimation("down", 3, 1);
    SpriteAnimation right = new SpriteAnimation("right", 3, 2);
    SpriteAnimation up = new SpriteAnimation("up", 3, 3);
    SpriteAnimation death = new SpriteAnimation("death", 6, 4);
    bombermanMap.put("left", left);
    bombermanMap.put("down", down);
    bombermanMap.put("right", right);
    bombermanMap.put("up", up);
    bombermanMap.put("death", death);
    animations.put("bomberman", bombermanMap);
  }

  private void buildEnemies() {
    HashMap<String, SpriteAnimation> enemy1Map = new HashMap<>();
    SpriteAnimation down = new SpriteAnimation("down", 4, 0);
    SpriteAnimation up = new SpriteAnimation("up", 4, 1);
    SpriteAnimation left = new SpriteAnimation("left", 4, 2);
    SpriteAnimation right = new SpriteAnimation("right", 4, 3);
    enemy1Map.put("down", down);
    enemy1Map.put("up", up);
    enemy1Map.put("left", left);
    enemy1Map.put("right", right);
    animations.put("enemy-1", enemy1Map);
  }
}
