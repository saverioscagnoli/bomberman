package loop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import entities.*;
import menu.Button;

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

    // variables for determining the latest key pressed for each direction
    public String latestHorizontalKey;
    public String latestVerticalKey;
    private GameLoop gameLoop;

    public KeyHandler(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        gameLoop.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Button btn : gameLoop.buttons) {
                    int x = e.getX();
                    int y = e.getY();
                    if ((x >= btn.x && x <= btn.x + btn.width) && (y >= btn.y && y <= btn.y + btn.height)) {
                        gameLoop.gameState = 2;
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
                break;
            }

            case KeyEvent.VK_A: {
                aPressed = true;
                latestHorizontalKey = "A";
                break;
            }

            case KeyEvent.VK_S: {
                latestVerticalKey = "S";
                sPressed = true;
                break;
            }

            case KeyEvent.VK_D: {
                latestHorizontalKey = "D";
                dPressed = true;
                break;
            }

            case KeyEvent.VK_SPACE: {
                GameLoop.entities.add(new Entity((float) (gameLoop.character.posX + gameLoop.character.width * 0.5),
                        (float) (gameLoop.character.posY + gameLoop.character.height * 0.5), 30, 30, 0));
                break;
            }

            case KeyEvent.VK_ESCAPE: {
                System.exit(0);
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
        // ...
    }
}
