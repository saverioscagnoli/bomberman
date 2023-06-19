package entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

import entities.Enemy;
import util.Utils;

public class Denkyun extends Enemy {

	public Denkyun(float posX, float posY, int width, int height, int speed, String src) {
		super(posX, posY, 47, 47, 1, src);
		this.health = 2;

	}

	public void update() {
		// the enemy moves in a direction until it hits a wall, then it changes
		// direction
		super.updateSprite();

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

		int[] nPos = Utils.normalizeEntityPos(this);
		int randInt = Utils.rng(0, 100);
		switch (this.direction) {
			case "left":
				if (Utils.enemyCollision(this, direction)) {
					// 1/3 chance to go down, 1/3 chance to go up, 1/3 chance to go right
					if (randInt < 33) {
						this.direction = "down";
						this.posX += this.speed;
					} else if (randInt < 66) {
						this.direction = "up";
						this.posX += this.speed;
					} else {
						this.direction = "right";
					}
				} else {
					this.posX -= this.speed;
				}
				break;
			case "right":
				if (Utils.enemyCollision(this, direction)) {
					// 1/3 chance to go down, 1/3 chance to go up, 1/3 chance to go left
					if (randInt < 33) {
						this.direction = "down";
						this.posX -= this.speed;
					} else if (randInt < 66) {
						this.direction = "up";
						this.posX -= this.speed;
					} else {
						this.direction = "left";
					}
				} else {
					this.posX += this.speed;
				}
				break;
			case "up":
				if (Utils.enemyCollision(this, direction)) {
					// 1/3 chance to go left, 1/3 chance to go right, 1/3 chance to go down
					if (randInt < 33) {
						this.direction = "left";
						this.posY += this.speed;
					} else if (randInt < 66) {
						this.direction = "right";
						this.posY += this.speed;
					} else {
						this.direction = "down";
					}
				} else {
					this.posY -= this.speed;
				}
				break;

			case "down":
				if (Utils.enemyCollision(this, direction)) {
					// 1/3 chance to go left, 1/3 chance to go right, 1/3 chance to go up
					if (randInt < 33) {
						this.direction = "left";
						this.posY -= this.speed;
					} else if (randInt < 66) {
						this.direction = "right";
						this.posY -= this.speed;
					} else {
						this.direction = "up";
					}
					break;
				} else {
					this.posY += this.speed;
				}
		}

		this.isAnimated = true;
		this.setAnimation(direction);
	}

	public void render(Graphics2D g2d) {
		// draw hitbox as blue box (debug purpose)
		super.drawSprite(g2d, (int) this.posX, (int) this.posY);
	}
}
