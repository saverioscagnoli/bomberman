package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import util.Utils;

public class Enemy extends Entity {

    private String direction = "right";

    public int health;
    public boolean immune = false;

    public Enemy(float posX, float posY, int width, int height, int speed, String src) {
        super(posX, posY, width, height, speed, src, false);
        this.health = 3;
    }

    public void dealDamage(int damage) {
        if (!immune) {
            health -= damage;
            immune = true;
            Utils.setTimeout(() -> immune = false, 1000);
            System.out.println(this + " health: " + health);
            if (health <= 0) {
                this.die();
            }
        }
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

        super.setAnimation(direction);
        // if the enemy is about to hit a solid entity, change direction

    }

    public void render(Graphics2D g2d) {
        super.drawSprite(g2d, (int) this.posX, (int) this.posY);
        g2d.setColor(Color.RED);
        g2d.fillRect((int) posX - 15, (int) posY - 20, 5 * 10, 5);
        g2d.setColor(Color.GREEN);
        g2d.fillRect((int) posX - 15, (int) posY - 20, health * 10, 5);
    }
}
