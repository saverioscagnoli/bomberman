package loop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entities.*;

/**
 * This class handles the key events for the game.
 * It is used to determine which keys are pressed and which are not.
 * It also determines the direction of the player.
 */

public class KeyHandler implements KeyListener {

    // variables for keys taht are currently pressed
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    // variables for determining the latest key pressed for each direction
    private String latestHorizontalKey;
    private String latestVerticalKey;

    // variables for the direction of the player
    // TODO make this properties of the player itself
    public float playerX = 0;
    public float playerY = 0;

    // Method to update the variables based on the current key presses
    // TODO make all these ifs a switch statement
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode){
        case KeyEvent.VK_W: {
            wPressed = true;
            latestVerticalKey = "W";
            break;
        }

        case KeyEvent.VK_A : {
            aPressed = true;
            latestHorizontalKey = "A";
            break;
        }

        case KeyEvent.VK_S : {
            latestVerticalKey = "S";
            sPressed = true;
            break;
        }

        case KeyEvent.VK_D : {
            latestHorizontalKey = "D";
            dPressed = true;
            break;
        }

        case KeyEvent.VK_SPACE : {
            // TODO : Change keybind for the bomb placement
            // TODO : Make the bomb place at the center of the player location
            GameLoop.entities.add(new Entity(GameLoop.characterX, GameLoop.characterY, 10, 10, 0, 0));
            break;
        }

        case KeyEvent.VK_ESCAPE : {
            System.exit(0);
            break;
        }

        default: {
            System.out.println("Key not recognized");
            break;
        }
        }


        updateDirection();
    }


    // Method to update the variables based on the current key releases
    // TODO make all these ifs a switch statement
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {
            wPressed = false;
            if (sPressed) {
                latestVerticalKey = "S";
            } else {
                latestVerticalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_A) {
            aPressed = false;
            if (dPressed) {
                latestHorizontalKey = "D";
            } else {
                latestHorizontalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_S) {
            sPressed = false;
            if (wPressed) {
                latestVerticalKey = "W";
            } else {
                latestVerticalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_D) {
            dPressed = false;
            if (aPressed) {
                latestHorizontalKey = "A";
            } else {
                latestHorizontalKey = "";
            }
        }

        updateDirection();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }

    private void updateDirection() {
        playerX = 0;
        playerY = 0;
    
        switch(latestHorizontalKey) {
            case "A":
                playerX = -1;
                break;
            case "D":
                playerX = 1;
                break;
        }
        switch(latestVerticalKey) {
            case "W":
                playerY = -1;
                break;
            case "S":
                playerY = 1;
                break;
        }
    }
}
