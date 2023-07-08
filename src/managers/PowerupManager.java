package managers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import entities.*;
import entities.powerups.BombPowerup;
import entities.powerups.LivesPowerup;
import entities.powerups.PassThroughPowerup;
import entities.powerups.RadiusPowerup;
import entities.powerups.RainPowerup;
import entities.powerups.SkullPowerup;
import entities.powerups.SpeedPowerup;
import entities.powerups.VestPowerup;
import util.Utils;

/**
 * 
 * The PowerupManager class manages all the powerups in the game. It keeps track
 * of the powerups,
 * 
 * updates them, and draws them on the screen.
 * 
 * It uses the singleton pattern to ensure only one instance is created.
 */
public class PowerupManager {
	public static PowerupManager instance = null;
	public ArrayList<PowerUp> powerups;
	public ArrayList<PowerUp> toRemove;
	public boolean isRaining = false;
	public int rainFrame;
	BufferedImage cloud = Utils.loadImage("assets/powerups/cloud.png");
	BufferedImage rain = Utils.loadImage("assets/powerups/Raindrops.png");
	int yrain;
	/**
	 * 
	 * Array to store all the classes. When instantiating a new powerup, it will be
	 * randomly chosen from this array. To add a new powerup, create a new class in
	 * the powerups folder
	 * and add it to this array.
	 */
	public Class<?>[] classes = {
			BombPowerup.class,
			RadiusPowerup.class,
			PassThroughPowerup.class,
			LivesPowerup.class,
			SpeedPowerup.class,
			RainPowerup.class,
			VestPowerup.class,
			SkullPowerup.class
	};

	/**
	 * 
	 * Private constructor to enforce singleton pattern.
	 */
	private PowerupManager() {
		this.powerups = new ArrayList<>();
		this.toRemove = new ArrayList<>();
	}

	/**
	 * 
	 * Retrieves the singleton instance of PowerupManager.
	 * 
	 * @return The PowerupManager instance.
	 */
	public static synchronized PowerupManager build() {
		if (instance == null) {
			instance = new PowerupManager();
		}
		return instance;
	}

	/**
	 * 
	 * Updates the powerups by checking if they are dead and removing them if
	 * necessary.
	 * 
	 * @param elapsed The time elapsed since the last update.
	 */
	public void updatePowerup(int elapsed) {
		for (PowerUp p : this.powerups) {
			if (p.dead) {
				this.toRemove.add(p);
				if (p instanceof RainPowerup) {
					isRaining = true;
					rainFrame = 0;
				}
			} else {
				p.update(elapsed);
			}
		}

		this.toRemove.forEach(p -> this.powerups.remove(p));
	}

	/**
	 * 
	 * Draws the powerups on the screen, including rain effects if applicable.
	 * 
	 * @param g2d The graphics context.
	 */
	public void drawPowerups(Graphics2D g2d) {
		ArrayList<PowerUp> copy = new ArrayList<>(this.powerups);
		for (PowerUp p : copy) {
			p.render(g2d);
		}

		if (isRaining) {
			yrain += 3;
			g2d.drawImage(rain, 0, yrain, null);
			g2d.drawImage(cloud, 0, -80, null);
			Utils.setTimeout(() -> {
				PowerupManager.build().isRaining = false;
			}, 1000);
		} else {
			yrain = -40;
		}
	}
}