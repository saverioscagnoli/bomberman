package managers;

import core.Loop;
import entities.bosses.ClownMask;
import entities.bosses.FaralsBoss;
import util.GameState;
import util.Utils;

/**
 * 
 * The LevelManager class handles loading and reloading game levels.
 * 
 * It uses the singleton pattern to ensure only one instance is created.
 */
public class LevelManager {
  private static LevelManager instance = null;

  /**
   * 
   * Retrieves the singleton instance of LevelManager.
   * 
   * @return The LevelManager instance.
   */
  public static LevelManager build() {
    if (instance == null) {
      instance = new LevelManager();
    }
    return instance;
  }

  /**
   * 
   * Private constructor to enforce singleton pattern.
   */
  private LevelManager() {
  }

  /**
   * 
   * Loads the next level based on the current progress stored in SaveManager.
   */
  public void loadNextLevel() {
    String newLevel = SaveManager.readProgress().get("level");
    int newLevelInt = Integer.parseInt(newLevel);
    loadLevel(newLevelInt);
  }

  /**
   * 
   * Reloads the current level.
   */
  public void reloadLevel() {
    String newLevel = SaveManager.readProgress().get("level");
    int newLevelInt = Integer.parseInt(newLevel);
    loadLevel(newLevelInt);
  }

  /**
   * 
   * Loads the specified level.
   * 
   * @param level The level number to load.
   */
  public void loadLevel(int level) {
    if (level == 7) {
      Loop.build().stop();
      Loop.build().setState(GameState.GameFinished);
      SaveManager.resetLevel();
      level = 1;
      return;
    }

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