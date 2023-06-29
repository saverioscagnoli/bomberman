package managers;

import java.util.ArrayList;
import entities.Enemy;
import entities.bosses.ClownMask;
import java.awt.Graphics2D;

/*
 * This class manages all the enemies. 
 * it has an array list which contains all the enemies, 
 * and updates, draws all the enemies at the same time. 
 */

public class EnemyManager {
	public static EnemyManager instance = null;
	public ArrayList<Enemy> enemies;

	private EnemyManager() {
		this.enemies = new ArrayList<>();
	}

	public static synchronized EnemyManager build() {
		if (instance == null) {
			instance = new EnemyManager();
		}
		return instance;
	}

	/* The function that instanciates all the enemies at random positions. */
	public void instanciateEnemies(int n) {
		/*
		 * for (int i = 0; i < n; i++) {
		 * int x = Utils.rng(Consts.tileDims + 1, Consts.screenWidth - Consts.tileDims);
		 * int y = Utils.rng(Consts.tileDims + 1, Consts.screenHeight -
		 * Consts.tileDims);
		 * int[] pos = Utils.normalizePos(x, y);
		 * TileType[][] grid = TileManager.build().grid;
		 * while (grid[y / Consts.tileDims][x / Consts.tileDims] == TileType.Obstacle
		 * || grid[y / Consts.tileDims][x / Consts.tileDims] == TileType.Wall) {
		 * x = Utils.rng(Consts.tileDims + 1, Consts.screenWidth - Consts.tileDims);
		 * y = Utils.rng(Consts.tileDims + 1, Consts.screenHeight - Consts.tileDims);
		 * pos = Utils.normalizePos(x, y);
		 * }
		 * Enemy e = new Denkyun(pos[0], pos[1], Consts.tileDims - 2, Consts.tileDims -
		 * 2, 1);
		 * enemies.add(e);
		 * }
		 */

//		this.enemies.add(new ClownMask(300, 300));
	}

	public void updateEnemies(int elapsed) {
		ArrayList<Enemy> toRemove = new ArrayList<>();
		int l = enemies.size();
		for (int i = 0; i < l; i++) {
			Enemy e = enemies.get(i);
			if (e.dead) {
				toRemove.add(e);
			} else {
				e.update(elapsed);
			}
		}

		toRemove.forEach((e) -> this.enemies.remove(e));
	}

	public void drawEnemies(Graphics2D g2d) {
		int l = enemies.size();
		for (int i = 0; i < l; i++) {
			Enemy e = enemies.get(i);
			if (!e.dead) {
				e.render(g2d);
			}
		}
	}
}
