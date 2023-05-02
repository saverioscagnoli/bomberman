package entities;

import java.awt.Color;
import java.awt.Graphics2D;

import loop.GameLoop;

public class Obstacle extends Entity {
  private static final int dims = 48;

  public Obstacle(float posX, float posY) {
    super(posX, posY, dims, dims, 0);
    this.normalizePos();
  }

  public void normalizePos() {
    // Fixing the obstacle's position in tiles
    double pX = this.posX + this.width * 0.5;
    double pY = this.posY + this.height * 0.5;
    this.posX = (float) (pX - (pX % dims));
    this.posY = (float) (pY - (pY % dims));
  }

 public void update() {
  // If the obstacle is hit by a bomb, delete itself

  if (/* TODO collision with bomb check */ false) {
    GameLoop.entities.remove(GameLoop.entities.indexOf(this));
  }
 }

  public void render(Graphics2D g2d) {
    g2d.setColor(Color.RED);
    g2d.fillRect((int) this.posX, (int) this.posY, this.width, this.height);
  }
}
