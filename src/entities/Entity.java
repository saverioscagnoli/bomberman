package entities;

public class Entity{
    public float posX,posY;
    public int width,height;
    public int speedX,speedY;

    public Entity(float posX, float posY, int width, int height, int speedX, int speedY) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public void update(){
        posX += speedX;
        posY += speedY;
    }
}