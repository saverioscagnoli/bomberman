package entities.powerups;

import core.Loop;
import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;


public class SpeedPowerup extends PowerUp {

  public SpeedPowerup(int posX, int posY) {
    super("Rollers", posX, posY, new Sprite("Rollers", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();
    Loop.build().bomberman.speed++;
    TileManager.build().grid[this.gridY][this.gridX] = this.prevTile;
  }
}
