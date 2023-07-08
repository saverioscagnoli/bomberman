package entities.bosses;

import java.awt.Graphics2D;
import core.Loop;
import entities.Bomb;
import entities.Bomberman;
import entities.Enemy;
import entities.Tile;
import managers.BombManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

/**
 * The FaralsBoss class represents a boss enemy called Farals in the game.
 * It extends the Enemy class and defines the behavior and rendering of the boss
 * enemy.
 */
public class FaralsBoss extends Enemy {

  public boolean stop;
  public boolean hit;
  private boolean hitFlag;

  /**
   * Constructs a FaralsBoss object with the specified position.
   * 
   * @param posX The x-coordinate of the boss's position.
   * @param posY The y-coordinate of the boss's position.
   */
  public FaralsBoss(int posX, int posY) {
    super(posX, posY, 0, 0, 1,
        new Sprite("Faralsboss", 5, 4, "idle-1", new SpriteAnimation[] {
            new SpriteAnimation("idle-1", 2, 0, 50),
            new SpriteAnimation("hit-1", 5, 1, 10),
            new SpriteAnimation("hit-2", 5, 2, 6),
            new SpriteAnimation("idle-2", 4, 3, 3)
        }, 2.5f));

    this.health = 10;
    this.speed = 1;
    this.stop = false;
    this.width = (int) (this.sprite.width * this.sprite.scale);
    this.height = (int) (this.sprite.height * this.sprite.scale);
    this.hit = false;
    this.hitFlag = false;
  }

  /**
   * Sets the boss as dead and reveals the hatch tile.
   */
  public void die() {
    Tile hatch = TileManager.build().hatch;
    hatch.isVisible = true;
    hatch.posX = Consts.tileDims * 2;
    hatch.posY = Consts.tileDims;
    TileManager.build().grid[1][2] = TileType.Hatch;
    this.dead = true;
  }

  /**
   * Moves the boss towards the player character's position.
   */
  private void move() {
    Bomberman bomberman = Loop.build().bomberman;
    if (bomberman.dead)
      return;
    double targetX = bomberman.posX + bomberman.sprite.width * 0.5 - width * 0.5;
    double targetY = bomberman.posY + bomberman.sprite.height * 0.5 - height * 0.5;

    if (!this.stop) { // Check if the target is not reached yet
      double dx = targetX - posX;
      double dy = targetY - posY;

      if (dx != 0 || dy != 0) {

        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        double maxAbs = Math.max(absDx, absDy);

        if (Math.sqrt(dx * dx + dy * dy) < 60) {
          if (!bomberman.dead && !bomberman.immune) {
            bomberman.die();
          }
        }

        double offsetX = (dx / maxAbs) * this.speed * 1.3;
        double offsetY = (dy / maxAbs) * this.speed * 1.3;

        double newPosX = posX + offsetX;
        double newPosY = posY + offsetY;

        // Check if the enemy will overshoot the target
        if ((dx > 0 && newPosX > targetX) || (dx < 0 && newPosX < targetX)) {
          posX = (int) targetX;
        } else {
          posX = (int) newPosX;
        }

        if ((dy > 0 && newPosY > targetY) || (dy < 0 && newPosY < targetY)) {
          posY = (int) targetY;
        } else {
          posY = (int) newPosY;
        }

        // Check if the enemy has reached the target position
        if (posX == targetX && posY == targetY) {
          this.stop = true;
        }
      }
    }
  }

  /**
   * Spawns bombs based on the boss's health at random positions on the grid.
   */
  private void spawnBombs() {
    // add a number ranging from 1 to 3 bombs in the array
    int bombs = Utils.rng(3, 13 - health);
    // for each bomb, pick a random position in the grid and add it
    // to tilemanager grid.
    for (int i = 0; i < bombs; i++) {
      int x = Utils.rng(0, 15) * 48;
      int y = Utils.rng(0, 15) * 48;
      if (TileManager.build().grid[y / 48][x / 48] == TileType.Wall
          || TileManager.build().grid[y / 48][x / 48] == TileType.Obstacle) {
        i--;
        continue;
      }
      BombManager.build().addBomb(new Bomb(x, y, 6));
    }
  }

  /**
   * Updates the boss's state, including movement, bomb spawning, and animations.
   * 
   * @param elapsed The elapsed time since the last update.
   */
  @Override
  public void update(int elapsed) {
    sprite.update(elapsed);
    this.move();

    if (hit && !hitFlag) {
      if (this.health > 5) {
        this.sprite.setAnimation("hit-1");
      } else {
        this.sprite.setAnimation("hit-2");
      }
      this.health--;

      if (this.health <= 0) {
        this.die();
      }

      this.hitFlag = true;
      this.spawnBombs();
      Utils.setTimeout(() -> {
        this.hit = false;
        this.hitFlag = false;
        this.stop = false;
        this.speed = 1;
        this.sprite.setAnimation("idle-2");
      }, 5000);
    }
  }

  /**
   * Renders the boss on the graphics context.
   * 
   * @param g2d The graphics context to render on.
   */
  @Override
  public void render(Graphics2D g2d) {
    this.sprite.draw(g2d, this.posX, this.posY, this.width, this.height);
  }
}
