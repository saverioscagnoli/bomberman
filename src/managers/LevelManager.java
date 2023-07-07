package managers;

import core.Loop;
import entities.bosses.ClownMask;
import entities.bosses.FaralsBoss;
import util.GameState;
import util.Utils;

public class LevelManager {
  // singleton pattern
  private static LevelManager instance = null;

  public static LevelManager build() {
    if (instance == null) {
      instance = new LevelManager();
    }
    return instance;
  }

  private LevelManager() {
  }

  public void loadNextLevel() {

    String newLevel = SaveManager.readProgress().get("level");
    int newLevelInt = Integer.parseInt(newLevel);
    loadLevel(newLevelInt);
  }

  public void reloadLevel() {
    String newLevel = SaveManager.readProgress().get("level");
    int newLevelInt = Integer.parseInt(newLevel);
    loadLevel(newLevelInt);
  }

  public void loadLevel(int level) {
    // convert level to string
    String newLevel = Integer.toString(level);
    String newLevelString = "levels/level-" + newLevel + ".lvl";
    if (newLevel.equals("3") || newLevel.equals("6")) {
      newLevelString = "levels/bosslevel.lvl";
      EnemyManager.build().enemies.clear();
    }
    TileManager.build().grid = Utils.readLevel(newLevelString);
    TileManager.build().readGrid();
    Loop.build().bomberman.posX = 80;
    Loop.build().bomberman.posY = 80;
    if (newLevel.equals("3") && EnemyManager.build().enemies.size() == 0) {
      EnemyManager.build().enemies.add(new ClownMask(400, 300));
    } else if (newLevel.equals("6") && EnemyManager.build().enemies.size() == 0) {
      EnemyManager.build().enemies.add(new FaralsBoss(400, 300));
    } else {
      EnemyManager.build().instanciateEnemies(5);
    }
    BombManager.build().bombs.clear();
    PowerupManager.build().powerups.clear();
  }
}
