package managers;

import java.awt.Graphics2D;
import java.util.ArrayList;

import entities.*;
import entities.powerups.BombPowerup;
import entities.powerups.LivesPowerup;
import entities.powerups.PassThroughPowerup;
import entities.powerups.RadiusPowerup;
import entities.powerups.SpeedPowerup;

/*
 * This class manages all the powerups. 
 * it has an array list which contains all the powerups, 
 * and updates, draws all thepowerups at the same time. 
 */

public class PowerupManager {
	public static PowerupManager instance = null;
	public ArrayList<PowerUp> powerups;
	public ArrayList<PowerUp> toRemove;
	/*
	 * Array to store all the classes. When instanciating a new powerup, it will be
	 * randomly chosen in this array. If you want to add a powerup, just make a new
	 * one in the powerups folder and add it here like the previous ones.
	 * See the die function of the Tile.java file.
	 */
	public Class<?>[] classes = {
			BombPowerup.class,
			RadiusPowerup.class,
			PassThroughPowerup.class,
			LivesPowerup.class,
			SpeedPowerup.class, };

	private PowerupManager() {
		this.powerups = new ArrayList<>();
		this.toRemove = new ArrayList<>();
	}

	public static synchronized PowerupManager build() {
		if (instance == null) {
			instance = new PowerupManager();
		}
		return instance;
	}

	public void updatePowerup(int elapsed) {
		for (PowerUp p : this.powerups) {
			if (p.dead) {
				this.toRemove.add(p);
			} else {
				p.update(elapsed);
			}
		}

		this.toRemove.forEach(p -> this.powerups.remove(p));
	}

	public void drawPowerups(Graphics2D g2d) {
		ArrayList<PowerUp> copy = new ArrayList<>(this.powerups);
		for (PowerUp p : copy) {
			p.render(g2d);
		}
	}
}