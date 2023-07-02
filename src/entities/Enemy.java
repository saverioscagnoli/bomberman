package entities;

import java.awt.Graphics2D;
import core.Loop;
import entities.enemies.Denkyun;
import managers.SaveManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.TileType;
import util.Utils;

public class Enemy extends Entity {

	/* The direction of the enemy */
	protected String direction;

	protected int gridX;
	protected int gridY;

	/* The hp of the enemy */
	public int health;

	/* A flag to determine if the enemy is immune, and will not take damage */
	public boolean immune = false;

	protected boolean stop = false;

	public Enemy(int posX, int posY, int width, int height, int speed, Sprite sprite) {
		super(posX, posY, width, height, speed, sprite);
		this.health = 3;

		// sets a random direction between right, left, up, down
		String[] directions = { "right", "left", "up", "down" };
		this.direction = Utils.pick(directions);
		this.stop = false;

		this.gridX = posX / Consts.tileDims;
		this.gridY = posY / Consts.tileDims;
	}

	public void die() {
		this.dead = true;
		TileManager.build().grid[this.gridY][this.gridX] = TileType.Empty;
	}

	/* Deals damage to the enemy */
	public void dealDamage(int damage) {
		if (immune || this.dead)
			return;
		health -= damage;
		immune = true;
		Utils.setTimeout(() -> immune = false, 100);
		System.out.println(this + " health: " + health);
		if (health <= 0) {
			this.stop = true;
			Utils.setTimeout(() -> {
				Utils.playSound("assets/sounds/enemy-dies.wav");
				this.sprite = new Sprite("enemy-explosion", 7.9, 1, "explosion", new SpriteAnimation[] {
						new SpriteAnimation("explosion", 7, 0, 7)
				}, 2.5f);
				Denkyun e = (Denkyun) this;
				Loop.build().bomberman.score += e.score;
				SaveManager.incrementScore(e.score);
				Loop.build().overlay.repaint();
			}, 1000);
		}

	}

	public void update(int elapsed) {
		if (Loop.build().bomberman.dead)
			return;
		if (this.sprite.currentAnimation.name == "explosion"
				&& this.sprite.current == this.sprite.currentAnimation.maxFrames - 1) {
			this.die();
			return;
		}
		this.sprite.update(elapsed);

		// checking AABB collision with the player

		if (this.posX + this.width > Loop.build().bomberman.posX
				&& this.posX < Loop.build().bomberman.posX + Loop.build().bomberman.width
				&& this.posY + this.height > Loop.build().bomberman.posY
				&& this.posY < Loop.build().bomberman.posY + Loop.build().bomberman.height) {
			Loop.build().bomberman.die();
		}
	}

	public void render(Graphics2D g2d) {

	}
}
