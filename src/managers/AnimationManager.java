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
    int scale = 1;
    BufferedImage bombSprite = spritesheets.get("bomb");
    SpriteAnimation bombAnim = new SpriteAnimation("idle", 4, 0);
    HashMap<String, SpriteAnimation> bombMap = new HashMap<>();
    bombMap.put("idle", bombAnim);
    animations.put("bomb", bombMap);

    int rows = 7;
    int max = 9;
    int stagger = 3;
    BufferedImage explosionSprite = spritesheets.get("explosion");
    SpriteAnimation up = new SpriteAnimation("up", max);
    SpriteAnimation central = new SpriteAnimation("central", max);
    SpriteAnimation vertical = new SpriteAnimation("", max);
    SpriteAnimation down = new SpriteAnimation("", max);
    SpriteAnimation left = new SpriteAnimation("", max);
    SpriteAnimation right = new SpriteAnimation("", max);
    SpriteAnimation horizontal = new SpriteAnimation("", max);

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
    int scale = 1;
    int rows = 3;
    int absMax = 6;
    int stagger = 10;
    BufferedImage wd1 = spritesheets.get("wd-1");
    SpriteAnimation idle = new SpriteAnimation(wd1, rows, absMax, scale, 0, 4, stagger);
    SpriteAnimation idleEdge = new SpriteAnimation(wd1, rows, absMax, scale, 1, 4, stagger);
    SpriteAnimation death = new SpriteAnimation(wd1, rows, absMax, scale, 2, absMax, stagger);
    tileMap.put("idle", idle);
    tileMap.put("idle-edge", idleEdge);
    tileMap.put("death", death);
    animations.put("wd-1", tileMap);
  }

  private void buildBomberman() {
    HashMap<String, SpriteAnimation> bombermanMap = new HashMap<>();
    float scale = 2.5f;
    int rows = 5;
    double absMax = 6.3;
    int stagger = 10;
    BufferedImage bombermanSprite = spritesheets.get("bomberman");
    SpriteAnimation left = new SpriteAnimation(bombermanSprite, rows, absMax, scale, 0, 3, stagger);
    SpriteAnimation down = new SpriteAnimation(bombermanSprite, rows, absMax, scale, 1, 3, stagger);
    SpriteAnimation right = new SpriteAnimation(bombermanSprite, rows, absMax, scale, 2, 3, stagger);
    SpriteAnimation up = new SpriteAnimation(bombermanSprite, rows, absMax, scale, 3, 3, stagger);
    SpriteAnimation death = new SpriteAnimation(bombermanSprite, rows, absMax, scale, 4, 6, stagger);
    bombermanMap.put("left", left);
    bombermanMap.put("down", down);
    bombermanMap.put("right", right);
    bombermanMap.put("up", up);
    bombermanMap.put("death", death);
    animations.put("bomberman", bombermanMap);
  }

  private void buildEnemies() {
    HashMap<String, SpriteAnimation> enemy1Map = new HashMap<>();
    float scale = 2.5f;
    int rows = 4;
    int max = 4;
    int stagger = 5;
    BufferedImage enemy1Sprite = spritesheets.get("enemy-1");
    SpriteAnimation down = new SpriteAnimation(enemy1Sprite, rows, max, scale, 0, max, stagger);
    SpriteAnimation up = new SpriteAnimation(enemy1Sprite, rows, max, scale, 1, max, stagger);
    SpriteAnimation left = new SpriteAnimation(enemy1Sprite, rows, max, scale, 2, max, stagger);
    SpriteAnimation right = new SpriteAnimation(enemy1Sprite, rows, max, scale, 3, max, stagger);
    enemy1Map.put("down", down);
    enemy1Map.put("up", up);
    enemy1Map.put("left", left);
    enemy1Map.put("right", right);
    animations.put("enemy-1", enemy1Map);
  }
}
