package entities;

import loop.GameLoop;
import loop.KeyHandler;

public class GameCharacter extends Entity {

    KeyHandler keyHandler;
    GameLoop gameLoop;

    public GameCharacter(float posX, float posY, int width, int height, int speed, KeyHandler keyHandler, GameLoop gameLoop) {
        super(posX, posY, width, height, speed);
        this.gameLoop = gameLoop;
        this.keyHandler = keyHandler;
    }
    
    public void update() {
        if (keyHandler.latestHorizontalKey != null) {
            switch (keyHandler.latestHorizontalKey) {
                case "A":
                    posX -= speed;
                    break;
                case "D":
                    posX += speed;
                    break;
            }
        }
        
        if (keyHandler.latestVerticalKey != null) {
            switch (keyHandler.latestVerticalKey) {
                case "W":
                    posY -= speed;
                    break;
                case "S":
                    posY += speed;
                    break;
            }
        }
    }
    }
