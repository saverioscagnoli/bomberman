package managers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import entities.*;

public class PowerupManager {

	public static PowerupManager instance = null;
	public ArrayList<PowerUp> powerups;

	private PowerupManager() {
		this.powerups = new ArrayList<>();
	}

	public static synchronized PowerupManager getInstance() {
		if (instance == null) {
			instance = new PowerupManager();
		}
		return instance;
	}

	public static void HandlePowerup(PowerUp p, Bomberman c) {
		switch (p.name) {
			case "speed": {
				Runnable onPickup = () -> {
					c.speed += 5;
				};
				Runnable onExpire = () -> {
					c.speed -= 5;
				};
				p.onPickup(5000, onPickup, onExpire);
			}
				break;

			case "bomb": {
				Runnable onPickup = () -> {
					if (c.bombRadius < 5) {
						c.bombRadius += 1;
					} // avoids having the bomb radius going above 5.
				};
				Runnable onExpire = () -> {
				};
				p.onPickup(5000, onPickup, onExpire);
			}
				break;

			case "rain": {
				Runnable onPickup = () -> {
					for (Enemy e : EnemyManager.getInstance().enemies) {
						if (e instanceof Enemy) {
							Enemy enemy = (Enemy) e;
							enemy.dealDamage((int) enemy.health / 2);
							System.out.println(enemy + " health: " + enemy.health);
						}
					}
				};
				Runnable onExpire = () -> {
				};
				p.onPickup(5000, onPickup, onExpire);
			}
		}
	}

	public static void RenderPowerup(Graphics2D g2d) {
		// draw an orange rectangle for the powerup
		for (PowerUp p : PowerupManager.getInstance().powerups) {
			p.render(g2d);
		}
	}

	public static void UpdatePowerup(int elapsed) {
		for (PowerUp p : PowerupManager.getInstance().powerups) {
			if (p.dead) {
				PowerupManager.getInstance().powerups.remove(p);
				break;
			} else {
				p.update(elapsed);
			}
		}
	}

	public static void RenderMessage(PowerUp p, Graphics2D g2d) {
		// draw a message above the powerup for 1 second
		System.out.println("rendering message");
		g2d.setColor(Color.BLACK);
		g2d.drawString(p.name, (int) p.posX, (int) p.posY - 10);
	}

}