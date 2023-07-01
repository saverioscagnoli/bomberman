package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;

import util.GameState;
import util.Utils;

public class MenuHandler extends MouseAdapter implements KeyListener {
  private static MenuHandler instance = null;
  private Loop loop;

  private MenuHandler(Loop loop) {
    this.loop = loop;
  }

  public static synchronized MenuHandler build(Loop loop) {
    if (instance == null) {
      instance = new MenuHandler(loop);
    }
    return instance;
  }

  private void setArrow() {
    if (this.loop.arrowY == 555) {
      this.loop.arrowY = 600;
    } else {
      this.loop.arrowY = 555;
    }
  }

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
        }
        break;
      }

      default: {
        break;
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
