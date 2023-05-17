package utils;

import java.util.List;

import entities.Entity;
import entities.GameCharacter;

public class CollisionChecker {
    // array for the 3x3 grid around the character
    public static Entity[] adjacentEntities = new Entity[8];

    public static void updateAdjacentEntities(GameCharacter character, List<Entity> entities){
        // reset the array
        for (int i = 0; i < adjacentEntities.length; i++) {
            adjacentEntities[i] = null;
        }

        // get the normalized position of the character
        int[] normalizedPos = Utils.normalizeCharacterPos(character);

        // create an array with the coordinates of the 8 surrounding grid squares
        int[][] surroundingGridSquares = { { normalizedPos[0] - 48, normalizedPos[1] - 48 },
                { normalizedPos[0], normalizedPos[1] - 48 }, { normalizedPos[0] + 48, normalizedPos[1] - 48 },
                { normalizedPos[0] - 48, normalizedPos[1] }, { normalizedPos[0] + 48, normalizedPos[1] },
                { normalizedPos[0] - 48, normalizedPos[1] + 48 }, { normalizedPos[0], normalizedPos[1] + 48 },
                { normalizedPos[0] + 48, normalizedPos[1] + 48 } };

        // loop through the entities and check if they are in any of the surrounding grid squares
        for (Entity entity : entities) {
            for (int i = 0; i < surroundingGridSquares.length; i++) {
                if (entity.posX == surroundingGridSquares[i][0] && entity.posY == surroundingGridSquares[i][1]) {
                    adjacentEntities[i] = entity;
                }
            }
        }

        // // print the array
        // for (int i = 0; i < adjacentEntities.length; i++) {
        //     System.out.println(adjacentEntities[i]);
        // }
        // // print newline
        // System.out.println();
    }

    // function to check collision between an entity and a square's coordinates
    public static boolean checkCollision(Entity entity, int x, int y,int width,int height) {
        return (x < entity.posX + entity.width && x + width > entity.posX && y < entity.posY + entity.height
                && y + height > entity.posY);
    }
}
