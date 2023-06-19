package util;

import entities.Entity;
import java.util.ArrayList;
import entities.Bomberman;
import entities.PowerUp;
import managers.PowerupManager;
import managers.TileManager;

public class CollisionChecker {
	// array for the 3x3 grid around the character
	public static Entity[] adjacentEntities = new Entity[9];

	public static void updateAdjacentEntities(Bomberman character) {
		// reset the array
		for (int i = 0; i < adjacentEntities.length; i++) {
			adjacentEntities[i] = null;
		}

		// get the normalized position of the character
		int[] normalizedPos = Utils.normalizeEntityPos(character);

		// create an array with the coordinates of the 8 surrounding grid squares
		int[][] surroundingGridSquares = {
				{ normalizedPos[0] - Consts.tileDims, normalizedPos[1] - Consts.tileDims },
				{ normalizedPos[0], normalizedPos[1] - Consts.tileDims },
				{ normalizedPos[0] + Consts.tileDims, normalizedPos[1] - Consts.tileDims },
				{ normalizedPos[0] - Consts.tileDims, normalizedPos[1] },
				{ normalizedPos[0] + Consts.tileDims, normalizedPos[1] },
				{ normalizedPos[0] - Consts.tileDims, normalizedPos[1] + Consts.tileDims },
				{ normalizedPos[0], normalizedPos[1] + Consts.tileDims },
				{ normalizedPos[0] + Consts.tileDims, normalizedPos[1] + Consts.tileDims },
				{ normalizedPos[0], normalizedPos[1] } };

		// loop through the entities and check if they are in any of the surrounding
		// grid squares

		ArrayList<Entity> obstaclesAndPowerups = new ArrayList<>();
		obstaclesAndPowerups.addAll(TileManager.getInstance().obtsacles);
		obstaclesAndPowerups.addAll(PowerupManager.getInstance().powerups);

		for (Entity tile : obstaclesAndPowerups) {
			for (int i = 0; i < surroundingGridSquares.length; i++) {
				if (tile.posX == surroundingGridSquares[i][0] && tile.posY == surroundingGridSquares[i][1]) {
					adjacentEntities[i] = tile;
				}
			}
		}

		// print adjacent tiles (DEBUG)
		/*
		 * for (int i = 0; i < adjacentEntities.length; i++) {
		 * if (adjacentEntities[i] != null) {
		 * System.out.println("adjacentEntities[" + i + "] = " +
		 * adjacentEntities[i].getClass().getSimpleName());
		 * }
		 * }
		 */
	}

	// function to check collision between an entity and a square's coordinates
	public static boolean checkCollision(Entity entity, Bomberman character, String direction) {
		// based on the direction, a future collision is checked. in case it will
		// happen, the function returns true

		// if the player's normalized position is on top of a powerup square, the
		// powerup is collected, activate the onpickup function
		if (entity instanceof PowerUp) {
			if (Utils.normalizeEntityPos(character)[0] == entity.posX
					&& Utils.normalizeEntityPos(character)[1] == entity.posY) {
				PowerUp powerup = (PowerUp) entity;
				PowerupManager.HandlePowerup(powerup, character);
			}
		}

		if (!entity.isSolid)
			return false;

		// TODO : risolvere coding horror
		switch (direction) {
			case "up":
				if (entity.posY + entity.height + character.speed >= character.posY
						&& entity.posY + entity.height <= character.posY) {
					if (entity.posX + entity.width >= character.posX
							&& entity.posX <= character.posX + character.width) {
						return true;
					}
				}
				break;
			case "down":
				if (entity.posY - character.speed <= character.posY + character.height
						&& entity.posY >= character.posY + character.height) {
					if (entity.posX + entity.width >= character.posX
							&& entity.posX <= character.posX + character.width) {
						return true;
					}
				}
				break;
			case "left":
				if (entity.posX + entity.width + character.speed >= character.posX
						&& entity.posX + entity.width <= character.posX) {
					if (entity.posY + entity.height >= character.posY
							&& entity.posY <= character.posY + character.height) {
						return true;
					}
				}
				break;
			case "right":
				if (entity.posX - character.speed <= character.posX + character.width
						&& entity.posX >= character.posX + character.width) {
					if (entity.posY + entity.height >= character.posY
							&& entity.posY <= character.posY + character.height) {
						return true;
					}
				}
				break;
		}
		return false;
	}
}
