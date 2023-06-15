package entities;

import java.awt.Color;
import java.awt.Graphics2D;

import util.Consts;
import util.Utils;

public class Explosion extends Entity {

  public Explosion(int posX, int posY) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, "");

    Utils.setTimeout(() -> this.die(), 500);
  }

  @Override
  public void render(Graphics2D g2d) {
    g2d.setColor(Color.BLUE);
    g2d.fillRect((int) posX, (int) posY, width, height);
  }
}
