/* package entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import entities.Enemy;
import util.Utils;

public class Puropen extends Enemy {

	public Puropen(int posX, int posY, int width, int height, int speed, String src) {
		super(posX, posY, 47, 47, 1, null);
		this.health = 1;
		this.direction = "left";
	}

	public void update() {
		// the enemy moves in a direction until it hits a wall, then it changes
		// direction
		super.updateSprite();
		switch (this.direction) {
			case "left":
				if (Utils.enemyCollision(this, direction)) {
					this.direction = "right";
				} else {
					this.posX -= this.speed;
				}
				break;
			case "right":
				if (Utils.enemyCollision(this, direction)) {
					this.direction = "left";
				} else {
					this.posX += this.speed;
				}
				break;
			case "up":
				if (Utils.enemyCollision(this, direction)) {
					this.direction = "down";
				} else {
					this.posY -= this.speed;
				}
				break;
			case "down":
				if (Utils.enemyCollision(this, direction)) {
					this.direction = "up";
				} else {
					this.posY += this.speed;
				}
				break;
		}
		this.isAnimated = true;
		// this.setAnimation(direction);
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
 */