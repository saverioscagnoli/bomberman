package managers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

public class AnimationManager {
  private static AnimationManager instance = null;
  public HashMap<String, HashMap<String, SpriteAnimation>> animations;
  public HashMap<String, BufferedImage> spritesheets = new HashMap<>();

  private AnimationManager() {
    this.animations = new HashMap<>();
    this.spritesheets = new HashMap<>();
    this.init();
  }

  public static synchronized AnimationManager getInstance() {
    if (instance == null) {
      instance = new AnimationManager();
    }
    return instance;
  }

  private void init() {
    this.buildSpritesheets();
    this.buildBombs();
  }

  private void buildSpritesheets() {
    this.spritesheets.put("bomberman", Utils.loadImage("assets/bomberman.png"));
    this.spritesheets.put("bomb", Utils.loadImage(Consts.bombPath + "bomb.png"));
    this.spritesheets.put("explosion", Utils.loadImage(Consts.bombPath + "explosion.png"));
    this.spritesheets.put("enemy-1", Utils.loadImage(Consts.enemiesPath + "enemy-1.png"));
    this.spritesheets.put("wd-1.png", Utils.loadImage(Consts.tilesPath + "wd-1.png"));
    this.spritesheets.put("wd-1-edge.png", Utils.loadImage(Consts.tilesPath + "wd-1-edge.png"));
  }

  private void buildBombs() {
    int scale = 1;
    BufferedImage bombSprite = this.spritesheets.get("bomb");
    SpriteAnimation bombAnim = new SpriteAnimation(bombSprite, 1, 4, scale, 0, 4, 10);
    HashMap<String, SpriteAnimation> bombMap = new HashMap<>();
    bombMap.put("idle", bombAnim);
    this.animations.put("bomb", bombMap);

    int rows = 7;
    int max = 9;
    int stagger = 3;
    BufferedImage explosionSprite = this.spritesheets.get("explosion");
    SpriteAnimation up = new SpriteAnimation(explosionSprite, rows, max, scale, 0, max, stagger);
    SpriteAnimation central = new SpriteAnimation(explosionSprite, rows, max, scale, 1, max, stagger);
    SpriteAnimation vertical = new SpriteAnimation(explosionSprite, rows, max, scale, 2, max, stagger);
    SpriteAnimation down = new SpriteAnimation(explosionSprite, rows, max, scale, 3, max, stagger);
    SpriteAnimation left = new SpriteAnimation(explosionSprite, rows, max, scale, 4, max, stagger);
    SpriteAnimation right = new SpriteAnimation(explosionSprite, rows, max, scale, 5, max, stagger);
    SpriteAnimation horizontal = new SpriteAnimation(explosionSprite, rows, max, scale, 6, max, stagger);

    HashMap<String, SpriteAnimation> exMap = new HashMap<>();
    exMap.put("up", up);
    exMap.put("central", central);
    exMap.put("vertical", vertical);
    exMap.put("down", down);
    exMap.put("left", left);
    exMap.put("right", right);
    exMap.put("horizontal", horizontal);
    this.animations.put("explosion", exMap);
  }
}
