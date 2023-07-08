package managers;

import java.util.ArrayList;
import java.util.List;
import entities.Bomb;
import entities.Explosion;
import util.CollisionChecker;
import util.Consts;
import util.TileType;

import java.awt.Graphics2D;

/**
 * 
 * The BombManager class manages all the bombs in the game. It keeps track of
 * the bombs,
 * 
 * updates their states, and renders them on the screen. It also manages the
 * explosions
 * 
 * caused by the bombs.
 * 
 * The class uses the singleton pattern to ensure only one instance is created.
 */
public class BombManager {
	private static BombManager instance = null;
	public ArrayList<Bomb> bombs;
	public ArrayList<Explosion> explosions;

	/**
	 * 
	 * Private constructor to enforce singleton pattern.
	 */
	private BombManager() {
		this.bombs = new ArrayList<>();
		this.explosions = new ArrayList<>();
	}

	/**
	 * 
	 * Retrieves the singleton instance of BombManager.
	 * 
	 * @return The BombManager instance.
	 */
	public static synchronized BombManager build() {
		if (instance == null) {
			instance = new BombManager();
		}
		return instance;
	}

	/**
	 * 
	 * Adds a bomb to the manager.
	 * 
	 * @param bomb The bomb to be added.
	 */
	public void addBomb(Bomb bomb) {
		this.bombs.add(bomb);
	}

	/**
	 * 
	 * Adds an explosion to the manager.
	 * 
	 * @param ex The explosion to be added.
	 */
	public void addExplosion(Explosion ex) {
		this.explosions.add(ex);
	}

	/**
	 * 
	 * Pauses all the bombs.
	 */
	public void pauseBombs() {
		for (Bomb b : this.bombs) {
			b.pause();
		}
	}

	/**
	 * 
	 * Resumes all the bombs.
	 */
	public void resumeBombs() {
		for (Bomb b : this.bombs) {
			b.resume();
		}
	}

	/**
	 * 
	 * Updates the explosions based on the elapsed time.
	 * 
	 * @param elapsed The time elapsed since the last update.
	 */
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

	/**
	 * 
	 * Updates the bombs based on the elapsed time. Removes dead bombs and updates
	 * the collision checker.
	 * 
	 * @param elapsed The time elapsed since the last update.
	 */
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

	/**
	 * 
	 * Draws the active explosions on the screen.
	 * 
	 * @param g2d The graphics context.
	 */
	public void drawExplosions(Graphics2D g2d) {
		List<Explosion> explosionsCopy = new ArrayList<>(this.explosions);
		for (Explosion ex : explosionsCopy) {
			if (!ex.dead) {
				ex.render(g2d);
			}
		}
	}

	/**
	 * 
	 * Draws the active bombs on the screen.
	 * 
	 * @param g2d The graphics context.
	 */
	public void drawBombs(Graphics2D g2d) {
		List<Bomb> bombsCopy = new ArrayList<>(this.bombs);
		for (Bomb b : bombsCopy) {
			if (!b.dead) {
				b.render(g2d);
			}
		}
	}
}
