package entities;

import java.awt.Graphics2D;

import ui.Sprite;
import util.Consts;
import util.Utils;

public class Obstacle extends Entity {
  public boolean destructable;
  public boolean isStatic = false;

  public Obstacle(int posX, int posY, boolean isSolid, boolean isStatic, boolean destructable, Sprite sprite) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, sprite);
    this.isSolid = isSolid;
    this.isStatic = isStatic;
    this.destructable = destructable;
  }

  public Obstacle(int posX, int posY, boolean isSolid, String spriteName) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, new Sprite(spriteName, 1, 1, null, null, 1));
    this.isSolid = isSolid;
    this.isStatic = true;
    this.destructable = false;
  }

  public void update(int elapsed) {
    if (!isStatic) {
      this.sprite.update(elapsed);
    }
  }

  @Override
  public void die() {
    this.sprite.setAnimation("death");
    Utils.setTimeout(() -> this.dead = true, 650);
  }

  @Override
  public void render(Graphics2D g2d) {
    if (this.isStatic) {
      g2d.drawImage(this.sprite.spritesheet, this.posX, this.posY, this.width, this.height, null);
    } else {
      this.sprite.draw(g2d, posX, posY, this.width, this.height);
    }
  }
}
