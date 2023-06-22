package managers;

import java.util.ArrayList;
import entities.Bomb;
import entities.Explosion;
import java.awt.Graphics2D;

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
		toRemove.forEach((b) -> this.bombs.remove(b));
	}

	public void drawExplosions(Graphics2D g2d) {
		int l = this.explosions.size();
		for (int i = 0; i < l; i++) {
			this.explosions.get(i).render(g2d);
		}
	}

	public void drawBombs(Graphics2D g2d) {
		int l = this.bombs.size();
		for (int i = 0; i < l; i++) {
			bombs.get(i).render(g2d);
		}
	}
}
