package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.Sprite;

public class Entity extends Sprite {
    public boolean isSolid = false;
    public float posX, posY;
    public int width, height;
    public int speed;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public boolean dead;

    public Entity(float posX, float posY, int width, int height, int speed, BufferedImage spritesheet,
            boolean isStatic, int maxFrames, int absoluteFrames, int stagger, int rows, int frameY) {
        super(spritesheet, isStatic, maxFrames, absoluteFrames, stagger, rows, frameY);
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.dead = false;
    }

    public void die() {
        this.dead = true;
    }

    public void update() {
    }

    public void render(Graphics2D g2d) {
    }
}