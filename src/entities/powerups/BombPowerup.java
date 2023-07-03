package entities.powerups;

import core.Loop;
import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;

public class BombPowerup extends PowerUp {

  public BombPowerup(int posX, int posY) {
    super("bomb+1", posX, posY, new Sprite("bomb+1", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();
    Loop.build().bomberman.maxBombs++;
    TileManager.build().grid[this.gridY][this.gridX] = this.prevTile;
  }
}
