package managers;

import java.util.ArrayList;
import java.util.HashMap;
import entities.Enemy;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;
import java.awt.Graphics2D;

public class EnemyManager {
    public static EnemyManager instance = null;
    public ArrayList<Enemy> enemies;

    private HashMap<Integer, HashMap<String, SpriteAnimation>> animations;
    private static String[] srcs = { "/assets/enemy-1.png" };

    private EnemyManager() {
        this.enemies = new ArrayList<>();
        this.animations = new HashMap<>();
        this.createAnimations();
    }

    public static synchronized EnemyManager getInstance() {
        if (instance == null) {
            instance = new EnemyManager();
        }
        return instance;
    }

    private void createAnimations() {
        Enemy enemy1 = new Enemy(0, 0, Consts.tileDims, Consts.tileDims, 1, srcs[0]);
        enemy1.setScale(2);
        HashMap<String, SpriteAnimation> animMap = new HashMap<>();
        animMap.put("down", new SpriteAnimation(enemy1.spritesheet, 4, 4, enemy1.scale, 0, 4, 5));
        animMap.put("up", new SpriteAnimation(enemy1.spritesheet, 4, 4, enemy1.scale, 1, 4, 5));
        animMap.put("left", new SpriteAnimation(enemy1.spritesheet, 4, 4, enemy1.scale, 2, 4, 5));
        animMap.put("right", new SpriteAnimation(enemy1.spritesheet, 4, 4, enemy1.scale, 3, 4, 5));
        this.animations.put(0, animMap);
    }

    public void instanciateEnemies(int n) {
        for (int i = 0; i < n; i++) {
            int num = Utils.rng(0, srcs.length);
            String src = srcs[num];
            HashMap<String, SpriteAnimation> animMap = this.animations.get(num);
            int x = Utils.rng(Consts.tileDims + 1, Consts.screenWidth - Consts.tileDims);
            int y = Utils.rng(Consts.tileDims + 1, Consts.screenHeight - Consts.tileDims);
            int[] pos = Utils.normalizePos(x, y);
            String[][] grid = TileManager.getInstance().grid;
            while (grid[y / Consts.tileDims][x / Consts.tileDims] == "WD"
                    || grid[y / Consts.tileDims][x / Consts.tileDims] == "W") {
                x = Utils.rng(Consts.tileDims + 1, Consts.screenWidth - Consts.tileDims);
                y = Utils.rng(Consts.tileDims + 1, Consts.screenHeight - Consts.tileDims);
            }
            Enemy e = new Enemy(pos[0], pos[1], Consts.tileDims, Consts.tileDims, 1, src);
            animMap.forEach((k, v) -> {
                e.addAnimation(k, v);
            });
            enemies.add(e);
        }
    }

    public void updateEnemies() {
        ArrayList<Enemy> toRemove = new ArrayList<>();
        int l = enemies.size();
        for (int i = 0; i < l; i++) {
            Enemy e = enemies.get(i);
            if (e.dead) {
                toRemove.add(e);
            } else {
                e.update();
            }
        }

        toRemove.forEach((e) -> this.enemies.remove(e));
    }

    public void drawEnemies(Graphics2D g2d) {
        int l = enemies.size();
        for (int i = 0; i < l; i++) {
            Enemy e = enemies.get(i);
            e.render(g2d);
        }
    }
}
