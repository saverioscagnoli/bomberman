package loop;

import java.util.LinkedList;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import entities.*;
import ui.Button;
import ui.Menus;
import utils.*;

/**
 * This class handles the key events for the game.
 * It is used to determine which keys are pressed and which are not.
 * It also determines the direction of the player.
 */

public class KeyHandler extends MouseAdapter implements KeyListener {

    // variables for keys taht are currently pressed
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    public List<String> buttonPriorities = new LinkedList<>();

    // variables for determining the latest key pressed for each direction
    public String latestHorizontalKey;
    public String latestVerticalKey;

    private GameLoop gameLoop;

    public KeyHandler(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        gameLoop.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (gameLoop.gameState == 1) {
                    for (Button btn : Menus.mainMenu.buttons) {
                        if (Utils.buttonClick(x, y, btn)) {
                            switch (btn.uuid) {
                                case "s":
                                    gameLoop.gameState = 2;
                                    break;
                                case "q":
                                        System.exit(0);
                                    break;
    
                            }
                        }
                    }
                }
                if (gameLoop.gameState == 4) {
                    for (Button btn : Menus.pauseMenu.buttons) {
                        if (Utils.buttonClick(x, y, btn)) {
                            switch (btn.uuid) {
                                case "r": {
                                    gameLoop.gameState = 2;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    // Method to update the variables based on the current key presses
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_W: {
                wPressed = true;
                latestVerticalKey = "W";
                if (buttonPriorities.contains("W"))
                    break;
                buttonPriorities.add(0, "W");
                break;
            }

            case KeyEvent.VK_A: {
                aPressed = true;
                latestHorizontalKey = "A";
                if (buttonPriorities.contains("A"))
                    break;
                buttonPriorities.add(0, "A");
                break;
            }

            case KeyEvent.VK_S: {
                latestVerticalKey = "S";
                sPressed = true;
                if (buttonPriorities.contains("S"))
                    break;
                buttonPriorities.add(0, "S");
                break;
            }

            case KeyEvent.VK_D: {
                latestHorizontalKey = "D";
                dPressed = true;
                if (buttonPriorities.contains("D"))
                    break;
                buttonPriorities.add(0, "D");
                break;
            }

            case KeyEvent.VK_SPACE: {
                int[] gridPos = Utils.getGridPos(gameLoop.character);
                GameLoop.entities.add(new Entity(gridPos[0], gridPos[1], Consts.tileDims, Consts.tileDims, 0));
                break;
            }

            case KeyEvent.VK_ESCAPE: {
                if (this.gameLoop.gameState == 2) {
                    this.gameLoop.gameState = 4;
                } else if (this.gameLoop.gameState == 4) {
                    this.gameLoop.gameState = 2;
                }
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
            buttonPriorities.remove("W");
            if (sPressed) {
                latestVerticalKey = "S";
            } else {
                latestVerticalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_A) {
            aPressed = false;
            buttonPriorities.remove("A");
            if (dPressed) {
                latestHorizontalKey = "D";
            } else {
                latestHorizontalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_S) {
            sPressed = false;
            buttonPriorities.remove("S");
            if (wPressed) {
                latestVerticalKey = "W";
            } else {
                latestVerticalKey = "";
            }
        } else if (keyCode == KeyEvent.VK_D) {
            dPressed = false;
            buttonPriorities.remove("D");
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
