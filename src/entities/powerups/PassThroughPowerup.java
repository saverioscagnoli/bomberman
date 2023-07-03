package entities.powerups;

import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.CollisionChecker;
import util.Consts;
import util.TileType;

public class PassThroughPowerup extends PowerUp {

  public PassThroughPowerup(int posX, int posY) {
    super("pass-through", posX, posY, new Sprite("pass-through", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();
    CollisionChecker.SolidTiles = new TileType[] { TileType.Bomb, TileType.Wall };
    TileManager.build().grid[this.posY / Consts.tileDims][this.posX / Consts.tileDims] = TileType.Empty;
  }
}
