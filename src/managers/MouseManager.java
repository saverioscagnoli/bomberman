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

/**
 * 
 * The MouseManager class is responsible for handling mouse input and managing
 * player movement.
 * 
 * It tracks the position of the clicked tiles, determines valid movement
 * directions, and updates the player's movement accordingly.
 * 
 * This class follows the singleton design pattern to ensure only one instance
 * is created.
 */
public class MouseManager extends MouseAdapter {
  public int[] tileClicked; // center position of the tile we clicked
  public int[] normtileClicked; // normalized position of the tile we clicked
  public boolean horizontalMovement = false; // variable for informing if the character is moving horizontally
  public boolean verticalMovement = false; // variable for informing if the character is moving vertically
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

  // tileManager.grid shorthand
  TileManager tileManager = TileManager.build();

  // bomberman shorthand
  private final Bomberman bomberman = Loop.build().bomberman;

  /**
   * 
   * Retrieves the singleton instance of MouseManager.
   * 
   * @return The MouseManager instance.
   */
  public static MouseManager build() {
    if (instance == null) {
      instance = new MouseManager();
    }
    return instance;
  }

  /**
   * 
   * Private constructor to enforce the singleton pattern.
   */
  MouseManager() {
    tileClicked = new int[2];
    normtileClicked = Utils.normalizeEntityPos(Loop.build().bomberman); // initialize it with spawn position
    Loop.build().addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        System.out.println(verticalMovement + " " + horizontalMovement);
        if (e.getButton() == MouseEvent.BUTTON1 && !(verticalMovement || horizontalMovement)) { // only move if there is
                                                                                                // no queued movement
          if (Loop.build().gameState == util.GameState.InGame) {
            tileClicked = Utils.normalizePos(e.getX(), e.getY());
            normtileClicked[0] = tileClicked[0];
            normtileClicked[1] = tileClicked[1];
            tileClicked[0] = tileClicked[0] + Consts.tileDims / 2 - Loop.build().bomberman.width / 2;
            tileClicked[1] = tileClicked[1] + Consts.tileDims / 2 - Loop.build().bomberman.height / 2;
          }
        } else if (e.getButton() == MouseEvent.BUTTON3) { // Right button to place bomb
          Loop.build().bomberman.placeBomb();
        }
      }
    });
  }

  /**
   * 
   * Checks the cardinal direction tiles reachable from the given position.
   * 
   * @param normBombermanX The normalized x-coordinate of the Bomberman's
   *                       position.
   * 
   * @param normBombermanY The normalized y-coordinate of the Bomberman's
   *                       position.
   * 
   * @param xOffset        The x-offset to determine the cardinal direction.
   * 
   * @param yOffset        The y-offset to determine the cardinal direction.
   * 
   * @return The list of valid tiles in the specified cardinal direction.
   */
  public ArrayList<int[]> CardinalDirectionChecker(int normBombermanX, int normBombermanY, int xOffset, int yOffset) {
    ArrayList<int[]> foundValidTiles = new ArrayList<>();

    if (tileManager.grid[(normBombermanY + yOffset) / 48][(normBombermanX + xOffset) / 48] == TileType.Empty
        || tileManager.grid[(normBombermanY + yOffset) / 48][(normBombermanX + xOffset) / 48] == TileType.PowerUp
        || (bomberman.passThroughWalls == true
            && tileManager.grid[(normBombermanY + yOffset) / 48][(normBombermanX + xOffset)
                / 48] == TileType.Obstacle)) {
      int[] dxTile = { normBombermanX + xOffset, normBombermanY + yOffset };
      foundValidTiles.add(dxTile);
      // while there are empty tiles to the right, add them to the list
      while (tileManager.grid[(dxTile[1] + yOffset) / 48][(dxTile[0] + xOffset) / 48] == TileType.Empty
          || tileManager.grid[(dxTile[1] + yOffset) / 48][(dxTile[0] + xOffset) / 48] == TileType.PowerUp
          || (bomberman.passThroughWalls == true
              && tileManager.grid[(dxTile[1] + yOffset) / 48][(dxTile[0] + xOffset) / 48] == TileType.Obstacle)) {
        int[] dxTile2 = { dxTile[0] + xOffset, dxTile[1] + yOffset };
        foundValidTiles.add(dxTile2);
        dxTile = dxTile2;
      }
    } else {
      foundValidTiles.clear();
    }
    return foundValidTiles;
  }

  /**
   * 
   * Populates the arrays of valid positions the player can move into.
   * 
   * @param normalizedBombermanPosition The normalized position of the Bomberman.
   */
  public void findLegalPositions(int[] normalizedBombermanPosition) {
    validRightTiles.clear();
    validLeftTiles.clear();
    validUpTiles.clear();
    validDownTiles.clear();

    int normBombermanX = normalizedBombermanPosition[0];
    int normBombermanY = normalizedBombermanPosition[1];

    validRightTiles = CardinalDirectionChecker(normBombermanX, normBombermanY, 48, 0);
    validLeftTiles = CardinalDirectionChecker(normBombermanX, normBombermanY, -48, 0);
    validUpTiles = CardinalDirectionChecker(normBombermanX, normBombermanY, 0, -48);
    validDownTiles = CardinalDirectionChecker(normBombermanX, normBombermanY, 0, 48);
  }

  /**
   * 
   * Checks if the player has reached the requested position and updates the
   * movement flags accordingly.
   */
  public void checkPositionReached() {
    int[] bombermanPos = { Loop.build().bomberman.posX, Loop.build().bomberman.posY };
    int[] bombermanNormPos = Utils.normalizePos(bombermanPos[0], bombermanPos[1]);
    findLegalPositions(bombermanNormPos);

    if (!(normtileClicked[0] == bombermanNormPos[0] && normtileClicked[1] == bombermanNormPos[1])) {
      for (int[] tile : allValidTiles) {
        if (Arrays.equals(tile, normtileClicked)) {
          validTile = true;
          break;
        }
      }
      if (!validTile) {
        return; // if it is not, ignore the clicked tile.
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
        bomberman.sprite.setAnimation("right");
      } else if (bombermanPos[0] > tileClicked[0] + 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("A");
        bomberman.sprite.setAnimation("left");
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
        bomberman.sprite.setAnimation("down");
      } else if (bombermanPos[1] > tileClicked[1] + 2) {
        Loop.build().bomberman.keys.clear();
        Loop.build().bomberman.keys.add("W");
        bomberman.sprite.setAnimation("up");
      }
    }

    if ((bombermanPos[1] <= tileClicked[1] + 2 && bombermanPos[1] >= tileClicked[1] - 2) && !horizontalMovement) {
      verticalMovement = false;
      Loop.build().bomberman.keys.clear();
    }
  }

  /**
   * 
   * Draws the indicators on the valid tiles the player can move into.
   * 
   * @param g2d The graphics object to draw on.
   */
  public void drawLegalPositions(Graphics2D g2d) {
    if (bomberman.posX % 48 <= Consts.tileDims - bomberman.width
        && bomberman.posY % 48 <= Consts.tileDims - bomberman.height) {
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