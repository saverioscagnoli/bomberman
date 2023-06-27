package managers;

import java.util.ArrayList;
import entities.Enemy;
import entities.enemies.Denkyun;
import util.Consts;
import util.Utils;
import java.awt.Graphics2D;

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

	public void instanciateEnemies(int n) {
		for (int i = 0; i < n; i++) {
			int x = Utils.rng(Consts.tileDims + 1, Consts.screenWidth - Consts.tileDims);
			int y = Utils.rng(Consts.tileDims + 1, Consts.screenHeight - Consts.tileDims);
			int[] pos = Utils.normalizePos(x, y);
			String[][] grid = TileManager.build().grid;
			while (grid[y / Consts.tileDims][x / Consts.tileDims] == "WD"
					|| grid[y / Consts.tileDims][x / Consts.tileDims] == "W") {
				x = Utils.rng(Consts.tileDims + 1, Consts.screenWidth - Consts.tileDims);
				y = Utils.rng(Consts.tileDims + 1, Consts.screenHeight - Consts.tileDims);
				pos = Utils.normalizePos(x, y);
			}
			Enemy e = new Denkyun(pos[0], pos[1], Consts.tileDims - 2, Consts.tileDims - 2, 1);
			enemies.add(e);
		}
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
			e.render(g2d);
		}
	}
}
