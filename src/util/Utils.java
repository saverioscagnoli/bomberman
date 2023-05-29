package util;

import java.util.List;

import entities.*;
import ui.Button;

public abstract class Utils {
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

    public static boolean EnemyCollision(Enemy enemy, List<Entity> entities, int direction){
        // if the enemy's normalized position is just about to hit a solid entity, change direction
        int[] normalizedPos = {(int)enemy.posX,(int) enemy.posY};
        switch (direction) {
            case 0:
                normalizedPos[0] -= Consts.tileDims;
                break;
            case 1:
                normalizedPos[0] += Consts.tileDims;
                break;
            case 2:
                normalizedPos[1] -= Consts.tileDims;
                break;
            case 3:
                normalizedPos[1] += Consts.tileDims;
                break;
        }
        for (Entity entity : entities) {
            if ((entity.isSolid || entity instanceof Bomb ) && entity.posX == normalizedPos[0] && entity.posY == normalizedPos[1]) {
                System.out.println("Collision");
                return true;
            }
        }
        return false;
    }

    public static boolean buttonClick(int mouseX, int mouseY, Button btn) {
        return  ((mouseX >= btn.x && mouseX <= btn.x + btn.width) && (mouseY >= btn.y && mouseY <= btn.y + btn.height));
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
}
