package entities.powerups;

import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.CollisionChecker;
import util.TileType;

public class PassThroughPowerup extends PowerUp {

  public PassThroughPowerup(int posX, int posY) {
    super("pass-through", posX, posY, new Sprite("pass-through", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();
    CollisionChecker.SolidTiles.remove(TileType.Obstacle);
    TileManager.build().grid[this.gridY][this.gridX] = this.prevTile;
  }
}
