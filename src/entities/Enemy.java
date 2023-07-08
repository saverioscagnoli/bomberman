package entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import core.Loop;
import entities.enemies.Cuppen;
import entities.enemies.Pakupa;
import managers.BombManager;
import managers.PowerupManager;
import managers.SaveManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

/**
 * 
 * The Enemy class represents an abstract enemy entity in the game.
 * 
 * It extends the Entity class and provides common functionality and attributes
 * 
 * for different types of enemies.
 */
public abstract class Enemy extends Entity {

	/**
	 * 
	 * The direction of the enemy.
	 */
	protected String direction;
	/**
	 * 
	 * The x-coordinate of the enemy in the grid.
	 */
	protected int gridX;
	/**
	 * 
	 * The y-coordinate of the enemy in the grid.
	 */
	protected int gridY;
	/**
	 * 
	 * The health points of the enemy.
	 */
	public int health;
	/**
	 * 
	 * A flag to determine if the enemy is immune and will not take damage.
	 */
	public boolean immune = false;
	/**
	 * 
	 * A flag to determine if the enemy is stopped.
	 */
	protected boolean stop = false;
	/**
	 * 
	 * The score obtained for defeating the enemy.
	 */
	protected int score;
	/**
	 * 
	 * The previous tile type occupied by the enemy.
	 */
	protected TileType prevTile = TileType.Empty;
	/**
	 * 
	 * The horizontal speed of the enemy.
	 */
	protected int speedX;
	/**
	 * 
	 * The vertical speed of the enemy.
	 */
	protected int speedY;

	/**
	 * 
	 * Constructs an enemy object with the specified position, dimensions, speed,
	 * and sprite.
	 * 
	 * @param posX   The x-coordinate of the enemy's position.
	 * 
	 * @param posY   The y-coordinate of the enemy's position.
	 * 
	 * @param width  The width of the enemy.
	 * 
	 * @param height The height of the enemy.
	 * 
	 * @param speed  The speed of the enemy.
	 * 
	 * @param sprite The sprite representing the enemy.
	 */
	public Enemy(int posX, int posY, int width, int height, int speed, Sprite sprite) {
		super(posX, posY, width, height, speed, sprite);
		this.health = 3;

		// sets a random direction between right, left, up, down
		String[] directions = { "right", "left", "up", "down" };
		this.direction = Utils.pick(directions);
		this.stop = false;

		this.gridX = posX / Consts.tileDims;
		this.gridY = posY / Consts.tileDims;
		this.speedX = 0;
		this.speedY = 0;
	}

