package managers;

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
  public int[] tileClicked; // center position of the tile we clicked
  public int[] normtileClicked; // normalized position of the tile we clicked
  private boolean horizontalMovement = false; // variable for informing if the character is moving horizontally
  private boolean verticalMovement = false; // variable for informing if the character is moving vertically
  public boolean enabled = false; // on/off toggle for mouse movement
  public boolean validTile = false; // a flag to check whether the tile we chose is actually reachable.
  public static MouseManager instance = null;

  // initializing arrays for the tiles the player can move in
  ArrayList<int[]> validRightTiles = new ArrayList<>();
  ArrayList<int[]> validLeftTiles = new ArrayList<>();
  ArrayList<int[]> validUpTiles = new ArrayList<>();
  ArrayList<int[]> validDownTiles = new ArrayList<>();
  ArrayList<int[]> allValidTiles = new ArrayList<>();

  // image to draw on the accessible tiles
  BufferedImage moveIndicator = Utils.loadImage("assets/moveIndicator.png");

  // gamegrid shorthand
  TileType[][] gameGrid = TileManager.build().grid;

  // bomberman shorthand
  private final Bomberman bomberman = Loop.build().bomberman;

  public static MouseManager build() {
    if (instance == null) {
      instance = new MouseManager();

    }
    return instance;
  }

  MouseManager() {

    // if the mouse is clicked,and it is above the player, then the player will move
    // up
    tileClicked = new int[2];
    normtileClicked = Utils.normalizeEntityPos(Loop.build().bomberman); // initialize it with spawn position
    Loop.build().addMouseListener(new java.awt.event.MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && !(verticalMovement || horizontalMovement)) { // only move if there is no queued movement
          if (Loop.build().gameState == util.GameState.InGame) {
            tileClicked = Utils.normalizePos(e.getX(), e.getY());
            normtileClicked[0] = tileClicked[0];
            normtileClicked[1] = tileClicked[1];
            tileClicked[0] = tileClicked[0] + Consts.tileDims / 2 - Loop.build().bomberman.width / 2;
            tileClicked[1] = tileClicked[1] + Consts.tileDims / 2 - Loop.build().bomberman.height / 2;

            // if the normalized position is not inside the valid tiles, print "error"

          }
        }

        else if (e.getButton() == MouseEvent.BUTTON3) { // Right button to place bomb
          Loop.build().bomberman.placeBomb();
        }
      }
    });
  }

  // function to populate the arrays of positions we can move into
  public void findLegalPositions(int[] normalizedBombermanPosition) {
    validRightTiles.clear();
    validLeftTiles.clear();
    validUpTiles.clear();
    validDownTiles.clear();

    //TODO : creare funzione che popola ciascun array per massima riutilizzabilitá del codice (usando x/y offset per decidere up/down/l/r)
    //TODO passaggio attraverso i muri con mouse movements
    //TODO set bomberman direction with mouse movements
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

    // extract the position of bomberman
    int[] bombermanPos = { Loop.build().bomberman.posX, Loop.build().bomberman.posY };
    int[] bombermanNormPos = Utils.normalizePos(bombermanPos[0], bombermanPos[1]);

    // get the cardinal positions he can actually reach
    findLegalPositions(bombermanNormPos);

    // if bomberman has not reached the requested position
    if (!(normtileClicked[0] == bombermanNormPos[0] && normtileClicked[1] == bombermanNormPos[1])) {
      for (int[] tile : allValidTiles) { // check the tiles he can move in
        if (Arrays.equals(tile, normtileClicked)) { // check if the tile we chose is actually reachable.
          validTile = true;
          break;
        }
      }
      if (!validTile) { // if it is not, ignore the clicked tile.
        return;
      } else { // else, continue with the code and reset the flag.
        validTile = false;
      }
    }

    if ((bombermanNormPos[0] != normtileClicked[0] && bombermanNormPos[1] != normtileClicked[1])) {
      return;
    }

    if (bombermanNormPos[0] != normtileClicked[0]) {
      horizontalMovement = true;

      if (bombermanPos[0] < tileClicked[0] - 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("D");
      }

      if (bombermanPos[0] > tileClicked[0] + 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("A");
      }

    }

    if ((bombermanPos[0] <= tileClicked[0] + 3 && bombermanPos[0] >= tileClicked[0] - 3) && !verticalMovement) {
      horizontalMovement = false;
      Loop.build().bomberman.keys.clear();
    }

    if (bombermanNormPos[1] != normtileClicked[1]) {
      verticalMovement = true;
      if (bombermanPos[1] < tileClicked[1] - 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("S");
      } else if (bombermanPos[1] > tileClicked[1] + 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("W");
      }
    }

    if ((bombermanPos[1] <= tileClicked[1] + 2 && bombermanPos[1] >= tileClicked[1] - 2)
        && !horizontalMovement) {
      verticalMovement = false;
      Loop.build().bomberman.keys.clear();
    }
  }

  public void drawLegalPositions(Graphics2D g2d) {
    if (bomberman.posX % 48 <= Consts.tileDims - bomberman.width
        && bomberman.posY % 48 <= Consts.tileDims - bomberman.height) {
      // allvalidtiles contains all the tiles that are valid for the player to move on
      allValidTiles.clear();
      allValidTiles.addAll(validDownTiles);
      allValidTiles.addAll(validUpTiles);
      allValidTiles.addAll(validLeftTiles);
      allValidTiles.addAll(validRightTiles);

      for (int[] tile : allValidTiles) {
        g2d.drawImage(moveIndicator, tile[0], tile[1], null);
      }
    }
  }

}
