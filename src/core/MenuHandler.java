package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;

import util.GameState;
import util.Utils;

/**
 * The MenuHandler class is responsible for handling menu interactions and key
 * events in the game.
 * It implements the KeyListener interface to handle keyboard input and
 * MouseAdapter for mouse events.
 */
public class MenuHandler extends MouseAdapter implements KeyListener {
  private static MenuHandler instance = null;
  private Loop loop;

  /**
   * Constructs a MenuHandler object with the specified Loop instance.
   * 
   * @param loop The Loop instance for handling menu interactions.
   */
  private MenuHandler(Loop loop) {
    this.loop = loop;
  }

  /**
   * Creates a MenuHandler instance if it doesn't exist and returns it.
   * 
   * @param loop The Loop instance for handling menu interactions.
   * @return The MenuHandler instance.
   */
  public static synchronized MenuHandler build(Loop loop) {
    if (instance == null) {
      instance = new MenuHandler(loop);
    }
    return instance;
  }

  /**
   * Sets the position of the menu arrow.
   */
  private void setArrow() {
    if (this.loop.arrowY == 555) {
      this.loop.arrowY = 600;
    } else {
      this.loop.arrowY = 555;
    }
  }

  /**
   * Handles key press events.
   * 
   * @param e The KeyEvent object representing the key press event.
   */
  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_UP:
      case KeyEvent.VK_DOWN:
        Utils.playSound("assets/sounds/menu-arrow-move.wav");
        this.setArrow();
        this.loop.repaint();
        break;
      case KeyEvent.VK_ENTER: {
        if (this.loop.gameState == GameState.Menu) {
          if (this.loop.arrowY == 555) {
            this.loop.setState(GameState.InGame);
          } else {
            this.loop.setState(GameState.Stats);
          }
        }
        break;
      }

      case KeyEvent.VK_ESCAPE: {
        if (this.loop.gameState == GameState.Pause) {
          this.loop.setState(GameState.InGame);
        } else if (this.loop.gameState == GameState.InGame) {
          this.loop.setState(GameState.Pause);
        } else if (this.loop.gameState == GameState.Menu) {
          System.exit(0);
        } else if (this.loop.gameState == GameState.Stats) {
          this.loop.setState(GameState.Menu);
        }
        break;
      }

      default: {
        break;
      }
    }
  }

  /**
   * Handles key typed events.
   * 
   * @param e The KeyEvent object representing the key typed event.
   */
  @Override
  public void keyTyped(KeyEvent e) {
  }

  /**
   * Handles key release events.
   * 
   * @param e The KeyEvent object representing the key release event.
   */
  @Override
  public void keyReleased(KeyEvent e) {
  }
}
