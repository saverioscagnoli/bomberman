package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import entities.*;
import managers.TileManager;
import ui.Button;

public abstract class Utils {

    public static int rng(int min, int max) {
        Random rnd = new Random();
        return rnd.nextInt(min, max);
    }

    public static <T> T pick(T[] arr) {
        return arr[rng(0, arr.length)];
    }

    public static int[] normalizePos(int x, int y) {
        int gridX = ((int) (x - (x % Consts.tileDims)));
        int gridY = ((int) (y - (y % Consts.tileDims)));
        int[] gridArray = { gridX, gridY };
        return gridArray;
    }

    public static int[] normalizeEntityPos(Entity entity) {
        int pX = (int) (entity.posX + entity.width * 0.5);
        int pY = (int) (entity.posY + entity.height * 0.5);
        return normalizePos(pX, pY);
    }

    public static boolean enemyCollision(Enemy enemy, String direction) {
        // if the enemy's normalized position is just about to hit a solid entity,
        // change direction
        int[] normalizedPos = { (int) enemy.posX, (int) enemy.posY };
        switch (direction) {
            case "left":
            case "up":
                normalizedPos[0] -= Consts.tileDims;
                break;
            case "right":
            case "down":
                normalizedPos[0] += Consts.tileDims;
                break;
        }

        for (Obstacle e : Utils.getSolidWalls()) {
            if (e.posX == normalizedPos[0] && e.posY == normalizedPos[1]) {
                return true;
            }
        }

        return false;
    }

    public static boolean buttonClick(int mouseX, int mouseY, Button btn) {
        return ((mouseX >= btn.x && mouseX <= btn.x + btn.width) && (mouseY >= btn.y && mouseY <= btn.y + btn.height));
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    public static Collection<Obstacle> getSolidWalls() {
        ArrayList<Obstacle> solidWalls = new ArrayList<>();
        for (Obstacle tile : TileManager.getInstance().tiles) {
            if (tile.isSolid) {
                solidWalls.add(tile);
            }
        }
        return solidWalls;
    }
}
