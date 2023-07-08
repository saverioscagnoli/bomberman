package entities;

import java.awt.Graphics2D;
import managers.BombManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.CollisionChecker;
import util.Consts;
import util.TileType;
import util.Utils;

/**
 * The `Bomb` class represents a bomb entity in a game. It extends the `Entity`
 * class.
 * A bomb has a specific delay before it explodes and destroys surrounding
 * entities.
 * It can be paused and resumed, and it maintains information about its position
 * in the game grid.
 */
public class Bomb extends Entity {

	/* The time that takes for the bomb to explode */
	private static final int DELAY = 3000;

	/* The time in milliseconds when the bomb was placed */
	private long createdAt;
	private int bombRadius = 1;

	/* The time in milliseconds that will be set if the game is paused */
	private long pausedAt;

	public int gridX;
	public int gridY;

	/**
	 * Constructs a new Bomb object with the specified position and bomb radius.
	 *
	 * @param posX       The X-coordinate of the bomb's position.
	 * @param posY       The Y-coordinate of the bomb's position.
	 * @param bombRadius The radius of the bomb's explosion.
	 */
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
		this.bombRadius = bombRadius;
	}

	/**
	 * Sets the paused time of the bomb to the current time.
	 * This method is used to pause the bomb's timer.
	 */
	public void pause() {
		this.pausedAt = System.currentTimeMillis();
	}

	/**
	 * Compensates the `createdAt` property by adding the time the game was paused.
	 * This method is used to resume the bomb's timer after it was paused.
	 */
	public void resume() {
		if (this.pausedAt != 0) {
			this.createdAt += System.currentTimeMillis() - this.pausedAt;
			this.pausedAt = 0;
		}
	}

	/**
	 * Calculates the elapsed time since the bomb was created or paused.
	 *
	 * @return The elapsed time in milliseconds.
	 */
	private long getElapsedTime() {
		if (this.pausedAt != 0) {
			return this.pausedAt - this.createdAt;
		} else {
			return System.currentTimeMillis() - this.createdAt;
		}
	}

	/**
	 * Makes the bomb explode and resets the tile on the grid.
	 * This method is called when the bomb's timer exceeds the specified delay.
	 */
	public void die() {
		this.explode();
		this.dead = true;
		TileManager.build().grid[this.gridY][this.gridX] = TileType.Empty;
	}

	/**
	 * Marks the bomb as dead without triggering an explosion.
	 */
	public void dieNotExplode() {
		this.dead = true;
		TileManager.build().grid[this.gridY][this.gridX] = TileType.Empty;
	}

	@Override
	public void update(int elapsed) {
		/* If the time from placing the bomb is greater than the delay, die */
		if (!this.dead && this.getElapsedTime() >= DELAY) {
			this.die();
			return;
		}
		TileManager.build().grid[this.gridY][this.gridX] = TileType.Bomb;
		this.sprite.update(elapsed);
	}

	/**
	 * Triggers the explosion of the bomb, causing damage to surrounding entities.
	 * This method creates explosion objects and destroys obstacles and bombs if
	 * encountered.
	 */
	public void explode() {
		/* If the bomb has already exploded (touched by another bomb) */
		if (this.dead)
			return;
		Explosion[][] explosionMatrix = new Explosion[4][10];
		int r = this.bombRadius;

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
						/* If the tile was an obstacle, find the instance and destroy it */
						if (tile == TileType.Obstacle) {
							TileManager.build().walls
									.stream()
									.filter((w) -> w.posX == ex.posX && w.posY == ex.posY)
									.findFirst()
									.ifPresent(w -> w.sprite.setAnimation("death"));

							/* Set the grid position to empty */
							TileManager.build().grid[y][x] = TileType.Empty;
							CollisionChecker.build().update_Centered_Collisions();
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
