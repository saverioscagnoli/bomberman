package entities.bosses;

import java.awt.Graphics2D;

import core.Loop;
import entities.Bomberman;
import entities.Entity;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;

/**
 * The ClownBullet class represents a bullet shot by a clown enemy in the game.
 * It extends the Entity class and defines the behavior and rendering of the
 * bullet.
 */
public class ClownBullet extends Entity {
  private int speedX;
  private int speedY;

  /**
   * Constructs a ClownBullet object with the specified position and direction.
   * 
   * @param posX The x-coordinate of the bullet's position.
   * @param posY The y-coordinate of the bullet's position.
   * @param i    The direction index representing the bullet's movement direction.
   */
  public ClownBullet(int posX, int posY, int i) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 2,
        new Sprite("clown-bullet", 4, 1, "idle", new SpriteAnimation[] {
            new SpriteAnimation("idle", 4, 0, 5)
        }, 1));

    switch (i) {
      case 0:
        this.speedX = 0;
        this.speedY = this.speed;
        break;
      case 1:
        this.speedX = this.speed;
        this.speedY = this.speed;
        break;
      case 2:
        this.speedX = this.speed;
        this.speedY = 0;
        break;
      case 3:
        this.speedX = this.speed;
        this.speedY = -this.speed;
        break;
      case 4:
        this.speedX = 0;
        this.speedY = -this.speed;
        break;
      case 5:
        this.speedX = -this.speed;
        this.speedY = -this.speed;
        break;
      case 6:
        this.speedX = -this.speed;
        this.speedY = 0;
        break;
      case 7:
        this.speedX = -this.speed;
        this.speedY = this.speed;
        break;
    }
  }

  /**
   * Sets the bullet as dead.
   */
  public void die() {
    this.dead = true;
  }

  /**
   * Updates the position and checks for collision with the player character.
   * 
   * @param elapsed The elapsed time since the last update.
   */
  @Override
  public void update(int elapsed) {
    this.sprite.update(elapsed);
    this.posX += this.speedX;
    this.posY += this.speedY;

    Bomberman bomberman = Loop.build().bomberman;

    if (!bomberman.dead && !bomberman.immune && this.posX + this.width > bomberman.posX &&
        this.posX < bomberman.posX + bomberman.width &&
        this.posY + this.height > bomberman.posY &&
        this.posY < bomberman.posY + bomberman.height) {

      bomberman.die();
    }

    if (this.posX < 0 ||
        this.posX > Consts.gridWidth * Consts.tileDims ||
        this.posY < 0 ||
        this.posY > Consts.gridHeight * Consts.tileDims) {
      this.die();
    }
  }

  /**
   * Renders the bullet on the graphics context.
   * 
   * @param g2d The graphics context to render on.
   */
  @Override
  public void render(Graphics2D g2d) {
    this.sprite.draw(g2d, this.posX, this.posY, this.width, this.height);
  }
}
