package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ui.Sprite;

public class Entity extends Sprite {
    public boolean isSolid = false;
    public float posX, posY;
    public int width, height;
    public int speed;
    public BufferedImage up1, up2, up3, up4, up5, up6, up7, down1, down2, down3, down4, down5, down6, down7, left1,
            left2, left3, left4, left5, left6, left7, right1, right2, right3, right4, right5, right6, right7, leftb,
            rightb, bombSprite;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public boolean dead;

    public Entity(float posX, float posY, int width, int height, int speed, String src) {
        super(src);
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