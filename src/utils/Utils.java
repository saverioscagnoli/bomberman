package utils;

import entities.GameCharacter;
import ui.Button;

public abstract class Utils {
    public static int[] normalizePos(int x, int y) {
        int gridX = ((int) (x - (x % Consts.tileDims)));
        int gridY = ((int) (y - (y % Consts.tileDims)));
        int[] gridArray = { gridX, gridY };
        return gridArray;
    }

    public static int[] normalizeCharacterPos(GameCharacter character) {
        int pX = (int) (character.posX + character.width * 0.5);
        int pY = (int) (character.posY + character.height * 0.5);
        return normalizePos(pX, pY);
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
