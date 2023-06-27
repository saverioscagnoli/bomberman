package entities;

import java.awt.Graphics2D;
import managers.TileManager;
import ui.Sprite;
import util.Consts;
import util.TileType;
import util.Utils;

public class Tile extends Entity {
  /* A flag to determine if the tile is destructable */
  public boolean destructable;

  /* A flag to determine if the tile is static and must have an animation */
  public boolean isStatic = false;

  /* Constructor for tiles with animations */
  public Tile(int posX, int posY, boolean isSolid, boolean isStatic, boolean destructable, Sprite sprite) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, sprite);
    this.isSolid = isSolid;
    this.isStatic = isStatic;
    this.destructable = destructable;
  }

  /* Constructor for tiles without animations */
  public Tile(int posX, int posY, boolean isSolid, String spriteName) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, new Sprite(spriteName, 1, 1, null, null, 1));
    this.isSolid = isSolid;
    this.isStatic = true;
    this.destructable = false;
  }

  public void update(int elapsed) {
    if (!isStatic) {
      this.sprite.update(elapsed);
    }
  }

  /* THIS METHOD IS USED ONLY ON DESTRUCTABLE TILES */
  @Override
  public void die() {
    this.dead = true;
    /*
     * If the tile under the obstacle is normal, reset the sprite (it had a shadow
     * before)
     */
    int x = this.posX / Consts.tileDims;
    int y = this.posY / Consts.tileDims;
    TileType[][] grid = TileManager.build().grid;
    if (y + 1 < grid.length && grid[y + 1][x] == TileType.Empty) {
      TileManager.build().basicTiles
          .stream()
          .filter(t -> t.posX == this.posX && t.posY == this.posY + Consts.tileDims)
          .findFirst()
          .ifPresent(t -> t.sprite.spritesheet = Utils.loadImage("assets/tiles/basic-1.png"));
    }
  }

  @Override
  public void render(Graphics2D g2d) {
    if (this.isStatic) {
      g2d.drawImage(this.sprite.spritesheet, this.posX, this.posY, this.width, this.height, null);
    } else {
      if (this.sprite.currentAnimation.name == "death" && this.sprite.current == 5) {
        this.die();
      }
      this.sprite.draw(g2d, posX, posY, Consts.tileDims, Consts.tileDims);
    }
  }
}
