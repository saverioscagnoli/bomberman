package entities.enemies;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import entities.Enemy;
import managers.BombManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

public class Pakupa extends Enemy {
  public Pakupa(int posX, int posY) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
        new Sprite("pakupa", 10, 4, "left", new SpriteAnimation[] {
            new SpriteAnimation("down", 10, 0, 10),
            new SpriteAnimation("up", 4, 1, 10),
            new SpriteAnimation("left", 6, 2, 10),
            new SpriteAnimation("right", 6, 3, 10)
        }, 2.5f));
    this.health = 3;
    this.direction = Utils.pick(new String[] { "up", "down", "left", "right" });
    this.sprite.setAnimation(this.direction);
    this.score = 400;
  }

  private boolean collide() {
    TileType[][] grid = TileManager.build().grid;
    HashMap<String, int[]> map = new HashMap<>();

    map.put("up", new int[] { this.gridY - 1, this.gridX });
    map.put("down", new int[] { this.gridY + 1, this.gridX });
    map.put("left", new int[] { this.gridY, this.gridX - 1 });
    map.put("right", new int[] { this.gridY, this.gridX + 1 });

    ArrayList<TileType> solid = new ArrayList<>();
    solid.add(TileType.Obstacle);
    solid.add(TileType.Wall);
    solid.add(TileType.Bomb);
    solid.add(TileType.Enemy);

    int[] nextCoords = map.get(this.direction);

    if (grid[nextCoords[0]][nextCoords[1]] == TileType.Bomb) {
      BombManager.build().bombs
          .stream()
          .forEach(bomb -> {
            if (bomb.gridX == nextCoords[1] && bomb.gridY == nextCoords[0]) {
              bomb.dieNotExplode();
            }
          });
    }

    return solid.contains(grid[nextCoords[0]][nextCoords[1]]);
  }

  private void checkBombs() {
    // TODO: fix that the enemy sees the bombs through walls
    TileManager tileManager = TileManager.build();
    TileType[][] grid = tileManager.grid;

    for (int i = this.gridX; i < grid.length; i++) {
      if (grid[this.gridY][i] == TileType.Bomb) {
        this.direction = "right";
        if (this.sprite.currentAnimation.name != this.direction) {
          this.sprite.setAnimation(this.direction);
        }
        return;
      } else if (grid[this.gridY][i] != TileType.Empty) {
        continue;
      }
    }
    for (int i = this.gridX; i >= 0; i--) {
      if (grid[this.gridY][i] == TileType.Bomb) {
        this.direction = "left";
        if (this.sprite.currentAnimation.name != this.direction) {
          this.sprite.setAnimation(this.direction);
        }
        return;
      } else if (grid[this.gridY][i] != TileType.Empty) {
        continue;
      }
    }
    for (int i = this.gridY; i < grid.length; i++) {
      if (grid[i][this.gridX] == TileType.Bomb) {
        this.direction = "down";
        if (this.sprite.currentAnimation.name != this.direction) {
          this.sprite.setAnimation(this.direction);
        }
        return;
      } else if (grid[i][this.gridX] != TileType.Empty) {
        continue;
      }
    }
    for (int i = this.gridY; i >= 0; i--) {
      if (grid[i][this.gridX] == TileType.Bomb) {
        this.direction = "up";
        if (this.sprite.currentAnimation.name != this.direction) {
          this.sprite.setAnimation(this.direction);
        }
        return;
      } else if (grid[i][this.gridX] != TileType.Empty) {
        continue;
      }
    }
  }

  public void update(int elapsed) {
    // the enemy moves in a direction until it hits a wall, then it changes
    // direction
    super.update(elapsed);

    if (this.stop)
      return;
    this.checkBombs();
    switch (this.direction) {
      case "up": {
        if (this.collide()) {
          int edge = this.gridY * Consts.tileDims;
          if (this.posY <= edge) {
            this.direction = Utils.pick(new String[] { "left", "right", "down" });
            this.sprite.setAnimation(this.direction);
          } else {
            this.posY -= this.speed;
          }
        } else {
          this.posY -= this.speed;
        }
        break;
      }
      case "down": {
        if (this.collide()) {
          int edge = this.gridY * Consts.tileDims + Consts.tileDims;
          if (this.posY + this.height >= edge) {
            this.direction = Utils.pick(new String[] { "left", "right", "up" });
            this.sprite.setAnimation(this.direction);
          } else {
            this.posY += this.speed;
          }
        } else {
          this.posY += this.speed;
        }
        break;
      }
      case "left": {
        if (this.collide()) {
          int edge = this.gridX * Consts.tileDims;
          if (this.posX <= edge) {
            this.direction = Utils.pick(new String[] { "up", "down", "right" });
            this.sprite.setAnimation(this.direction);
          } else {
            this.posX -= this.speed;
          }
        } else {
          this.posX -= this.speed;
        }
        break;
      }
      case "right": {
        if (this.collide()) {
          int edge = this.gridX * Consts.tileDims + Consts.tileDims;
          if (this.posX + this.width >= edge) {
            this.direction = Utils.pick(new String[] { "up", "down", "left" });
            this.sprite.setAnimation(this.direction);
          } else {
            this.posX += this.speed;
          }
        } else {
          this.posX += this.speed;
        }
        break;
      }
    }

    TileManager tileManager = TileManager.build();

    int prevX = this.gridX;
    int prevY = this.gridY;
    int x = (int) (this.posX + this.width * 0.5);
    int y = (int) (this.posY + this.height * 0.5);
    int[] normPos = Utils.normalizePos(x, y);

    this.gridX = normPos[0] / Consts.tileDims;
    this.gridY = normPos[1] / Consts.tileDims;

    if (prevX != this.gridX || prevY != this.gridY) {
      if (tileManager.grid[prevY][prevX] == TileType.Enemy) {
        tileManager.grid[prevY][prevX] = this.prevTile;
      }
      this.prevTile = tileManager.grid[this.gridY][this.gridX];
    }

    if (this.prevTile == TileType.Bomberman) {
      this.prevTile = TileType.Empty;
    }

    tileManager.grid[this.gridY][this.gridX] = TileType.Enemy;

  }

  public void render(Graphics2D g2d) {
    /*
     * int offsetY = 0;
     * if (this.sprite.currentAnimation.name == "explosion") {
     * offsetY = -30;
     * }
     */
    this.sprite.draw(g2d, posX + 3, posY);
  }
}
