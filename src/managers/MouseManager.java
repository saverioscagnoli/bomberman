package managers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import core.Loop;
import util.Consts;
import util.Utils;

public class MouseManager extends MouseAdapter {
  public int[] tileClicked;
  public int[] normtileClicked;
  public boolean enabled = false;
  public static MouseManager instance = null;

  public static MouseManager build() {
    if (instance == null) {
      instance = new MouseManager();
    }
    return instance;
  }

  MouseManager() {
    // if the mouse is clicked and it is above the player, then the player will move
    // up
    tileClicked = new int[2];
    normtileClicked = new int[2];
    Loop.build().addMouseListener(new java.awt.event.MouseAdapter() {

      public void mouseClicked(MouseEvent e) {

        if (Loop.build().gameState == util.GameState.InGame) {
          tileClicked = Utils.normalizePos(e.getX(), e.getY());
          normtileClicked[0] = tileClicked[0];
          normtileClicked[1] = tileClicked[1];
          System.out.println(tileClicked[0] + " " + tileClicked[1]);
          tileClicked[0] = tileClicked[0] + Consts.tileDims / 2 - Loop.build().bomberman.width / 2;
          tileClicked[1] = tileClicked[1] + Consts.tileDims / 2 - Loop.build().bomberman.height / 2;
        }
      }
    });
  }

  public void checkPositionReached() {
    int[] bombermanPos = { Loop.build().bomberman.posX, Loop.build().bomberman.posY };
    int[] bombermanNormPos = Utils.normalizePos(bombermanPos[0], bombermanPos[1]);

    if (bombermanNormPos[0] != tileClicked[0]) {
      if (bombermanPos[0] < tileClicked[0] - 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("D");
      } else if (bombermanPos[0] > tileClicked[0] + 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("A");
      } else if (bombermanPos[0] <= tileClicked[0] + 2 || bombermanPos[0] >= tileClicked[0] - 2) {
        Loop.build().bomberman.keys.clear();
      }

    }

    else if (bombermanNormPos[1] != tileClicked[1]) {
      if (bombermanPos[1] < tileClicked[1] - 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("S");
      } else if (bombermanPos[1] > tileClicked[1] + 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("W");
      } else if (bombermanPos[1] <= tileClicked[1] + 2 || bombermanPos[1] >= tileClicked[1] - 2) {
        Loop.build().bomberman.keys.clear();
      }
    }
  }

}
