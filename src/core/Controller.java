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

    /**
     * Constructs a new Controller instance.
     * 
     * @param loop the Loop object managing the game loop
     */
    private Controller(Loop loop) {
        this.bomberman = loop.bomberman;
    }

    /**
     * Returns the singleton instance of the Controller, creating it if necessary.
     * 
     * @param loop the Loop object managing the game loop
     * @return the Controller instance
     */
    public static synchronized Controller build(Loop loop) {
        if (instance == null) {
            instance = new Controller(loop);
        }
        return instance;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) { // switch for the keypresses
            case KeyEvent.VK_W: { // up key
                if (MouseManager.build().enabled == false) {
                    wPressed = true;
                    latestVerticalKey = "W";
                    if (this.bomberman.keys.contains("W"))
                        break;
                    this.bomberman.keys.add(0, "W");
                    break;
                }
            }

            case KeyEvent.VK_A: { // left key
                if (MouseManager.build().enabled == false) {
                    aPressed = true;
                    latestHorizontalKey = "A";
                    if (this.bomberman.keys.contains("A"))
                        break;
                    this.bomberman.keys.add(0, "A");
                    break;
                }
            }

            case KeyEvent.VK_S: { // down key
                if (MouseManager.build().enabled == false) {
                    latestVerticalKey = "S";
                    sPressed = true;
                    if (this.bomberman.keys.contains("S"))
                        break;
                    this.bomberman.keys.add(0, "S");
                }
                break;
            }

            case KeyEvent.VK_D: { // right key
                if (MouseManager.build().enabled == false) {
                    latestHorizontalKey = "D";
                    dPressed = true;
                    if (this.bomberman.keys.contains("D"))
                        break;
                    this.bomberman.keys.add(0, "D");
                }
                break;
            }
            case KeyEvent.VK_M: { // mouse enabling key
                if (MouseManager.build().enabled == false) {
                    Loop.build().bomberman.keys.clear(); // stops the bomberman from moving

                    // sets the current position of bomberman to the target, so it stays in place.
                    MouseManager.build().tileClicked = Utils.normalizeEntityPos(Loop.build().bomberman);
                    MouseManager.build().normtileClicked = Utils.normalizeEntityPos(Loop.build().bomberman);
                }
                if (MouseManager.build().enabled == true) {
                    // clears the keys so no phantom movement occurs
                    Loop.build().bomberman.keys.clear();
                }
                // toggles mouse movements
                MouseManager.build().enabled = !MouseManager.build().enabled;

                // sets bomberman to the centered position of the tile so it doesn't get stuck
                bomberman.posX = Utils.normalizeEntityPos(bomberman)[0] + Consts.tileDims / 2 - bomberman.width / 2;
                bomberman.posY = Utils.normalizeEntityPos(bomberman)[1] + Consts.tileDims / 2 - bomberman.height / 2;

                // animates the mouse button
                Loop.build().isMouseChanging = !Loop.build().isMouseChanging;
                break;
            }

            case KeyEvent.VK_SPACE: { // bomb placing key
                this.bomberman.placeBomb();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {
            wPressed = false;
            this.bomberman.keys.remove("W");
            if (sPressed) {
                latestVerticalKey = "S";
            } else {
                latestVerticalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_A) {
            aPressed = false;
            this.bomberman.keys.remove("A");
            if (dPressed) {
                latestHorizontalKey = "D";
            } else {
                latestHorizontalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_S) {
            sPressed = false;
            this.bomberman.keys.remove("S");
            if (wPressed) {
                latestVerticalKey = "W";
            } else {
                latestVerticalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_D) {
            dPressed = false;
            this.bomberman.keys.remove("D");
            if (aPressed) {
                latestHorizontalKey = "A";
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
