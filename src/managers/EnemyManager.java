package managers;

import java.util.ArrayList;
import java.util.List;

import entities.Enemy;
import entities.bosses.FaralsBoss;
import entities.enemies.Cuppen;
import entities.enemies.Denkyun;
import entities.enemies.NutsStar;
import entities.enemies.Pakupa;
import entities.enemies.Puropen;
import util.Consts;
import util.TileType;
import util.Utils;

import java.awt.Graphics2D;

/*
 * This class manages all the enemies. 
 * it has an array list which contains all the enemies, 
 * and updates, draws all the enemies at the same time. 
 */

public class EnemyManager {
	public static EnemyManager instance = null;
	public ArrayList<Enemy> enemies;
	public Class<?>[] classes = { Puropen.class, Denkyun.class, NutsStar.class, Pakupa.class, Cuppen.class };

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
		this.enemies.clear();
		for (int i = 0; i < n; i++) {

			int gridX = Utils.rng(4, Consts.gridWidth - 1);
			int gridY = Utils.rng(4, Consts.gridHeight - 1);
			int x = gridX * Consts.tileDims;
			int y = gridY * Consts.tileDims;

			while (TileManager.build().grid[gridY][gridX] != TileType.Empty) {
				gridX = Utils.rng(4, Consts.gridWidth - 1);
				gridY = Utils.rng(4, Consts.gridHeight - 1);
				x = gridX * Consts.tileDims;
				y = gridY * Consts.tileDims;
			}

			Class<?> c = Utils.pick(classes);
			Enemy e = null;
			try {
				e = (Enemy) c.getConstructor(int.class, int.class).newInstance(x, y);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			enemies.add(e);
		}
		// enemies.add(new FaralsBoss(200, 200));
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
		List<Enemy> enemiesCopy = new ArrayList<>(this.enemies);
		for (Enemy e : enemiesCopy) {
			if (!e.dead) {
				e.render(g2d);
			}
		}
	}
}
