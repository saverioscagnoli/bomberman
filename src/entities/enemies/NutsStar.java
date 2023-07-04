package entities.enemies;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import entities.Enemy;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

public class NutsStar extends Enemy {
  private boolean justChanged;

  public NutsStar(int posX, int posY) {
    super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
        new Sprite("nuts-star", 9, 4, "left", new SpriteAnimation[] {
            new SpriteAnimation("down", 9, 0, 10),
            new SpriteAnimation("up", 9, 1, 10),
            new SpriteAnimation("left", 7, 2, 10),
            new SpriteAnimation("right", 7, 3, 10)
        }, 2.5f));

    this.score = 200;
    this.justChanged = false;
    this.health = 1;
  }

  private boolean collide() {
    TileType[][] grid = TileManager.build().grid;
    HashMap<String, TileType> map = new HashMap<>();

    map.put("up", grid[this.gridY - 1][this.gridX]);
    map.put("down", grid[this.gridY + 1][this.gridX]);
    map.put("left", grid[this.gridY][this.gridX - 1]);
    map.put("right", grid[this.gridY][this.gridX + 1]);

    ArrayList<TileType> solid = new ArrayList<>();
    solid.add(TileType.Obstacle);
    solid.add(TileType.Wall);
    solid.add(TileType.Bomb);
    solid.add(TileType.Enemy);

    return solid.contains(map.get(this.direction));
  }

  private void setDirection(String direction) {
    this.direction = direction;
    this.sprite.setAnimation(this.direction);
    this.justChanged = true;
  }

  private void handleCollisions(String[] directions, boolean check, Runnable fn) {
    if (this.collide()) {
      if (check) {
        this.setDirection(Utils.pick(directions));
      } else {
        fn.run();
      }
    } else if (this.gridX % 2 != 0 && this.gridY % 2 != 0 && !this.justChanged && check) {
      if (Utils.rng(1, 5) == 1) {
        this.setDirection(Utils.pick(directions));
      } else {
        fn.run();
        this.justChanged = true;
      }
    } else {
      fn.run();
    }
  }

  public void update(int elapsed) {
    // the enemy moves in a direction until it hits a wall, then it changes
    // direction
    super.update(elapsed);

    if (this.stop)
      return;

    switch (this.direction) {
      case "up": {
        int edge = this.gridY * Consts.tileDims;
        String[] directions = new String[] { "left", "right", "down" };
        this.handleCollisions(directions, this.posY <= edge, () -> this.posY -= this.speed);
        break;
      }
      case "down": {
        int edge = this.gridY * Consts.tileDims + Consts.tileDims;
        String[] directions = new String[] { "left", "right", "up" };
        this.handleCollisions(directions, this.posY + this.height >= edge, () -> this.posY += this.speed);
        break;
      }
      case "left": {
        int edge = this.gridX * Consts.tileDims;
        String[] directions = new String[] { "up", "down", "right" };
        this.handleCollisions(directions, this.posX <= edge, () -> this.posX -= this.speed);
        break;
      }
      case "right": {
        int edge = this.gridX * Consts.tileDims + Consts.tileDims;
        String[] directions = new String[] { "up", "down", "left" };
        this.handleCollisions(directions, this.posX + this.width >= edge, () -> this.posX += this.speed);
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
      this.justChanged = false;
    }

    if (this.prevTile == TileType.Bomberman) {
      this.prevTile = TileType.Empty;
    }

    tileManager.grid[this.gridY][this.gridX] = TileType.Enemy;
  }

  public void render(Graphics2D g2d) {
    int offsetX = 0;
    int offsetY = 0;

    if (this.sprite.currentAnimation.name == "explosion") {
      offsetX = -5;
      offsetY = -60;
    }
    this.sprite.draw(g2d, posX + 3 + offsetX, posY - 10 + offsetY);
  }
}