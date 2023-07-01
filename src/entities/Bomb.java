package entities;

import java.awt.Graphics2D;
import core.Loop;
import managers.BombManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

public class Bomb extends Entity {
	/* The time that takes for the bomb to explode */
	private static final int DELAY = 3000;

	/* The time in milliseconds when which the bomb was placed */
	private long createdAt;

	/* The time in milliseconds that will set if the game is paused */
	private long pausedAt;

	private int gridX;
	private int gridY;

	public Bomb(int posX, int posY, int bombRadius) {
		/* Pass everything to the entity superclass */
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0,
				new Sprite("bomb", 4, 1, "idle",
						new SpriteAnimation[] { new SpriteAnimation("idle", 4, 0, 10) },
						1));

		/* Set the bomb in the grid */
		this.gridX = posX / Consts.tileDims;
		this.gridY = posY / Consts.tileDims;

		/* Set the bomb properties */
		this.isSolid = true;
		this.createdAt = System.currentTimeMillis();
		this.pausedAt = 0;
	}

	/* Set the paused time of the bomb to now */
	public void pause() {
		this.pausedAt = System.currentTimeMillis();
	}

	/* Compensate the createdAt prop by adding how the time the game was paused */
	public void resume() {
		if (this.pausedAt != 0) {
			this.createdAt += System.currentTimeMillis() - this.pausedAt;
			this.pausedAt = 0;
		}
	}

	/* Detect if the game was paused and return the time accordingly */
	private long getElapsedTime() {
		if (this.pausedAt != 0) {
			return this.pausedAt - this.createdAt;
		} else {
			return System.currentTimeMillis() - this.createdAt;
		}
	}

	/* Makes the bomb explode and resets the tile on the grid */
	public void die() {
		this.explode();
		this.dead = true;
		int i = posY / Consts.tileDims;
		int j = posX / Consts.tileDims;
		TileManager.build().grid[i][j] = TileType.Empty;
	}

	@Override
	public void update(int elapsed) {
		/* If the time from the placing on the bomb is greater than the delay, die */
		if (!this.dead && this.getElapsedTime() >= DELAY) {
			this.die();
			return;
		}
		TileManager.build().grid[this.gridY][this.gridX] = TileType.Bomb;
		this.sprite.update(elapsed);
	}

	public void explode() {
		/* If the bomb already exploded (touched by another bomb) */
		if (this.dead)
			return;
		Explosion[][] explosionMatrix = new Explosion[4][5];
		int r = Loop.build().bomberman.bombRadius;

		Utils.playSound(Consts.soundPath + "bomb-explosion.wav");
		for (int rad = 1; rad < r + 1; rad++) { // for the length of the bomb radius
			int d = rad * Consts.tileDims;
			int[][] coords = {
					{ this.posX - d, this.posY }, // left
					{ this.posX, this.posY - d }, // up
					{ this.posX + d, this.posY }, // right
					{ this.posX, this.posY + d } // down
			};
			Explosion leftEx = null;
			Explosion upEx = null;
			Explosion rightEx = null;
			Explosion downEx = null;

			if (rad == r) {
				leftEx = new Explosion(coords[0][0], coords[0][1], "left", 4);
				upEx = new Explosion(coords[1][0], coords[1][1], "up", 0);
				rightEx = new Explosion(coords[2][0], coords[2][1], "right", 5);
				downEx = new Explosion(coords[3][0], coords[3][1], "down", 3);
			} else {
				leftEx = new Explosion(coords[0][0], coords[0][1], "horizontal", 6);
				upEx = new Explosion(coords[1][0], coords[1][1], "vertical", 2);
				rightEx = new Explosion(coords[2][0], coords[2][1], "horizontal", 6);
				downEx = new Explosion(coords[3][0], coords[3][1], "vertical", 2);
			}

			explosionMatrix[0][rad] = leftEx;
			explosionMatrix[1][rad] = upEx;
			explosionMatrix[2][rad] = rightEx;
			explosionMatrix[3][rad] = downEx;

			Explosion centralEx = new Explosion(this.posX, this.posY, "central", 1);
			BombManager.build().addExplosion(centralEx);
		}

		// adds explosions in explosion matrix to entities
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < r + 1; j++) {
				Explosion ex = explosionMatrix[i][j];
				if (ex != null) {
					int x = ex.posX / Consts.tileDims;
					int y = ex.posY / Consts.tileDims;
					TileType tile = TileManager.build().grid[y][x];
					if (tile != TileType.Wall) {
						/* If the tile was a an obstacle, find the instance and destroy it */
						if (tile == TileType.Obstacle) {
							TileManager.build().walls
									.stream()
									.filter((w) -> w.posX == ex.posX && w.posY == ex.posY)
									.findFirst()
									.ifPresent(w -> w.sprite.setAnimation("death"));

							/* Set the grid position to empty */
							TileManager.build().grid[y][x] = TileType.Empty;
							break;
						} else {
							BombManager.build().addExplosion(ex);
							/* If the tile was a bomb, find the instance and destroy it */
							if (tile == TileType.Bomb) {
								BombManager
										.build().bombs
										.stream()
										.filter(b -> b.posX == ex.posX && b.posY == ex.posY)
										.findFirst()
										.ifPresent(b -> Utils.setTimeout(() -> b.die(), 100));
							}
						}
					} else {
						break;
					}
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		this.sprite.draw(g2d, posX, posY, this.width, this.height);
	}
}
