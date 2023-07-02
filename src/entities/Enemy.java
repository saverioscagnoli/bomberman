package entities;

import java.awt.Color;
import java.awt.Graphics2D;

import ui.Sprite;
import util.Utils;

public class Enemy extends Entity {

	/* The direction of the enemy */
	protected String direction;

	/* The hp of the enemy */
	public int health;

	/* A flag to determine if the enemy is immune, and will not take damage */
	public boolean immune = false;

	public Enemy(int posX, int posY, int width, int height, int speed, Sprite sprite) {
		super(posX, posY, width, height, speed, sprite);
		this.health = 3;

		// sets a random direction between right, left, up, down
		String[] directions = { "right", "left", "up", "down" };
		this.direction = Utils.pick(directions);
	}

	public void die() {
		this.dead = true;
	}

	/* Deals damage to the enemy */
	public void dealDamage(int damage) {
		if (!immune) {
			health -= damage;
			immune = true;
			Utils.setTimeout(() -> immune = false, 100);
			System.out.println(this + " health: " + health);
			if (health <= 0) {
				this.die();
			}
		}
	}

	public void update(int elapsed) {
		this.sprite.update(elapsed);
	}

	public void render(Graphics2D g2d) {
		this.sprite.draw(g2d, (int) this.posX, (int) this.posY);
		// draw the as red (debug purpose)
		// g2d.setColor(Color.RED);
		// g2d.fillRect((int) posX, (int) posY, width, height);

		// draw an health bar
		g2d.setColor(Color.RED);
		g2d.fillRect((int) posX - 15, (int) posY - 20, 5 * 10, 5);
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) posX - 15, (int) posY - 20, health * 10, 5);
	}
}
