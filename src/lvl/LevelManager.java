package lvl;

import java.util.Random;

import entities.Obstacle;
import loop.GameLoop;
import util.Consts;

public class LevelManager {
  public static void genLevel(int n) {
    genWall();
    genGrid();
  }

  public static void genWall() {
    for (int i = 0; i < Consts.screenWidth; i += Consts.tileDims) {
      Obstacle ob = new Obstacle(i, 0, false);
      GameLoop.entities.add(ob);
    }
    for (int i = 0; i < Consts.screenWidth; i += Consts.tileDims) {
      Obstacle ob = new Obstacle(i, Consts.screenHeight - Consts.tileDims, false);
      GameLoop.entities.add(ob);
    }
    for (int i = 0; i < Consts.screenHeight; i += Consts.tileDims) {
      Obstacle ob = new Obstacle(0, i, false);
      GameLoop.entities.add(ob);
    }
    for (int i = 0; i < Consts.screenHeight; i += Consts.tileDims) {
      Obstacle ob = new Obstacle(Consts.screenWidth - Consts.tileDims, i, false);
      GameLoop.entities.add(ob);
    }
  }

  public static void genGrid() {
    for (int i = 0; i < Consts.screenWidth; i += Consts.tileDims * 2) {
      for (int j = 0; j < Consts.screenHeight; j += Consts.tileDims * 2) {
        Obstacle ob = new Obstacle(i, j, false);

        GameLoop.entities.add(ob);
      }
    }

    for (int i = Consts.tileDims; i < Consts.screenWidth - Consts.tileDims; i += Consts.tileDims * 2) {
      for (int j = Consts.tileDims; j < Consts.screenHeight - Consts.tileDims; j += Consts.tileDims * 2) {
        Random rand = new Random();
        Boolean spawned = rand.nextInt(5) == 3;
        if (spawned) {
          Obstacle ob = new Obstacle(i, j, true);
          GameLoop.entities.add(ob);
        }
      }

    }
  }
}
