package entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Entity {
    public float posX, posY;
    public int width, height;
    public int speed;

    public Entity(float posX, float posY, int width, int height, int speed) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void update() {
    }

    public void render(Graphics2D g2d) {
    }
}