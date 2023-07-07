package entities;

import java.awt.Graphics2D;

import managers.PowerupManager;
import managers.SaveManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

public class Tile extends Entity {
  /* A flag to determine if the tile is destructable */
  public boolean destructable;

  /* A flag to determine if the tile is static and must have an animation */
  public boolean isStatic = false;

  private boolean isHatch;
  public boolean isVisible;

  private int gridX;
  private int gridY;

  /* Constructor for tiles with animations */
  public Tile(int posX, int posY, boolean isSolid, boolean isStatic, boolean destructable, Sprite sprite) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, sprite);
    this.isSolid = isSolid;
    this.isStatic = isStatic;
    this.destructable = destructable;
    this.isHatch = false;
  }

  /* Constructor for tiles without animations */
  public Tile(int posX, int posY, boolean isSolid, String spriteName) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0, new Sprite(spriteName, 1, 1, null, null, 1));
    this.isSolid = isSolid;
    this.isStatic = true;
    this.destructable = false;
    this.isHatch = false;
  }

  /* Consttructor for the hatch */
  public Tile(int posX, int posY) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 0,
        new Sprite("hatch", 2, 1, "idle", new SpriteAnimation[] {
            new SpriteAnimation("idle", 2, 0, 2)
        }, 1));
    this.isSolid = false;
    this.isStatic = false;
    this.destructable = false;
    this.isHatch = true;

    this.gridX = posX / Consts.tileDims;
    this.gridY = posY / Consts.tileDims;
    this.isVisible = false;
  }

  public void update(int elapsed) {
    this.sprite.update(elapsed);

    if (this.isHatch) {
      TileManager tileManager = TileManager.build();
      if (tileManager.grid[this.gridY][this.gridX] == TileType.Empty) {
        tileManager.grid[this.gridY][this.gridX] = TileType.Hatch;
        if (!this.isVisible) {
          this.isVisible = true;
        }
      } else {
        if (!this.isVisible) {
          for (int i = 0; i < Consts.gridHeight; i++) {
            for (int j = 0; j < Consts.gridWidth; j++) {
              if (tileManager.grid[i][j] == TileType.Hatch) {
                String lvl = SaveManager.readProgress().get("level");
                String path = (lvl.equals("3") || lvl.equals("6")) ? "bosslevel.lvl" : "level-" + lvl + ".lvl";
                TileType[][] grid = Utils.readLevel("levels/" + path);
                tileManager.grid[i][j] = grid[i][j];
              }
            }
          }
        }
      }
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
    TileManager tileManager = TileManager.build();
    TileType[][] grid = tileManager.grid;
    if (y + 1 < grid.length && grid[y + 1][x] == TileType.Empty) {
      TileManager.build().basicTiles
          .stream()
          .filter(t -> t.posX == this.posX && t.posY == this.posY + Consts.tileDims)
          .findFirst()
          .ifPresent(t -> t.sprite.spritesheet = Utils.loadImage("assets/tiles/basic-1.png"));
    }

    if (tileManager.hatch.posX == this.posX && tileManager.hatch.posY == this.posY) {
      tileManager.grid[y][x] = TileType.Hatch;
    }

    /* Spawn a random powerup */
    if (Utils.rng(1, 10) <= 1) {
      /* Get a randon powerup class */
      Class<?> c = Utils.pick(PowerupManager.build().classes);
      PowerUp p = null;
      try {
        /* Instanciate the random class */
        p = (PowerUp) c.getDeclaredConstructor(int.class, int.class).newInstance(this.posX, this.posY);
      } catch (Exception e) {
        e.printStackTrace();
      }
      /* Add the powerups */
      PowerupManager.build().powerups.add(p);
      TileManager.build().grid[y][x] = TileType.PowerUp;
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
