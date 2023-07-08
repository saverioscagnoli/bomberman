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
                if (MouseManager.build().enabled == false) {
                    wPressed = true;
                    latestVerticalKey = "W";
                    if (this.bomberman.keys.contains("W"))
                        break;
                    this.bomberman.keys.add(0, "W");
                    break;
                }
            }

            case KeyEvent.VK_A: {
                if (MouseManager.build().enabled == false) {
                    aPressed = true;
                    latestHorizontalKey = "A";
                    if (this.bomberman.keys.contains("A"))
                        break;
                    this.bomberman.keys.add(0, "A");
                    break;
                }
            }

            case KeyEvent.VK_S: {
                if (MouseManager.build().enabled == false) {
                    latestVerticalKey = "S";
                    sPressed = true;
                    if (this.bomberman.keys.contains("S"))
                        break;
                    this.bomberman.keys.add(0, "S");
                }
                break;
            }

            case KeyEvent.VK_D: {
                if (MouseManager.build().enabled == false) {
                    latestHorizontalKey = "D";
                    dPressed = true;
                    if (this.bomberman.keys.contains("D"))
                        break;
                    this.bomberman.keys.add(0, "D");
                }
                break;
            }
            case KeyEvent.VK_F: { // TODO : Levarlo
                Loop.build().bomberman.win();
                break;
            }
            case KeyEvent.VK_M: {
                if (MouseManager.build().enabled == false) {
                    Loop.build().bomberman.keys.clear();
                    MouseManager.build().tileClicked = Utils.normalizeEntityPos(Loop.build().bomberman);
                    MouseManager.build().normtileClicked = Utils.normalizeEntityPos(Loop.build().bomberman);
                }
                if (MouseManager.build().enabled == true) {
                    Loop.build().bomberman.keys.clear();
                }
                MouseManager.build().enabled = !MouseManager.build().enabled;
                bomberman.posX = Utils.normalizeEntityPos(bomberman)[0] + Consts.tileDims / 2 - bomberman.width / 2;
                bomberman.posY = Utils.normalizeEntityPos(bomberman)[1] + Consts.tileDims / 2 - bomberman.height / 2;
                Loop.build().isMouseChanging = !Loop.build().isMouseChanging;
                break;
            }

            case KeyEvent.VK_SPACE: {
                this.bomberman.placeBomb();
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
