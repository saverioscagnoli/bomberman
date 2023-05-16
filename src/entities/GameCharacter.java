package entities;

import java.awt.Color;
import java.awt.Graphics2D;

import loop.GameLoop;
import loop.Controller;

public class GameCharacter extends Entity {

    Controller keyHandler;
    GameLoop gameLoop;

    public GameCharacter(float posX, float posY, int width, int height, int speed, Controller keyHandler,
            GameLoop gameLoop) {
        super(posX, posY, width, height, speed);
        this.gameLoop = gameLoop;
        this.keyHandler = keyHandler;
    }

    public void update() {

        if (keyHandler.buttonPriorities.isEmpty() == false) {
            switch (keyHandler.buttonPriorities.get(0)) {
                case "A":
                    posX -= speed;
                    break;
                case "D":
                    posX += speed;
                    break;
                case "W":
                    posY -= speed;
                    break;
                case "S":
                    posY += speed;
                    break;
            }
        }
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(Color.GREEN); // Set the color to red for character
        g2d.fillRect((int) this.posX, (int) this.posY, this.width, this.height); // Draw the character
    }
}
