package entities.bosses;

import java.awt.Graphics2D;
import java.util.ArrayList;

import core.Loop;
import entities.Bomberman;
import entities.Enemy;
import entities.Tile;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

/**
 * The ClownMask class represents the clown boss in super bomberman.
 * It extends the Enemy class and defines the behavior and rendering of the
 * clown.
 */
public class ClownMask extends Enemy {
  public boolean stop;
  public boolean hit;
  private boolean hitFlag;
  private ArrayList<ClownBullet> bullets;

  /**
   * Constructs a ClownMask object with the specified position.
   * 
   * @param posX The x-coordinate of the clown's position.
   * @param posY The y-coordinate of the clown's position.
   */
  public ClownMask(int posX, int posY) {
    super(posX, posY, 0, 0, 1,
        new Sprite("clown-mask", 4, 4, "idle-1", new SpriteAnimation[] {
            new SpriteAnimation("idle-1", 2, 0, 50),
            new SpriteAnimation("hit-1", 2, 1, 3),
            new SpriteAnimation("hit-2", 2, 2, 3),
            new SpriteAnimation("idle-2", 4, 3, 50)
        }, 2.5f));

    this.health = 2;
    this.speed = 1;
    this.stop = false;
    this.width = (int) (this.sprite.width * this.sprite.scale);
    this.height = (int) (this.sprite.height * this.sprite.scale);
    this.hit = false;
    this.hitFlag = false;
    this.bullets = new ArrayList<>();
  }

  /**
   * Sets the clown as dead and reveals the hatch tile.
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
   * Moves the clown towards the player character's position.
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

        if (Math.sqrt(dx * dx + dy * dy) < 100) {
          if (!bomberman.dead && !bomberman.immune) {
            bomberman.die();
          }
        }

        double offsetX = (dx / maxAbs) * this.speed;
        double offsetY = (dy / maxAbs) * this.speed;

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
   * Spawns bullets from the clown in different directions.
   */
  private void spawnBullets() {
    int offsetX;
    int offsetY;
    final int OFFSET_1 = 20;
    final int OFFSET_2 = 50;
    for (int i = 0; i < 8; i++) {
      int x = (int) (this.posX + this.width * 0.5 - Consts.tileDims * 0.5);
      int y = (int) (this.posY + this.height * 0.5 - Consts.tileDims * 0.5);

      switch (i) {
        case 0:
          offsetX = 0;
          offsetY = OFFSET_2;
          break;
        case 1:
          offsetX = OFFSET_1;
          offsetY = -OFFSET_1;
          break;
        case 2:
          offsetX = OFFSET_2;
          offsetY = 0;
          break;
        case 3:
          offsetX = OFFSET_1;
          offsetY = OFFSET_1;
          break;
        case 4:
          offsetX = 0;
          offsetY = -OFFSET_2;
          break;
        case 5:
          offsetX = -OFFSET_1;
          offsetY = OFFSET_1;
          break;
        case 6:
          offsetX = -OFFSET_2;
          offsetY = 0;
          break;
        case 7:
          offsetX = -OFFSET_1;
          offsetY = -OFFSET_1;
          break;
        default:
          offsetX = 0;
          offsetY = 0;
          break;
      }
      this.bullets.add(new ClownBullet(x + offsetX, y + offsetY, i));
    }
  }

  /**
   * Updates the bullets' positions and removes dead bullets.
   * 
   * @param elapsed The elapsed time since the last update.
   */
  private void updateBullets(int elapsed) {
    ArrayList<ClownBullet> toRemove = new ArrayList<>();

    for (ClownBullet b : this.bullets) {
      if (b.dead) {
        toRemove.add(b);
      } else {
        b.update(elapsed);
      }
    }

    toRemove.forEach(b -> this.bullets.remove(b));
  }

  /**
   * Draws the bullets on the graphics context.
   * 
   * @param g2d The graphics context to render on.
   */
  private void drawBullets(Graphics2D g2d) {
    for (ClownBullet b : this.bullets) {
      if (!b.dead) {
        b.render(g2d);
      }
    }
  }

  /**
   * Updates the clown's state, including movement, bullet spawning, and
   * animations.
   * 
   * @param elapsed The elapsed time since the last update.
   */
  @Override
  public void update(int elapsed) {
    sprite.update(elapsed);
    this.move();
    this.updateBullets(elapsed);

    if (hit && !hitFlag) {
      if (this.health == 18) {
        this.sprite.setAnimation("hit-1");
      } else {
        this.sprite.setAnimation("hit-2");
      }
      this.health--;

      if (this.health <= 0) {
        this.die();
      }

      this.hitFlag = true;
      this.spawnBullets();
      Utils.setTimeout(() -> {
        this.hit = false;
        this.hitFlag = false;
        this.stop = false;
        this.sprite.setAnimation("idle-2");
      }, 2000);
    }
  }

  /**
   * Renders the clown and its bullets on the graphics context.
   * 
   * @param g2d The graphics context to render on.
   */
  @Override
  public void render(Graphics2D g2d) {
    this.drawBullets(g2d);
    this.sprite.draw(g2d, this.posX, this.posY, this.width, this.height);
  }
}
