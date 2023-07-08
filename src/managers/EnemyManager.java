package managers;

import java.util.ArrayList;
import java.util.List;

import entities.Enemy;
import entities.enemies.Cuppen;
import entities.enemies.Denkyun;
import entities.enemies.NutsStar;
import entities.enemies.Pakupa;
import entities.enemies.Puropen;
import util.Consts;
import util.TileType;
import util.Utils;

import java.awt.Graphics2D;

/**
 * 
 * The EnemyManager class manages all the enemies in the game.
 * 
 * It uses the singleton pattern to ensure only one instance is created.
 */
public class EnemyManager {
	public static EnemyManager instance = null;
	public ArrayList<Enemy> enemies;
	public Class<?>[] classes = { Puropen.class, Denkyun.class, NutsStar.class, Pakupa.class, Cuppen.class };

	/**
	 * 
	 * Private constructor to enforce singleton pattern.
	 */
	private EnemyManager() {
		this.enemies = new ArrayList<>();
	}

	/**
	 * 
	 * Retrieves the singleton instance of EnemyManager.
	 * 
	 * @return The EnemyManager instance.
	 */
	public static synchronized EnemyManager build() {
		if (instance == null) {
			instance = new EnemyManager();
		}
		return instance;
	}

	/**
	 * 
	 * Instantiates a specified number of enemies at random positions.
	 * 
	 * @param n The number of enemies to instantiate.
	 */
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
	}

	/**
	 * 
	 * Updates all the enemies.
	 * 
	 * @param elapsed The elapsed time since the last update.
	 */
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

	/**
	 * 
	 * Draws all the enemies.
	 * 
	 * @param g2d The graphics context.
	 */
	public void drawEnemies(Graphics2D g2d) {
		List<Enemy> enemiesCopy = new ArrayList<>(this.enemies);
		for (Enemy e : enemiesCopy) {
			if (!e.dead) {
				e.render(g2d);
			}
		}
	}
}