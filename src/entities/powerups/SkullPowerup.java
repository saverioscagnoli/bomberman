package entities.powerups;

import core.Loop;
import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

public class SkullPowerup extends PowerUp {
  public SkullPowerup(int posX, int posY) {
    super("Skull", posX, posY, new Sprite("Skull", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();
    Loop.build().bomberman.bombRadius += 1;
    Loop.build().bomberman.speed += 1;
    Loop.build().bomberman.maxBombs += 1;
    Loop.build().bomberman.lives -= 1;
    TileManager.build().grid[this.posY / Consts.tileDims][this.posX / Consts.tileDims] = TileType.Empty;
  }
}
