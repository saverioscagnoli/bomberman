package entities.powerups;

import core.Loop;
import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Utils;

public class VestPowerup extends PowerUp {
  public VestPowerup(int posX, int posY) {
    super("Vest", posX, posY, new Sprite("Vest", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();
    Loop.build().bomberman.immune = true;
    Utils.setTimeout(() -> {
      Loop.build().bomberman.immune = false;
    }, 8000);
    TileManager.build().grid[this.gridY][this.gridX] = this.prevTile;
  }
}
