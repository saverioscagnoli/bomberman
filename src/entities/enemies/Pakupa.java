package entities.enemies;

import java.awt.Graphics2D;
import entities.Enemy;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

/**
 * The Pakupa class represents an enemy character called Pakupa in the game.
 * It extends the Enemy class and defines the behavior and rendering of the
 * Pakupa enemy.
 */
public class Pakupa extends Enemy {

  /**
   * Constructs a Pakupa object with the specified position.
   * 
   * @param posX The x-coordinate of the Pakupa's position.
   * @param posY The y-coordinate of the Pakupa's position.
   */
  public Pakupa(int posX, int posY) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
        new Sprite("pakupa", 10, 4, "left", new SpriteAnimation[] {
            new SpriteAnimation("down", 10, 0, 10),
            new SpriteAnimation("up", 4, 1, 10),
            new SpriteAnimation("left", 6, 2, 10),
            new SpriteAnimation("right", 6, 3, 10)
        }, 2.5f));
    this.health = 3;
    this.score = 400;
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

  private void checkBombs() {
    TileManager tileManager = TileManager.build();
    TileType[][] grid = tileManager.grid;

    for (int i = this.gridX + 1; i < grid.length; i++) {
      if (grid[this.gridY][i] == TileType.Bomb) {
        this.direction = "right";
        if (this.sprite.currentAnimation.name != this.direction) {
          this.sprite.setAnimation(this.direction);
        }
        return;
      } else if (grid[this.gridY][i] != TileType.Empty) {
        break;
      }
    }
    for (int i = this.gridX - 1; i >= 0; i--) {
      if (grid[this.gridY][i] == TileType.Bomb) {
        this.direction = "left";
        if (this.sprite.currentAnimation.name != this.direction) {
          this.sprite.setAnimation(this.direction);
        }
        return;
      } else if (grid[this.gridY][i] != TileType.Empty) {
        break;
      }
    }
    for (int i = this.gridY + 1; i < grid.length; i++) {
      if (grid[i][this.gridX] == TileType.Bomb) {
        this.direction = "down";
        if (this.sprite.currentAnimation.name != this.direction) {
          this.sprite.setAnimation(this.direction);
        }
        return;
      } else if (grid[i][this.gridX] != TileType.Empty) {
        break;
      }
    }
    for (int i = this.gridY - 1; i >= 0; i--) {
      if (grid[i][this.gridX] == TileType.Bomb) {
        this.direction = "up";
        if (this.sprite.currentAnimation.name != this.direction) {
          this.sprite.setAnimation(this.direction);
        }
        return;
      } else if (grid[i][this.gridX] != TileType.Empty) {
        break;
      }
    }
  }

  /**
   * Updates the Pakupa's state, including movement and grid updates.
   * 
   * @param elapsed The elapsed time since the last update.
   */
  public void update(int elapsed) {
    super.update(elapsed);
    this.move(true);
    updateGrid(() -> {
    });
    if (this.posX % 48 == 0 && this.posY % 48 == 0) {
      System.out.println("check");
      this.checkBombs();
    }
  }

  /**
   * Renders the Pakupa on the graphics context.
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
    this.sprite.draw(g2d, posX + 3 + offsetX, posY + offsetY);
  }
}
