package entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import managers.BombManager;
import managers.EnemyManager;
import managers.TileManager;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

public class Bomb extends Entity {
	private Explosion explosionMatrix[][];
	private int offsetX = 5;
	private int offsetY = 10;

	public Bomb(float posX, float posY, int width, int height, int speed, int bombRadius) {
		super(posX, posY, width, height, speed, "assets/bomb.png", false);
		this.isSolid = false;
		Utils.setTimeout(() -> this.explode(bombRadius), 3000);
	}

	@Override
	public void update() {
		super.updateSprite();
	}

	private void explode(int bombRadius) {
		Utils.playSound("assets/bomb-explosion.wav");
		explosionMatrix = new Explosion[4][5]; // creating an array that can store up to 5 explosions in the 4
		// directions
		String src = "assets/explosion.png";

		for (int rad = 1; rad < bombRadius + 1; rad++) { // for the length of the bomb radius
			Explosion ex1 = new Explosion((int) this.posX - Consts.tileDims * rad, (int) this.posY, src);
			Explosion ex2 = new Explosion((int) this.posX, (int) this.posY - Consts.tileDims * rad, src);
			Explosion ex3 = new Explosion((int) this.posX + Consts.tileDims * rad, (int) this.posY, src);
			Explosion ex4 = new Explosion((int) this.posX, (int) this.posY + Consts.tileDims * rad, src);
			Explosion central = new Explosion((int) this.posX, (int) this.posY, src);
			central.setScale(2);
			central.addAnimation("central", new SpriteAnimation(central.spritesheet, 7, 9, central.scale, 1, 9, 3));

			ex1.setScale(2);
			ex2.setScale(2);
			ex3.setScale(2);
			ex4.setScale(2);

			if (rad == bombRadius) {
				ex1.addAnimation("left", new SpriteAnimation(ex1.spritesheet, 7, 9, ex1.scale, 4, 9, 3));
				ex2.addAnimation("up", new SpriteAnimation(ex2.spritesheet, 7, 9, ex2.scale, 0, 9, 3));
				ex3.addAnimation("right", new SpriteAnimation(ex3.spritesheet, 7, 9, ex3.scale, 5, 9, 3));
				ex4.addAnimation("down", new SpriteAnimation(ex3.spritesheet, 7, 9, ex4.scale, 3, 9, 3));

			} else {
				ex1.addAnimation("central-left", new SpriteAnimation(ex1.spritesheet, 7, 9, ex1.scale, 6, 9, 3));
				ex2.addAnimation("central-up", new SpriteAnimation(ex2.spritesheet, 7, 9, ex2.scale, 2, 9, 3));
				ex3.addAnimation("central-right", new SpriteAnimation(ex1.spritesheet, 7, 9, ex3.scale, 6, 9, 3));
				ex4.addAnimation("central-down", new SpriteAnimation(ex1.spritesheet, 7, 9, ex4.scale, 2, 9, 3));

			}
			explosionMatrix[0][rad] = ex1;
			explosionMatrix[1][rad] = ex2;
			explosionMatrix[2][rad] = ex3;
			explosionMatrix[3][rad] = ex4;
			BombManager.getInstance().addExplosion(central);
		}

		// adds explosions in explosion matrix to entities
		for (int i = 0; i < 4; i++) {
			boolean hitWall = false;
			for (int j = 0; j < bombRadius + 1; j++) {
				if (explosionMatrix[i][j] != null) {
					if (!this.checkSolid((int) explosionMatrix[i][j].posX, (int) explosionMatrix[i][j].posY)
							&& !hitWall) {
						BombManager bombManager = BombManager.getInstance();
						bombManager.addExplosion(explosionMatrix[i][j]);
					} else {
						hitWall = true;
						break;
					}
				}
			}
			this.die();
		}
	}

	private boolean checkSolid(int posX, int posY) {
		Collection<Entity> wallsAndEnemies = new ArrayList<>();
		wallsAndEnemies.addAll(EnemyManager.getInstance().enemies);
		wallsAndEnemies.addAll(TileManager.getInstance().obtsacles);

		for (Entity e : wallsAndEnemies) { // for every entity in the list
			if (e.posX == posX && e.posY == posY) { // if the entity is in the same position as the explosion
				if (e.isSolid) { // if the entity is solid
					Obstacle wall = (Obstacle) e; // cast the entity to an obstacle
					if (wall.destructable) { // if the obstacle is destructable
						Utils.setTimeout(() -> wall.die(), 100); // destroy the obstacle
						int x = (int) wall.posX / Consts.tileDims;
						int y = (int) wall.posY / Consts.tileDims;
						TileManager.getInstance().grid[y][x] = "N";
					}
				}
				return true;
			}

			// check if the entity is an enemy. if it is, and the enemy and explosion
			// overlap, kill it.
			if (e instanceof Enemy) {
				Enemy enemy = (Enemy) e;
				// if the enemy and the explosion have aabb collision, damage it.
				if (enemy.posX < posX + Consts.tileDims && enemy.posX + enemy.width > posX
						&& enemy.posY < posY + Consts.tileDims && enemy.posY + enemy.height > posY) {
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
		super.drawSprite(g2d, (int) this.posX + this.offsetX, (int) this.posY + this.offsetY);
	}
}
