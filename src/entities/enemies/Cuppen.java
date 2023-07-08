package entities.enemies;

import java.awt.Graphics2D;
import entities.Enemy;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

/**
 * The Cuppen class represents an enemy character called Cuppen in the game.
 * It extends the Enemy class and defines the behavior and rendering of the
 * Cuppen enemy.
 */
public class Cuppen extends Enemy {

  /**
   * Constructs a Cuppen object with the specified position.
   * 
   * @param posX The x-coordinate of the Cuppen's position.
   * @param posY The y-coordinate of the Cuppen's position.
   */
  public Cuppen(int posX, int posY) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
        new Sprite("cuppen", 8, 1, "idle", new SpriteAnimation[] {
            new SpriteAnimation("idle", 8, 0, 15),
        }, 2.5f));

    this.health = 1;
    this.gridX = posX / Consts.tileDims;
    this.gridY = posY / Consts.tileDims;
    this.score = 400;
    this.health = 2;

    String[] dirs = { "up", "down", "left", "right" };
    String dir = Utils.pick(dirs);
    int i = 0;
    while (this.collide(dir) && i < Consts.maxIterations) {
      dir = Utils.pick(dirs);
      i++;
    }
    this.direction = dir;
  }

  /**
   * Updates the Cuppen's state, including movement and grid updates.
   * 
   * @param elapsed The elapsed time since the last update.
   */
  public void update(int elapsed) {
    super.update(elapsed);

    if (this.health == 0)
      return;

    this.move(true);
    this.updateGrid();
  }

  /**
   * Renders the Cuppen on the graphics context.
   * 
   * @param g2d The graphics context to render on.
   */
  public void render(Graphics2D g2d) {
    int offsetX = 0;
    int offsetY = 0;

    if (this.sprite.currentAnimation.name == "explosion") {
      offsetX = -5;
      offsetY = -60;
    }
    this.sprite.draw(g2d, posX + 3 + offsetX, posY - 10 + 3 + offsetY);
  }

}
