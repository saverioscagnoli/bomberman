package entities.powerups;

import core.Loop;
import entities.Enemy;
import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;

public class RainPowerup extends PowerUp {

  public RainPowerup(int posX, int posY) {
    super("AcidRain", posX, posY, new Sprite("AcidRain", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();

    for (Enemy e : Loop.build().enemyManager.enemies) {
      e.dealDamage(1);

    }

    TileManager.build().grid[this.posY / Consts.tileDims][this.posX / Consts.tileDims] = TileType.Empty;
  }
}
