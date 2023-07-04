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

public class Puropen extends Enemy {
	public Puropen(int posX, int posY) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
				new Sprite("enemy-1", 4, 4, "left", new SpriteAnimation[] {
						new SpriteAnimation("down", 4, 0, 3),
						new SpriteAnimation("up", 4, 1, 3),
						new SpriteAnimation("left", 4, 2, 3),
						new SpriteAnimation("right", 4, 3, 3)
				}, 2.5f));
		this.health = 1;
		this.direction = Utils.pick(new String[] { "up", "down", "left", "right" });
		this.sprite.setAnimation(this.direction);
		this.score = 100;
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

	public void update(int elapsed) {
		// the enemy moves in a direction until it hits a wall, then it changes
		// direction
		super.update(elapsed);

		if (this.stop)
			return;

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
		int offsetY = 0;
		if (this.sprite.currentAnimation.name == "explosion") {
			offsetY = -30;
		}
		this.sprite.draw(g2d, posX - 5, posY - 15 + offsetY);
	}
}
