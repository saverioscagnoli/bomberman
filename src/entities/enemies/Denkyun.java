package entities.enemies;

import java.awt.Graphics2D;
import entities.Enemy;
import ui.Sprite;
import ui.SpriteAnimation;

public class Denkyun extends Enemy {

	public Denkyun(int posX, int posY, int width, int height, int speed) {
		super(posX, posY, width, height, speed, new Sprite("enemy-1", 4, 4, "down", new SpriteAnimation[] {
				new SpriteAnimation("down", 4, 0, 5),
				new SpriteAnimation("up", 4, 1, 5),
				new SpriteAnimation("left", 4, 2, 5),
				new SpriteAnimation("right", 4, 3, 5)
		}, 2.5f));
		this.health = 2;
		this.direction = "left";
	}

	public void update(int elapsed) {
		// the enemy moves in a direction until it hits a wall, then it changes
		// direction
		super.update(elapsed);

		// TODO : USARE CODICE INTERSEZIONE PER PROSSIMI NEMICI :
		// int[] prova = { (int) this.posX, (int) this.posY };
		// int[] normPos = Utils.normalizeEntityPos(this);
		// boolean intersection = (normPos[0] == prova[0] && normPos[1] == prova[1]);
		// if (intersection == true) {
		// if (Utils.rng(0, 100) < 10) {
		// this.direction = "up";
		// } else {
		// this.direction = "down"; fixare sto else di merda che non serve a nulla,
		// bisogna mettere uno switch

		// }
		// }

		// int randInt = Utils.rng(0, 100);
		/*
		 * switch (this.direction) {
		 * case "left":
		 * if (Utils.enemyCollision(this, direction)) {
		 * // 1/3 chance to go down, 1/3 chance to go up, 1/3 chance to go right
		 * if (randInt < 33) {
		 * this.direction = "down";
		 * this.posX += this.speed;
		 * } else if (randInt < 66) {
		 * this.direction = "up";
		 * this.posX += this.speed;
		 * } else {
		 * this.direction = "right";
		 * }
		 * } else {
		 * this.posX -= this.speed;
		 * }
		 * break;
		 * case "right":
		 * if (Utils.enemyCollision(this, direction)) {
		 * // 1/3 chance to go down, 1/3 chance to go up, 1/3 chance to go left
		 * if (randInt < 33) {
		 * this.direction = "down";
		 * this.posX -= this.speed;
		 * } else if (randInt < 66) {
		 * this.direction = "up";
		 * this.posX -= this.speed;
		 * } else {
		 * this.direction = "left";
		 * }
		 * } else {
		 * this.posX += this.speed;
		 * }
		 * break;
		 * case "up":
		 * if (Utils.enemyCollision(this, direction)) {
		 * // 1/3 chance to go left, 1/3 chance to go right, 1/3 chance to go down
		 * if (randInt < 33) {
		 * this.direction = "left";
		 * this.posY += this.speed;
		 * } else if (randInt < 66) {
		 * this.direction = "right";
		 * this.posY += this.speed;
		 * } else {
		 * this.direction = "down";
		 * }
		 * } else {
		 * this.posY -= this.speed;
		 * }
		 * break;
		 * 
		 * case "down":
		 * if (Utils.enemyCollision(this, direction)) {
		 * // 1/3 chance to go left, 1/3 chance to go right, 1/3 chance to go up
		 * if (randInt < 33) {
		 * this.direction = "left";
		 * this.posY -= this.speed;
		 * } else if (randInt < 66) {
		 * this.direction = "right";
		 * this.posY -= this.speed;
		 * } else {
		 * this.direction = "up";
		 * }
		 * } else {
		 * this.posY += this.speed;
		 * }
		 * break;
		 * }
		 */
	}

	public void render(Graphics2D g2d) {
		// draw hitbox as blue box (debug purpose)
		this.sprite.draw(g2d, posX, posY);
	}
}
