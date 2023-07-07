package managers;

import java.util.ArrayList;
import java.util.List;
import entities.Bomb;
import entities.Explosion;
import util.CollisionChecker;
import util.Consts;
import util.TileType;

import java.awt.Graphics2D;

/*
 * 
 * This class manages all the bombs. 
 * it has an array list which contains all the bombs, 
 * and updates, draws, pauses and resumes all the bombs at the same time. 
 */

public class BombManager {
	private static BombManager instance = null;
	public ArrayList<Bomb> bombs;
	public ArrayList<Explosion> explosions;

	private BombManager() {
		this.bombs = new ArrayList<>();
		this.explosions = new ArrayList<>();
	}

	public static synchronized BombManager build() {
		if (instance == null) {
			instance = new BombManager();
		}
		return instance;
	}

	public void addBomb(Bomb bomb) {
		this.bombs.add(bomb);
	}

	public void addExplosion(Explosion ex) {
		this.explosions.add(ex);
	}

	public void pauseBombs() {
		for (Bomb b : this.bombs) {
			b.pause();
		}
	}

	public void resumeBombs() {
		for (Bomb b : this.bombs) {
			b.resume();
		}
	}

	public void updateExplosions(int elapsed) {
		ArrayList<Explosion> toRemove = new ArrayList<>();
		int l = this.explosions.size();
		for (int i = 0; i < l; i++) {
			Explosion ex = this.explosions.get(i);
			if (ex.dead) {
				toRemove.add(ex);
			} else {
				ex.update(elapsed);
			}
		}
		toRemove.forEach((ex) -> this.explosions.remove(ex));
	}

	public void updateBombs(int elapsed) {
		ArrayList<Bomb> toRemove = new ArrayList<>();
		int l = this.bombs.size();
		for (int i = 0; i < l; i++) {
			Bomb b = bombs.get(i);
			if (b.dead) {
				toRemove.add(b);
			} else {
				b.update(elapsed);
			}
		}
		toRemove.forEach((b) -> {
			this.bombs.remove(b);
			CollisionChecker.build().update_Centered_Collisions();
		});

		if (this.bombs.size() == 0) {
			TileManager tileManager = TileManager.build();
			for (int i = 0; i < Consts.gridHeight; i++) {
				for (int j = 0; j < Consts.gridWidth; j++) {
					if (tileManager.grid[i][j] == TileType.Bomb) {
						tileManager.grid[i][j] = TileType.Empty;
					}
				}
			}
		}
	}

	public void drawExplosions(Graphics2D g2d) {
		List<Explosion> explosionsCopy = new ArrayList<>(this.explosions);
		for (Explosion ex : explosionsCopy) {
			if (!ex.dead) {
				ex.render(g2d);
			}
		}
	}

	public void drawBombs(Graphics2D g2d) {
		List<Bomb> bombsCopy = new ArrayList<>(this.bombs);
		for (Bomb b : bombsCopy) {
			if (!b.dead) {
				b.render(g2d);
			}
		}
	}
}
