package entities.enemies;

import java.awt.Graphics2D;
import entities.Enemy;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

public class Cuppen extends Enemy {
  public Cuppen(int posX, int posY) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
        new Sprite("cuppen", 8, 1, "idle", new SpriteAnimation[] {
            new SpriteAnimation("idle", 8, 0, 15),
        }, 2.5f));

    this.health = 1;
    this.direction = Utils.pick(new String[] { "up", "down", "left", "right" });
    this.sprite.setAnimation(this.direction);
    this.gridX = posX / Consts.tileDims;
    this.gridY = posY / Consts.tileDims;
    this.score = 400;
    this.health = 2;
  }

  public void update(int elapsed) {
    // the enemy moves in a direction until it hits a wall, then it changes
    // direction
    super.update(elapsed);

    if (this.stop)
      return;

    this.move(true);
    this.updateGrid();
  }

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
