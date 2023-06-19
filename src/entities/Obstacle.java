package entities;

import java.awt.Graphics2D;
import util.Consts;
import util.Utils;

public class Obstacle extends Entity {
  public boolean destructable;

  public Obstacle(float posX, float posY, boolean isSolid, boolean isStatic, boolean destructable, String src) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, src, isStatic);
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
    this.setAnimation("death");
    Utils.setTimeout(() -> this.dead = true, 800);
  }

  public void render(Graphics2D g2d) {
    super.drawSprite(g2d, (int) this.posX, (int) this.posY, this.width, this.height);
  }
}
