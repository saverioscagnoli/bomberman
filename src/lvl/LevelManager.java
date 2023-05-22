package lvl;
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
      Obstacle ob = new Obstacle(i, 0);
      ob.normalizePos();
      GameLoop.entities.add(ob);
    }
    for (int i = 0; i < Consts.screenWidth; i += Consts.tileDims) {
      Obstacle ob = new Obstacle(i, Consts.screenHeight - Consts.tileDims);
      ob.normalizePos();
      GameLoop.entities.add(ob);
    }
    for (int i = 0; i < Consts.screenHeight; i += Consts.tileDims) {
      Obstacle ob = new Obstacle(0, i);
      ob.normalizePos();
      GameLoop.entities.add(ob);
    }
    for (int i = 0; i < Consts.screenHeight; i += Consts.tileDims) {
      Obstacle ob = new Obstacle(Consts.screenWidth - Consts.tileDims, i);
      ob.normalizePos();
      GameLoop.entities.add(ob);
    }
  }

  public static void genGrid() {
    for (int i = 0; i < Consts.screenWidth; i += Consts.tileDims * 2) {
      for (int j = 0; j < Consts.screenHeight; j += Consts.tileDims * 2) {
        Obstacle ob = new Obstacle(i, j);
        ob.normalizePos();
        GameLoop.entities.add(ob);
      } 
    }
  }
}