	/**
	 * 
	 * Handles the death of the enemy.
	 * Sets the enemy as dead, updates the grid tile, and adds a random powerup if
	 * applicable.
	 */
	public void die() {
		this.dead = true;
		TileManager.build().grid[this.gridY][this.gridX] = TileType.Empty;
		if (Utils.rng(1, 10) <= 3) {
			// Get a random powerup class
			Class<?> c = Utils.pick(PowerupManager.build().classes);
			PowerUp p = null;
			int[] normPosition = Utils.normalizePos(this.posX, this.posY);
			try {
				// Instantiate the random class
				p = (PowerUp) c.getDeclaredConstructor(int.class, int.class).newInstance(normPosition[0], normPosition[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Add the powerup
			PowerupManager.build().powerups.add(p);
			TileManager.build().grid[normPosition[1] / 48][normPosition[0] / 48] = TileType.PowerUp;
		}
	}

	/**
	 * 
	 * Deals damage to the enemy.
	 * If the enemy is immune or already in the explosion animation, no damage is
	 * taken.
	 * If the enemy's health reaches zero, it is stopped and the explosion animation
	 * is triggered.
	 * 
	 * @param damage The amount of damage to be dealt.
	 */
	public void dealDamage(int damage) {
		if (immune || this.sprite.currentAnimation.name.equals("explosion"))
			return;
		health -= damage;
		immune = true;
		Utils.setTimeout(() -> immune = false, 100);
		System.out.println(this + " health: " + health);
		if (health <= 0) {
			this.stop = true;
			Utils.setTimeout(() -> {
				Utils.playSound("assets/sounds/enemy-dies.wav");
				this.sprite = new Sprite("enemy-explosion", 7.9, 1, "explosion", new SpriteAnimation[] {
						new SpriteAnimation("explosion", 7, 0, 7)
				}, 2.5f);
				Enemy e = (Enemy) this;
				Loop.build().bomberman.score += e.score;
				SaveManager.incrementScore(e.score);
				Loop.build().overlay.repaint();
			}, 1000);
		}
	}

	/**
	 * 
	 * Updates the enemy's grid position and handles tile changes.
	 * 
	 * Optionally executes additional actions on tile change.
	 * 
	 * @param onTileChange Optional actions to be executed on tile change.
	 */
	protected void updateGrid(Runnable... onTileChange) {
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
			if (onTileChange.length > 0) {
				onTileChange[0].run();
			}

			this.prevTile = tileManager.grid[this.gridY][this.gridX];
		}

		if (this.prevTile == TileType.Bomberman) {
			this.prevTile = TileType.Empty;
		}

		tileManager.grid[this.gridY][this.gridX] = TileType.Enemy;
	}

	/**
	 * 
	 * Checks if the enemy collides with the specified directions.
	 * 
	 * @param dir The directions to check for collision.
	 * 
	 * @return true if collision occurs, false otherwise.
	 */
	protected boolean collide(String... dir) {
		TileType[][] grid = TileManager.build().grid;
		HashMap<String, TileType> map = new HashMap<>();

		map.put("up", grid[this.gridY - 1][this.gridX]);
		map.put("down", grid[this.gridY + 1][this.gridX]);
		map.put("left", grid[this.gridY][this.gridX - 1]);
		map.put("right", grid[this.gridY][this.gridX + 1]);

		ArrayList<TileType> solid = new ArrayList<>();
		solid.add(TileType.Wall);
		solid.add(TileType.Bomb);
		solid.add(TileType.Enemy);

		if (!(this instanceof Cuppen)) {
			solid.add(TileType.Obstacle);
		}

		if (this instanceof Pakupa) {
			TileType next = map.get(this.direction);
			if (next == TileType.Bomb) {
				int x = 0;
				int y = 0;

				switch (this.direction) {
					case "up":
						y = -1;
						break;
					case "down":
						y = 1;
						break;
					case "left":
						x = -1;
						break;
					case "right":
						x = 1;
						break;
				}

				for (int i = 0; i < BombManager.build().bombs.size(); i++) {
					Bomb bomb = BombManager.build().bombs.get(i);
					System.out.println(x + " " + y);
					if (bomb.gridX == this.gridX + x && bomb.gridY == this.gridY + y) {
						bomb.dieNotExplode();
					}
				}
			}
		}

		return solid.contains(map.get(dir.length > 0 ? dir[0] : this.direction));
	}

	/**
	 * 
	 * Checks if the enemy is blocked in all four directions.
	 * 
	 * @return true if the enemy is blocked in all directions, false otherwise.
	 */
	protected boolean checkBlocked() {
		String[] dirs = { "up", "down", "left", "right" };
		for (int i = 0; i < dirs.length; i++) {
			if (!this.collide(dirs[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * Moves the enemy based on its current direction and handles collision and
	 * direction changes.
	 * 
	 * @param randomDirection Whether to choose a random direction if the enemy is
	 *                        stopped.
	 */
	protected void move(boolean randomDirection) {
		this.speedX = 0;
		this.speedY = 0;
		if (this.checkBlocked()) {
			this.stop = true;
		} else if (this.stop) {
			this.direction = Utils.pick(new String[] { "up", "down", "left", "right" });
			int i = 0;
			while (this.collide(this.direction) && i < Consts.maxIterations) {
				this.direction = Utils.pick(new String[] { "up", "down", "left", "right" });
				i++;
			}
			this.sprite.setAnimation(this.direction);
			this.stop = false;
		}

		if (this.stop) {
			return;
		}

		switch (this.direction) {
			case "up": {
				int edge = this.gridY * Consts.tileDims;
				if (this.collide() && this.posY <= edge) {
					if (randomDirection) {
						String[] dirs = { "down", "left", "right" };
						this.direction = Utils.pick(dirs);
						int i = 0;

						while (this.collide(this.direction) && i < Consts.maxIterations) {
							this.direction = Utils.pick(dirs);
							if (i == 50) {
								dirs = new String[] { "down", "left", "right", "up" };
							}
							i++;
						}

					} else {
						this.direction = "down";
					}
					this.sprite.setAnimation(this.direction);
				} else {
					this.speedY = -this.speed;
				}
				break;
			}
			case "down": {
				int edge = this.gridY * Consts.tileDims + Consts.tileDims;
				if (this.collide() && this.posY + this.height >= edge) {
					if (randomDirection) {
						String[] dirs = { "up", "left", "right" };
						this.direction = Utils.pick(dirs);
						int i = 0;

						while (this.collide(this.direction) && i < Consts.maxIterations) {
							this.direction = Utils.pick(dirs);
							if (i == 50) {
								dirs = new String[] { "down", "left", "right", "up" };
							}
							i++;
						}

					} else {
						this.direction = "up";
					}
					this.sprite.setAnimation(this.direction);
				} else {
					this.speedY = this.speed;
				}
				break;
			}
			case "left": {
				int edge = this.gridX * Consts.tileDims;
				if (this.collide() && this.posX <= edge) {

					if (randomDirection) {
						String[] dirs = { "up", "down", "right" };
						this.direction = Utils.pick(dirs);
						int i = 0;

						while (this.collide(this.direction) && i < Consts.maxIterations) {
							this.direction = Utils.pick(dirs);
							if (i == 50) {
								dirs = new String[] { "down", "left", "right", "up" };
							}
							i++;
						}
					} else {
						this.direction = "right";
					}

					this.sprite.setAnimation(this.direction);
				} else {
					this.speedX = -this.speed;
				}
				break;
			}
			case "right": {
				int edge = this.gridX * Consts.tileDims + Consts.tileDims;
				if (this.collide() && this.posX + this.width >= edge) {

					if (randomDirection) {
						String[] dirs = { "up", "down", "left" };
						this.direction = Utils.pick(dirs);

						int i = 0;

						while (this.collide(this.direction) && i < Consts.maxIterations) {
							this.direction = Utils.pick(dirs);
							if (i == 50) {
								dirs = new String[] { "down", "left", "right", "up" };
							}
							i++;
						}

					} else {
						this.direction = "left";
					}

					this.sprite.setAnimation(this.direction);
				} else {
					this.speedX = this.speed;
				}
				break;
			}
		}

		this.posX += this.speedX;
		this.posY += this.speedY;
	}

	/**
	 * 
	 * Updates the enemy's state and handles collision with the player.
	 * 
	 * @param elapsed The elapsed time since the last update.
	 */
	public void update(int elapsed) {
		Bomberman bomberman = Loop.build().bomberman;

		if (bomberman.dead)
			return;
		if (this.sprite.currentAnimation.name.equals("explosion")
				&& this.sprite.current == this.sprite.currentAnimation.maxFrames - 1) {
			this.die();
			return;
		}
		this.sprite.update(elapsed);

		// Checking AABB collision with the player
		if (!bomberman.immune) {
			if (this.posX + this.width > bomberman.posX
					&& this.posX < bomberman.posX + bomberman.width
					&& this.posY + this.height > bomberman.posY
					&& this.posY < bomberman.posY + bomberman.height) {
				bomberman.die();
			}
		}
	}

	/**
	 * 
	 * Renders the enemy on the specified graphics context.
	 * 
	 * @param g2d The graphics context to render on.
	 */
	public abstract void render(Graphics2D g2d);
}