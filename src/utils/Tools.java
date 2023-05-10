package utils;

import entities.GameCharacter;

public class Tools {
    private static double pX, pY;

    public static int[] getGridPos(GameCharacter character) {
        pX = character.posX + character.width * 0.5;
        pY = character.posY + character.height * 0.5;
        int gridX = ((int) (pX - (pX % Consts.tileDims)));
        int gridY = ((int) (pY - (pY % Consts.tileDims)));
        int[] gridArray = { gridX, gridY };
        return gridArray;
    }
}
