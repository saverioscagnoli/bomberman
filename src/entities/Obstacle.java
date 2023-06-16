package entities;

import java.awt.Graphics2D;
import util.Utils;

public class Obstacle extends Entity {
  private static final int dims = 48;
  public boolean destructable;

  public Obstacle(float posX, float posY, boolean destructable) {
    super(posX, posY, dims, dims, 0, destructable ? "/assets/wall-destructable.png" : "/assets/wall.png", true);
    this.isSolid = true;
    this.destructable = destructable;
    this.normalizePos();
  }

  @Override
  public void update() {
    super.updateSprite();
  }

  private void normalizePos() {
    // Fixing the obstacle's position in tiles
    double pX = this.posX + this.width * 0.5;
    double pY = this.posY + this.height * 0.5;
    int[] norm = Utils.normalizePos((int) pX, (int) pY);
    this.posX = norm[0];
    this.posY = norm[1];
  }

  public void render(Graphics2D g2d) {
    super.drawSprite(g2d, (int) this.posX, (int) this.posY, this.width, this.height);
  }
}
