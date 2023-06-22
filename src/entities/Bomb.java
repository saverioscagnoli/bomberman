package entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import loop.Loop;
import managers.BombManager;
import managers.EnemyManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

public class Bomb extends Entity {

	public Bomb(int posX, int posY, int bombRadius) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0,
				new Sprite("bomb", 4, 1, "idle",
						new SpriteAnimation[] { new SpriteAnimation("idle", 4, 0, 10) }, 1));
		this.explode(3000);
		int i = posX / Consts.tileDims;
		int j = posY / Consts.tileDims;
		TileManager.build().grid[i][j] = "B";
	}

	public void die() {
		this.dead = true;
	}

	@Override
	public void update(int elapsed) {
		this.sprite.update(elapsed);
	}

	private void explode(int ms) {
		Explosion[][] explosionMatrix = new Explosion[4][5];
		int r = Loop.character.bombRadius;

		Utils.setTimeout(() -> {
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
				boolean hitWall = false;
				for (int j = 0; j < r + 1; j++) {
					Explosion ex = explosionMatrix[i][j];
					if (ex != null) {
						if (!this.checkSolid(ex.posX, ex.posY) && !hitWall) {
							BombManager.build().addExplosion(ex);
						} else {
							hitWall = true;
							break;
						}
					}
				}
			}
			this.die();
		}, ms);
	}

	private boolean checkSolid(int posX, int posY) {
		Collection<Entity> wallsAndEnemies = new ArrayList<>();
		wallsAndEnemies.addAll(EnemyManager.getInstance().enemies);
		wallsAndEnemies.addAll(TileManager.build().walls);
		wallsAndEnemies.addAll(BombManager.build().bombs);
		wallsAndEnemies.add(Loop.character);

		for (Entity e : wallsAndEnemies) { // for every entity in the list
			if (e.posX == posX && e.posY == posY) { // if the entity is in the same position as the explosion
				if (e.isSolid) { // if the entity is solid
					Obstacle wall = (Obstacle) e; // cast the entity to an obstacle
					if (wall.destructable) { // if the obstacle is destructable
						wall.die(); // destroy the obstacle
						int x = (int) wall.posX / Consts.tileDims;
						int y = (int) wall.posY / Consts.tileDims;
						TileManager.build().grid[y][x] = "N";

						// have a 30% chance to drop a powerup when a wall is destroyed
						/*
						 * if (Math.random() < 0.3) {
						 * PowerUp powerup = new PowerUp(wall.posX, wall.posY, Consts.tileDims,
						 * Consts.tileDims, 0, "speed");
						 * PowerupManager.getInstance().powerups.add(powerup);
						 * }
						 */
					}
				}
				return true;
			}

			// check if the entity is an enemy. if it is, and the enemy and explosion
			// overlap, kill it.
			if (e instanceof Enemy) {
				Enemy enemy = (Enemy) e;
				// if the enemy and the explosion have aabb collision, damage it.
				if ((enemy.posX < posX + Consts.tileDims && enemy.posX + enemy.width > posX
						&& enemy.posY < posY + Consts.tileDims && enemy.posY + enemy.height > posY)) {
					enemy.dealDamage(1);
				}

			}

			if (e instanceof Bomberman) {
				Bomberman player = (Bomberman) e;
				if (player.posX < posX + Consts.tileDims && player.posX + player.width > posX
						&& player.posY < posY + Consts.tileDims && player.posY + player.height > posY) {
					player.dealDamage(1);
				}
			}
		}
		return false;
	}

	@Override
	public void render(Graphics2D g2d) {
		this.sprite.draw(g2d, posX, posY, this.width, this.height);
	}
}
