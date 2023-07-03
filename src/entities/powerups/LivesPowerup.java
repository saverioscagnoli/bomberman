package entities.powerups;

import core.Loop;
import entities.PowerUp;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
public class LivesPowerup extends PowerUp {

  public LivesPowerup(int posX, int posY) {
    super("lives+1", posX, posY, new Sprite("lives+1", 2, 1, "idle", new SpriteAnimation[] {
        new SpriteAnimation("idle", 2, 0, 3)
    }, 1));
  }

  public void onPickup() {
    this.die();
    Loop.build().bomberman.lives++;
    TileManager.build().grid[this.gridY][this.gridX] = this.prevTile;
    Loop.build().overlay.repaint();
  }
}