package entities;

import java.awt.Graphics2D;

import ui.Sprite;

public abstract class Entity {
    public boolean isSolid = false;
    public int posX, posY;
    public int width, height;
    public int speed;
    public Sprite sprite;
    public boolean dead;

    public Entity(int posX, int posY, int width, int height, int speed, Sprite sprite) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.sprite = sprite;
        this.dead = false;
    }

    public abstract void die();

    public abstract void update(int elapsed);

    public abstract void render(Graphics2D g2d);
}