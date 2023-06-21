package entities;

import java.awt.Color;
import java.awt.Graphics2D;

import util.Utils;

public class Enemy extends Entity {

	protected String direction;

	public int health;
	public boolean immune = false;

	public Enemy(float posX, float posY, int width, int height, int speed, String spritesheetName) {
		super(posX, posY, width, height, speed, spritesheetName, false, 4, 3, 4);
		this.health = 3;

		// sets a random direction between right, left, up, down
		int rand = (int) (Math.random() * 4);
		switch (rand) {
			case 0:
				this.direction = "right";
				break;
			case 1:
				this.direction = "left";
				break;
			case 2:
				this.direction = "up";
				break;
			case 3:
				this.direction = "down";
				break;
		}
	}

	public void dealDamage(int damage) {
		if (!immune) {
			health -= damage;
			immune = true;
			Utils.setTimeout(() -> immune = false, 30);
			System.out.println(this + " health: " + health);
			if (health <= 0) {
				this.die();
			}
		}
	}

	public void update() {
	}

	public void render(Graphics2D g2d) {
		super.drawSprite(g2d, (int) this.posX, (int) this.posY);

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
