package entities;

import java.awt.Graphics2D;
import util.Consts;

public class Obstacle extends Entity {
  public boolean destructable;

  public Obstacle(float posX, float posY, boolean isSolid, boolean destructable, String src) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, src, true);
    this.isSolid = isSolid;
    this.destructable = destructable;
  }

  public void render(Graphics2D g2d) {
    super.drawSprite(g2d, (int) this.posX, (int) this.posY, this.width, this.height);
  }
}
