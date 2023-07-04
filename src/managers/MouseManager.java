package managers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import core.Loop;
import entities.Bomberman;
import util.Consts;
import util.TileType;
import util.Utils;

public class MouseManager extends MouseAdapter {
  public int[] tileClicked;
  public int[] normtileClicked;
  private boolean HorizontalMovement = false;
  private boolean VerticalMovement = false;
  public boolean enabled = false;
  public boolean validTile = false;
  public static MouseManager instance = null;
  ArrayList<int[]> validRightTiles = new ArrayList<int[]>();
  ArrayList<int[]> validLeftTiles = new ArrayList<int[]>();
  ArrayList<int[]> validUpTiles = new ArrayList<int[]>();
  ArrayList<int[]> validDownTiles = new ArrayList<int[]>();
  ArrayList<int[]> allValidTiles = new ArrayList<int[]>();
  BufferedImage moveIndicator = Utils.loadImage("assets/moveIndicator.png");

  TileType[][] gameGrid = TileManager.build().grid;

  private Bomberman bomberman = Loop.build().bomberman;
  Color semiTransparent = new Color(0, 200, 200, 125);

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
    normtileClicked = Utils.normalizeEntityPos(Loop.build().bomberman);
    Loop.build().addMouseListener(new java.awt.event.MouseAdapter() {

      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && !(VerticalMovement || HorizontalMovement)) {
          if (Loop.build().gameState == util.GameState.InGame) {
            tileClicked = Utils.normalizePos(e.getX(), e.getY());
            normtileClicked[0] = tileClicked[0];
            normtileClicked[1] = tileClicked[1];
            tileClicked[0] = tileClicked[0] + Consts.tileDims / 2 - Loop.build().bomberman.width / 2;
            tileClicked[1] = tileClicked[1] + Consts.tileDims / 2 - Loop.build().bomberman.height / 2;

            // if the normalized position is not inside the valid tiles, print "error"

          }
        }

        else if (e.getButton() == MouseEvent.BUTTON3) {
          Loop.build().bomberman.placeBomb();
        }
      }
    });
  }

  public void findLegalPositions(int[] normalizedBombermanPosition) {
    validRightTiles.clear();
    validLeftTiles.clear();
    validUpTiles.clear();
    validDownTiles.clear();

    int normBombermanX = normalizedBombermanPosition[0];
    int normBombermanY = normalizedBombermanPosition[1];
    if (gameGrid[normBombermanY / 48][(normBombermanX + 48) / 48] == TileType.Empty) {// se la tile a destra é libera
      int[] dxTile = { normBombermanX + 48, normBombermanY };
      validRightTiles.add(dxTile);
      // while there are empty tiles to the right, add them to the list
      while (gameGrid[dxTile[1] / 48][(dxTile[0] + 48) / 48] == TileType.Empty) {
        int[] dxTile2 = { dxTile[0] + 48, dxTile[1] };
        validRightTiles.add(dxTile2);
        dxTile = dxTile2;
      }
    } else {
      validRightTiles.clear();
    }

    if (gameGrid[normBombermanY / 48][(normBombermanX - 48) / 48] == TileType.Empty) {// se la tile a sinistra é libera
      int[] sxTile = { normBombermanX - 48, normBombermanY };
      validLeftTiles.add(sxTile);
      // while there are empty tiles to the left, add them to the list
      while (gameGrid[sxTile[1] / 48][(sxTile[0] - 48) / 48] == TileType.Empty) {
        int[] sxTile2 = { sxTile[0] - 48, sxTile[1] };
        validLeftTiles.add(sxTile2);
        sxTile = sxTile2;
      }
    } else {
      validLeftTiles.clear();
    }

    if (gameGrid[(normBombermanY - 48) / 48][normBombermanX / 48] == TileType.Empty) {// se la tile sopra é libera
      int[] upTile = { normBombermanX, normBombermanY - 48 };
      validUpTiles.add(upTile);
      // while there are empty tiles above, add them to the list
      while (gameGrid[(upTile[1] - 48) / 48][upTile[0] / 48] == TileType.Empty) {
        int[] upTile2 = { upTile[0], upTile[1] - 48 };
        validUpTiles.add(upTile2);
        upTile = upTile2;
      }
    } else {
      validUpTiles.clear();
    }

    if (gameGrid[(normBombermanY + 48) / 48][normBombermanX / 48] == TileType.Empty) {// se la tile sotto é libera
      int[] downTile = { normBombermanX, normBombermanY + 48 };
      validDownTiles.add(downTile);
      // while there are empty tiles below, add them to the list
      while (gameGrid[(downTile[1] + 48) / 48][downTile[0] / 48] == TileType.Empty) {
        int[] downTile2 = { downTile[0], downTile[1] + 48 };
        validDownTiles.add(downTile2);
        downTile = downTile2;
      }
    } else {
      validDownTiles.clear();
    }

  }

  public void checkPositionReached() {

    int[] bombermanPos = { Loop.build().bomberman.posX, Loop.build().bomberman.posY };
    int[] bombermanNormPos = Utils.normalizePos(bombermanPos[0], bombermanPos[1]);
    int[] requestedtile = { normtileClicked[0], normtileClicked[1] };

    findLegalPositions(bombermanNormPos);

    if (!(requestedtile[0] == bombermanNormPos[0] && requestedtile[1] == bombermanNormPos[1])) {
      for (int[] tile : allValidTiles) {
        // if the requested tile is in the list of valid tiles, then the player can move
        if (Arrays.equals(tile, requestedtile)) {
          System.out.println("valid tile");
          validTile = true;
          break;
        }
      }
      if (!validTile) {
        return;
      } else {
        validTile = false;
      }
    }

    if ((bombermanNormPos[0] != normtileClicked[0] && bombermanNormPos[1] != normtileClicked[1])) {
      return;
    }

    if (bombermanNormPos[0] != normtileClicked[0]) {
      HorizontalMovement = true;

      if (bombermanPos[0] < tileClicked[0] - 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("D");
      }

      if (bombermanPos[0] > tileClicked[0] + 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("A");
      }

    }

    if ((bombermanPos[0] <= tileClicked[0] + 3 && bombermanPos[0] >= tileClicked[0] - 3) && !VerticalMovement) {
      HorizontalMovement = false;
      Loop.build().bomberman.keys.clear();
    }

    if (bombermanNormPos[1] != normtileClicked[1]) {
      VerticalMovement = true;
      if (bombermanPos[1] < tileClicked[1] - 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("S");
      } else if (bombermanPos[1] > tileClicked[1] + 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("W");
      }
    }

    if ((bombermanPos[1] <= tileClicked[1] + 2 && bombermanPos[1] >= tileClicked[1] - 2)
        && !HorizontalMovement) {
      VerticalMovement = false;
      Loop.build().bomberman.keys.clear();
    }
  }

  public void DrawLegalPositions(Graphics2D g2d) {
    if (bomberman.posX % 48 <= Consts.tileDims - bomberman.width
        && bomberman.posY % 48 <= Consts.tileDims - bomberman.height) {
      // allvalidtiles contains all the tiles that are valid for the player to move on
      allValidTiles.clear();
      allValidTiles.addAll(validDownTiles);
      allValidTiles.addAll(validUpTiles);
      allValidTiles.addAll(validLeftTiles);
      allValidTiles.addAll(validRightTiles);

      for (int[] tile : allValidTiles) {
        // g2d.setColor(semiTransparent);
        // g2d.fillRect(tile[0], tile[1], Consts.tileDims, Consts.tileDims);
        g2d.drawImage(moveIndicator, tile[0], tile[1], null);
      }
    }
  }

}
