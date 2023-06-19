package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import loop.Loop;
import ui.SpriteAnimation;
import loop.Controller;
import util.*;

public class Bomberman extends Entity {

	Controller keyHandler;
	Loop gameLoop;
	public int bombRadius, maxBombs;
	public int health;
	public boolean immune;
	public int lives;

	public Bomberman(float posX, float posY, int width, int height, int speed, Controller keyHandler,
			Loop gameLoop) {
		super(posX, posY, width, height, speed, "assets/bomberman.png", false);

		this.maxBombs = 3;
		this.gameLoop = gameLoop;
		this.keyHandler = keyHandler;
		this.bombRadius = 3;
		this.health = 5;
		this.immune = false;
		this.lives = 3;
		this.direction = "up";

		super.setScale(2.1f);
		super.addAnimation("left", new SpriteAnimation(this.spritesheet, 5, 6.3, this.scale, 0, 3, 10));
		super.addAnimation("down", new SpriteAnimation(this.spritesheet, 5, 6.3, this.scale, 1, 3, 10));
		super.addAnimation("right", new SpriteAnimation(this.spritesheet, 5, 6.3, this.scale, 2, 3, 10));
		super.addAnimation("up", new SpriteAnimation(this.spritesheet, 5, 6.3, this.scale, 3, 3, 10));

	}

	public void dealDamage(int damage) {
		if (!immune) {
			health -= damage;
			immune = true;
			Utils.setTimeout(() -> immune = false, 1000);
		}
	}

	public void update() {

		// TODO: sarebbe un po' piu organizzato creare un metodo fatto apposta nel
		// collision checker per il movimento del player.
		// probabilmente si potrebbe anche comprimere un po'.

		super.updateSprite();

		if (!keyHandler.buttonPriorities.isEmpty()) {

			super.isAnimated = true;
			switch (keyHandler.buttonPriorities.get(0)) {
				case "A":
					for (Entity entity : CollisionChecker.adjacentEntities) {
						if (entity == null)
							continue;
						if (CollisionChecker.checkCollision(entity, this, "left")) {
							posX = entity.posX + entity.width + 2;
							posX += speed;
							break;
						}
					}
					posX -= speed;
					direction = "left";
					break;
				case "D":
					for (Entity entity : CollisionChecker.adjacentEntities) {
						if (entity == null)
							continue;
						if (CollisionChecker.checkCollision(entity, this, "right")) {
							posX = entity.posX - width - 1;
							posX -= speed;
							break;
						}
					}
					posX += speed;
					direction = "right";
					break;
				case "W":
					for (Entity entity : CollisionChecker.adjacentEntities) {
						if (entity == null)
							continue;
						if (CollisionChecker.checkCollision(entity, this, "up")) {
							posY = entity.posY + entity.height + 2;
							posY += speed;
							break;
						}
					}
					posY -= speed;
					direction = "up";
					break;
				case "S":
					for (Entity entity : CollisionChecker.adjacentEntities) {
						if (entity == null)
							continue;
						if (CollisionChecker.checkCollision(entity, this, "down")) {
							posY = entity.posY - height - 1;
							posY -= speed;
							break;
						}
					}
					posY += speed;
					direction = "down";
					break;
			}

			super.setAnimation(direction);
		} else {
			super.isAnimated = false;
			super.currentAnimation.currentFrame = 1;
		}
	}

	public void render(Graphics2D g2d) {
		super.drawSprite(g2d, (int) this.posX, (int) this.posY - 22);
		// draw the health bar above the player with 5 squares for each health point

		// draw the hitbox as a gray square
		// g2d.setColor(Color.GRAY);
		// g2d.fillRect((int) posX, (int) posY, width, height);

		g2d.setColor(Color.RED);
		g2d.fillRect((int) posX - 15, (int) posY - 20, 5 * 10, 5);
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) posX - 15, (int) posY - 20, health * 10, 5);
		g2d.setColor(Color.GRAY);
	}
}
