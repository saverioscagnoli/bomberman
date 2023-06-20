package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import managers.AnimationManager;
import util.Consts;
import util.Utils;

public class Obstacle extends Entity {
  public boolean destructable;

  public Obstacle(float posX, float posY, boolean isSolid, boolean isStatic, boolean destructable,
      BufferedImage spritesheet) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, spritesheet, isStatic);
    this.isSolid = isSolid;
    this.destructable = destructable;
  }

  public void update() {
    if (!isStatic) {
      super.updateSprite();
    }
  }

  @Override
  public void die() {
    this.setAnimation(AnimationManager.animations.get("wd-1").get("death"));
    Utils.setTimeout(() -> this.dead = true, 800);
  }

  public void render(Graphics2D g2d) {
    super.drawSprite(g2d, (int) this.posX, (int) this.posY, this.width, this.height);
  }
}
