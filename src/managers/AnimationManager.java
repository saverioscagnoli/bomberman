package managers;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import util.Consts;
import util.Utils;

/* THIS CLASS PRE-LOADS EVERY SPRITE FOR EVERYTHING, TO PREVENT PERFORMANCE ISSUES */
public class AnimationManager {
  private static AnimationManager instance = null;
  public HashMap<String, BufferedImage> spriteMap = new HashMap<>();

  private AnimationManager() {
    spriteMap = new HashMap<>();
    this.buildspriteMap();
  }

  public static synchronized AnimationManager build() {
    if (instance == null) {
      instance = new AnimationManager();
    }
    return instance;
  }

  public BufferedImage getSprite(String spriteName) {
    return this.spriteMap.get(spriteName);
  }

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
  }
}
