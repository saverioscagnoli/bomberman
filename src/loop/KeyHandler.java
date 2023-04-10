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
    public String latestHorizontalKey;
    public String latestVerticalKey;
    private GameLoop gameLoop;


    public KeyHandler(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }


    // Method to update the variables based on the current key presses
    // TODO make all these ifs a switch statement
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode){
        case KeyEvent.VK_W: {
            wPressed = true;
            latestVerticalKey = "W";
            System.out.println("W pressed");
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
            System.out.println("Space pressed");
            GameLoop.entities.add(new Entity(gameLoop.character.posX,gameLoop.character.posY, 30, 30, 0));
            System.out.println(GameLoop.entities.size());
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

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }


}
