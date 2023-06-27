package entities.bosses;

import java.awt.Graphics2D;

import core.Loop;
import entities.Bomberman;
import entities.Enemy;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

public class ClownMask extends Enemy {
  private double dirX;
  private double dirY;
  private Bomberman bomberman;

  public ClownMask(int posX, int posY) {
    super(posX, posY, Consts.tileDims * 5, Consts.tileDims * 5, 1,
        new Sprite("clown-mask", 2, 1, "idle", new SpriteAnimation[] {
            new SpriteAnimation("idle", 2, 0, 50)
        }, 2.5f));
  }

  public void die() {
    this.dead = true;
  }

  @Override
  public void update(int elapsed) {
    this.sprite.update(elapsed);

    Bomberman bomberman = Loop.build().bomberman;

    this.posX = (int) (Utils.lerp(this.posX, bomberman.posX, 0.008));
    this.posY = (int) (Utils.lerp(this.posY, bomberman.posY, 0.008));

    /*
     * int dX = bomberman.posX - this.posX;
     * int dY = bomberman.posY - this.posY;
     */
  }

  @Override
  public void render(Graphics2D g2d) {
    this.sprite.draw(g2d, posX, posY);
  }
}
