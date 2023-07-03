package core;

import entities.Bomberman;
import managers.MouseManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import util.*;

/**
 * This class handles the key events for the game.
 * It is used to determine which keys are pressed and which are not.
 * It also determines the direction of the player.
 */

public class Controller extends MouseAdapter implements KeyListener {
    private static Controller instance = null;

    // variables for keys that are currently pressed
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    // variables for determining the latest key pressed for each direction
    public String latestHorizontalKey;
    public String latestVerticalKey;

    private Bomberman bomberman;

    private Controller(Loop loop) {
        this.bomberman = loop.bomberman;
    }

    public static synchronized Controller build(Loop loop) {
        if (instance == null) {
            instance = new Controller(loop);
        }
        return instance;
    }

    // Method to update the variables based on the current key presses
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_W: {
                wPressed = true;
                latestVerticalKey = "W";
                if (this.bomberman.keys.contains("W"))
                    break;
                this.bomberman.sprite.setAnimation("up");
                this.bomberman.keys.add(0, "W");
                break;
            }

            case KeyEvent.VK_A: {
                aPressed = true;
                latestHorizontalKey = "A";
                if (this.bomberman.keys.contains("A"))
                    break;
                this.bomberman.sprite.setAnimation("left");
                this.bomberman.keys.add(0, "A");
                break;
            }

            case KeyEvent.VK_S: {
                latestVerticalKey = "S";
                sPressed = true;
                if (this.bomberman.keys.contains("S"))
                    break;
                this.bomberman.sprite.setAnimation("down");
                this.bomberman.keys.add(0, "S");
                break;
            }

            case KeyEvent.VK_D: {
                latestHorizontalKey = "D";
                dPressed = true;
                if (this.bomberman.keys.contains("D"))
                    break;
                this.bomberman.sprite.setAnimation("right");
                this.bomberman.keys.add(0, "D");
                break;
            }
            case KeyEvent.VK_M: {
                MouseManager.build().enabled = !MouseManager.build().enabled;
                Loop.build().changeButton = true;
                break;
            }

            case KeyEvent.VK_SPACE: {
                this.bomberman.placeBomb();
                Utils.playSound(Consts.soundPath + "place-bomb.wav");
                break;
            }
        }
    }

    // Method to update the variables based on the current key releases
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {
            wPressed = false;
            this.bomberman.keys.remove("W");
            if (sPressed) {
                latestVerticalKey = "S";
                this.bomberman.sprite.setAnimation("down");
            } else {
                latestVerticalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_A) {
            aPressed = false;
            this.bomberman.keys.remove("A");
            if (dPressed) {
                latestHorizontalKey = "D";
                this.bomberman.sprite.setAnimation("right");
            } else {
                latestHorizontalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_S) {
            sPressed = false;
            this.bomberman.keys.remove("S");
            if (wPressed) {
                latestVerticalKey = "W";
                this.bomberman.sprite.setAnimation("up");
            } else {
                latestVerticalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_D) {
            dPressed = false;
            this.bomberman.keys.remove("D");
            if (aPressed) {
                latestHorizontalKey = "A";
                this.bomberman.sprite.setAnimation("left");
            } else {
                latestHorizontalKey = "";
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ...
    }
}
