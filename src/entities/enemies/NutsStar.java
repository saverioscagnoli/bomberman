package entities.enemies;

import java.awt.Graphics2D;
import entities.Enemy;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

/**
 * The NutsStar class represents an enemy character called NutsStar in the game.
 * It extends the Enemy class and defines the behavior and rendering of the
 * NutsStar enemy.
 */
public class NutsStar extends Enemy {
  private boolean justChanged;

  /**
   * Constructs a NutsStar object with the specified position.
   * 
   * @param posX The x-coordinate of the NutsStar's position.
   * @param posY The y-coordinate of the NutsStar's position.
   */
  public NutsStar(int posX, int posY) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
        new Sprite("nuts-star", 9, 4, "left", new SpriteAnimation[] {
            new SpriteAnimation("down", 9, 0, 10),
            new SpriteAnimation("up", 9, 1, 10),
            new SpriteAnimation("left", 7, 2, 10),
            new SpriteAnimation("right", 7, 3, 10)
        }, 2.5f));

    this.score = 200;
    this.justChanged = false;
    this.health = 1;
    String[] dirs = { "up", "down", "left", "right" };
    String dir = Utils.pick(dirs);
    int i = 0;
    while (this.collide(dir) && i < Consts.maxIterations) {
      dir = Utils.pick(dirs);
      i++;
    }
    this.direction = dir;
    this.sprite.setAnimation(this.direction);
  }

  private void setDirection(String direction) {
    this.direction = direction;
    this.sprite.setAnimation(this.direction);
    this.justChanged = true;
  }

  private void handleDirectionChange(String[] directions, boolean check, Runnable fn) {
    if (this.gridX % 2 != 0 && this.gridY % 2 != 0 && !this.justChanged && check) {
      if (Utils.rng(1, 5) == 1) {
        String dir = Utils.pick(directions);
        int i = 0;
        while (this.collide(dir) && i < Consts.maxIterations) {
          dir = Utils.pick(directions);
          i++;
        }
        this.setDirection(dir);
      } else {
        fn.run();
        this.justChanged = true;
      }
    }
  }

  /**
   * Updates the NutsStar's state, including movement and grid updates.
   * 
   * @param elapsed The elapsed time since the last update.
   */
  public void update(int elapsed) {
    super.update(elapsed);
    this.move(true);

    switch (this.direction) {
      case "up": {
        int edge = this.gridY * Consts.tileDims;
        String[] directions = new String[] { "left", "right", "down" };
        this.handleDirectionChange(directions, this.posY <= edge, () -> this.posY -= this.speed);
        break;
      }
      case "down": {
        int edge = this.gridY * Consts.tileDims + Consts.tileDims;
        String[] directions = new String[] { "left", "right", "up" };
        this.handleDirectionChange(directions, this.posY + this.height >= edge, () -> this.posY += this.speed);
        break;
      }
      case "left": {
        int edge = this.gridX * Consts.tileDims;
        String[] directions = new String[] { "up", "down", "right" };
        this.handleDirectionChange(directions, this.posX <= edge, () -> this.posX -= this.speed);
        break;
      }
      case "right": {
        int edge = this.gridX * Consts.tileDims + Consts.tileDims;
        String[] directions = new String[] { "up", "down", "left" };
        this.handleDirectionChange(directions, this.posX + this.width >= edge, () -> this.posX += this.speed);
        break;
      }
    }

    this.updateGrid(() -> this.justChanged = false);
  }

  /**
   * Renders the NutsStar on the graphics context.
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
    this.sprite.draw(g2d, posX + 3 + offsetX, posY - 10 + offsetY);
  }
}
