package loop;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    public float playerX = 0;
    public float playerY = 0;

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {
            wPressed = true;
        } else if (keyCode == KeyEvent.VK_A) {
            aPressed = true;
        } else if (keyCode == KeyEvent.VK_S) {
            sPressed = true;
        } else if (keyCode == KeyEvent.VK_D) {
            dPressed = true;
        }

        updateDirection();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {
            wPressed = false;
        } else if (keyCode == KeyEvent.VK_A) {
            aPressed = false;
        } else if (keyCode == KeyEvent.VK_S) {
            sPressed = false;
        } else if (keyCode == KeyEvent.VK_D) {
            dPressed = false;
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

        if (wPressed) {
            playerY = -1;
        } else if (sPressed) {
            playerY = 1;
        }

        if (aPressed) {
            playerX = -1;
        } else if (dPressed) {
            playerX = 1;
        }
    }
}
