package entities.powerups;

import core.Loop;
import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;

public class RadiusPowerup extends PowerUp {

  public RadiusPowerup(int posX, int posY) {
    super("radius+1", posX, posY, new Sprite("radius+1", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();
    Loop.build().bomberman.bombRadius++;
    TileManager.build().grid[this.posY / Consts.tileDims][this.posX / Consts.tileDims] = TileType.Empty;
  }
}
