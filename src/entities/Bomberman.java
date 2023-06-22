package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import ui.Sprite;
import ui.SpriteAnimation;
import loop.Controller;
import managers.BombManager;
import util.*;

public class Bomberman extends Entity {
	public int bombRadius;
	public int maxBombs;
	public int health;
	public boolean immune;
	public int lives;
	public boolean stop;
	private Controller controller;

	public Bomberman(int posX, int posY) {
		super(posX, posY, 30, 30, 5, new Sprite("bomberman", 6.3, 5, "down",
				new SpriteAnimation[] {
						new SpriteAnimation("left", 3, 0, 10),
						new SpriteAnimation("down", 3, 1, 10),
						new SpriteAnimation("right", 3, 2, 10),
						new SpriteAnimation("up", 3, 3, 10),
				},
				2.5f));
		this.controller = Controller.build();
		this.health = 3;
		this.maxBombs = 3;
		this.bombRadius = 2;
	}

	public void die() {
		this.dead = true;
	}

	public void placeBomb() {
		BombManager bombManager = BombManager.build();
		int[] pos = Utils.normalizePos(this.posX, this.posY);
		if (bombManager.bombs.size() >= this.maxBombs) {
			return;
		}
		// if the bomb is already there, don't add it
		if (bombManager.bombs.stream().anyMatch(b -> b.posX == pos[0] && b.posY == pos[1])) {
			return;
		}
		bombManager.addBomb(new Bomb(pos[0], pos[1], this.bombRadius));
	}

	public void dealDamage(int damage) {
		if (!immune) {
			health -= damage;
			immune = true;
			Utils.setTimeout(() -> immune = false, 1000);
		}
	}

	public void update(int elapsed) {

		// TODO: sarebbe un po' piu organizzato creare un metodo fatto apposta nel
		// collision checker per il movimento del player.
		// probabilmente si potrebbe anche comprimere un po'.

		this.sprite.update(elapsed);

		if (!controller.buttonPriorities.isEmpty()) {
			String direction = "";
			this.stop = false;

			switch (controller.buttonPriorities.get(0)) {
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
		} else {
			this.stop = true;
			this.sprite.current = 1;
		}
	}

	public void render(Graphics2D g2d) {
		this.sprite.draw(g2d, (int) this.posX, (int) this.posY - 30);
		// draw the health bar above the player with 5 squares for each health point

		// draw the hitbox as a gray square
		/*
		 * g2d.setColor(Color.GRAY);
		 * g2d.fillRect(posX, posY, width, height)
		 */;
		g2d.setColor(Color.RED);
		g2d.fillRect((int) posX - 15, (int) posY - 20, 5 * 10, 5);
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) posX - 15, (int) posY - 20, health * 10, 5);
		g2d.setColor(Color.GRAY);
	}
}
