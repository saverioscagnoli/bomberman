package entities;

import java.awt.Color;
import java.awt.Graphics2D;

import loop.GameLoop;
import util.Consts;
import util.Utils;


public class Enemy extends Entity {

    private int direction = 0;

    public int health;
    public boolean immune = false;

    public Enemy(float posX, float posY, int width, int height, int speed) {
        super(posX, posY, width, height, speed);
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
        // the enemy moves in a direction until it hits a wall, then it changes direction
        switch (this.direction) {
         case 0:
                if (Utils.EnemyCollision(this, GameLoop.entities, direction)){
                    this.direction = 1;
                } else {
                    this.posX -= this.speed;
                }
                break;
            case 1:
                if (Utils.EnemyCollision(this, GameLoop.entities, direction)){
                    this.direction = 0;
                } else {
                    this.posX += this.speed;
                }
                break;
            case 2:
                if (Utils.EnemyCollision(this, GameLoop.entities, direction)){
                    this.direction = 3;
                } else {
                    this.posY -= this.speed;
                }
                break;
            case 3:
                if (Utils.EnemyCollision(this, GameLoop.entities, direction)){
                    this.direction = 2;
                } else {
                    this.posY += this.speed;
                }
                break;
        }

        // if the enemy is about to hit a solid entity, change direction

    }

    public void render(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect((int) this.posX, (int) this.posY, this.width, this.height);

        g2d.setColor(Color.RED);
        g2d.fillRect((int) posX-15, (int) posY-20, 5*10, 5);
        g2d.setColor(Color.GREEN);
        g2d.fillRect((int) posX-15, (int) posY-20, health*10, 5);

    }
}
