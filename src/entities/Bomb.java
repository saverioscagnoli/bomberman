package entities;

import java.awt.Graphics2D;
import java.util.Optional;

import core.Loop;
import managers.BombManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

public class Bomb extends Entity {
	public boolean exploded;

	public Bomb(int posX, int posY, int bombRadius) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0,
				new Sprite("bomb", 4, 1, "idle",
						new SpriteAnimation[] { new SpriteAnimation("idle", 4, 0, 10) },
						1));

		Utils.setTimeout(() -> this.explode(), 3000);
		int i = posY / Consts.tileDims;
		int j = posX / Consts.tileDims;
		TileManager.build().grid[i][j] = "B";
		this.isSolid = true;
		this.exploded = false;
	}

	public void die() {
		this.dead = true;
		int i = posY / Consts.tileDims;
		int j = posX / Consts.tileDims;
		TileManager.build().grid[i][j] = "N";
	}

	@Override
	public void update(int elapsed) {
		this.sprite.update(elapsed);
	}

	public void explode() {
		if (this.exploded)
			return;
		this.exploded = true;
		this.die();
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
					String tile = TileManager.build().grid[y][x];
					if (tile != "W") {
						if (tile == "WD") {
							Optional<Obstacle> wall = TileManager.build().walls
									.stream()
									.filter((w) -> w.posX == ex.posX && w.posY == ex.posY)
									.findFirst();

							if (wall.isPresent()) {
								wall.get().die();
							}
							TileManager.build().grid[y][x] = "N";
							break;
						} else {
							BombManager.build().addExplosion(ex);
							if (tile == "B") {
								Optional<Bomb> bomb = BombManager
										.build().bombs
										.stream()
										.filter(b -> b.posX == ex.posX && b.posY == ex.posY)
										.findFirst();

								if (bomb.isPresent()) {
									Utils.setTimeout(() -> bomb.get().explode(), 100);
								}
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
