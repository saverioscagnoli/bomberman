package util;

import core.Loop;
import entities.Bomberman;
import entities.PowerUp;
import managers.TileManager;
import managers.PowerupManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * The CollisionChecker class handles collision detection for the game's
 * entities.
 * It checks for collisions between the player character (Bomberman) and the
 * game environment.
 */
public class CollisionChecker {

	private static CollisionChecker instance = null;
	private boolean old_verticalAlign;
	private boolean old_horizontalAlign;
	private int hor_maxDistance;
	private int vert_maxDistance;
	private final Bomberman loop_Bomberman = Loop.build().bomberman;
	boolean newHorizontal_align;
	boolean newVertical_align;
	public static ArrayList<TileType> SolidTiles = new ArrayList<>(
			Arrays.asList(TileType.Bomb, TileType.Wall, TileType.Obstacle));

	private boolean upSolid;
	private boolean downSolid;
	private boolean rightSolid;
	private boolean leftSolid;

	int[] savedPos;

	private CollisionChecker() {
		// max distances before player misalignment
		hor_maxDistance = Consts.tileDims - Loop.build().bomberman.width;
		vert_maxDistance = Consts.tileDims - Loop.build().bomberman.height;
		this.old_horizontalAlign = true;
		this.old_verticalAlign = true;
	}

	/**
	 * Retrieves the singleton instance of CollisionChecker or creates a new
	 * instance if it doesn't exist.
	 *
	 * @return The CollisionChecker instance.
	 */
	public static CollisionChecker build() {
		if (instance == null) {
			instance = new CollisionChecker();
		}
		return instance;
	}

	/**
	 * Updates the collision detection for the centered position of the player
	 * character.
	 */
	public void update_Centered_Collisions() {
		TileType[][] gameGrid = TileManager.build().grid;
		savedPos = Utils.normalizeEntityPos(loop_Bomberman);
		upSolid = SolidTiles.contains(gameGrid[(savedPos[1]) / 48 - 1][(savedPos[0]) / 48]);
		downSolid = SolidTiles.contains(gameGrid[(savedPos[1]) / 48 + 1][(savedPos[0]) / 48]);
		rightSolid = SolidTiles.contains(gameGrid[(savedPos[1]) / 48][(savedPos[0]) / 48 + 1]);
		leftSolid = SolidTiles.contains(gameGrid[(savedPos[1]) / 48][(savedPos[0]) / 48 - 1]);
	}

	/**
	 * Performs collision checking for the player character.
	 */
	public void Collision_To_check() {

		/* vengono create le coordinate aggiornate */
		newHorizontal_align = loop_Bomberman.posX % 48 <= hor_maxDistance;
		newVertical_align = loop_Bomberman.posY % 48 <= vert_maxDistance;

		if (old_horizontalAlign && !newHorizontal_align) { // se il player si é disallineato orizzontalmente
			// salvare la posizione in cui era prima
			savedPos = Utils.normalizeEntityPos(loop_Bomberman);

			// in base alla direzione, salvare anche l'altro quadrato su cui é appena andato
			int[][] occupiedTiles;

			/* decide which positions to save */
			if (Objects.equals(loop_Bomberman.direction, "right")) { // if going left
				occupiedTiles = new int[][] { savedPos, { savedPos[0] + 48, savedPos[1] } };
			} else { // if going right
				occupiedTiles = new int[][] { savedPos, { savedPos[0] - 48, savedPos[1] } };
			}
			// System.out.println(Arrays.deepToString(occupiedTiles));

			upSolid = SolidTiles
					.contains(TileManager.build().grid[occupiedTiles[0][1] / 48 - 1][occupiedTiles[0][0] / 48])
					||
					SolidTiles
							.contains(TileManager.build().grid[occupiedTiles[1][1] / 48 - 1][occupiedTiles[1][0] / 48]);

			downSolid = SolidTiles
					.contains(TileManager.build().grid[occupiedTiles[0][1] / 48 + 1][occupiedTiles[0][0] / 48])
					||
					SolidTiles
							.contains(TileManager.build().grid[occupiedTiles[1][1] / 48 + 1][occupiedTiles[1][0] / 48]);

			for (PowerUp p : PowerupManager.build().powerups) {
				// if one of the occupied tiles has a powerup, pick it up
				if (p.posX == occupiedTiles[1][0] && p.posY == occupiedTiles[1][1]) {
					p.onPickup();
					Utils.playSound("assets/sounds/powerup.wav");
				}
			}

		}

		if (old_verticalAlign && !newVertical_align) { // se il player si é disallineato verticalmente

			savedPos = Utils.normalizeEntityPos(loop_Bomberman);

			int[][] occupiedTiles;
			if (Objects.equals(loop_Bomberman.direction, "up")) { // if going up
				occupiedTiles = new int[][] { savedPos, { savedPos[0], savedPos[1] - 48 } };
			} else {
				occupiedTiles = new int[][] { savedPos, { savedPos[0], savedPos[1] + 48 } };
			}

			rightSolid = SolidTiles
					.contains(TileManager.build().grid[occupiedTiles[0][1] / 48][occupiedTiles[0][0] / 48 + 1])
					||
					SolidTiles
							.contains(TileManager.build().grid[occupiedTiles[1][1] / 48][occupiedTiles[1][0] / 48 + 1]);

			leftSolid = SolidTiles
					.contains(TileManager.build().grid[occupiedTiles[0][1] / 48][occupiedTiles[0][0] / 48 - 1])
					||
					SolidTiles
							.contains(TileManager.build().grid[occupiedTiles[1][1] / 48][occupiedTiles[1][0] / 48 - 1]);
			// System.out.println("rightsolid : " + rightSolid + " leftsolid : "+
			// leftSolid);

			for (PowerUp p : PowerupManager.build().powerups) {
				// if one of the occupied tiles has a powerup, pick it up
				if (p.posX == occupiedTiles[1][0] && p.posY == occupiedTiles[1][1]) {
					p.onPickup();
					Utils.playSound("assets/sounds/powerup.wav");
				}
			}
		}

		if ((!old_verticalAlign && newVertical_align) || (!old_horizontalAlign && newHorizontal_align)) {
			// se il player é tornato al centro di una grid
			update_Centered_Collisions();
		}

		// save the directions for the next cycle
		old_horizontalAlign = newHorizontal_align;
		old_verticalAlign = newVertical_align;

		if ((newHorizontal_align && newVertical_align)) {
			/* collision check da fare in caso il player sia dentro un quadrato */
			// TileType[][] gameGrid = TileManager.build().grid;
			// System.out.printlnSolidTiles.contains(gameGrid[1][2]));

			// TODO : mettere uno switch per le direzioni
			// TODO : aumentare di x pixel la tolleranza fra il blocco e il player.
			// questo crea l'effetto di "spinta" fuori dai blocchi.
			if (rightSolid) {
				if (loop_Bomberman.posX + loop_Bomberman.width + loop_Bomberman.speed >= savedPos[0] + 48) {
					loop_Bomberman.posX -= loop_Bomberman.speed;
				}
			}
			if (leftSolid) {
				if (loop_Bomberman.posX - loop_Bomberman.speed <= savedPos[0]) {
					loop_Bomberman.posX += loop_Bomberman.speed;
				}
			}
			if (upSolid) {
				if (loop_Bomberman.posY - loop_Bomberman.speed <= savedPos[1]) {
					loop_Bomberman.posY += loop_Bomberman.speed;
				}
			}
			if (downSolid) {
				if (loop_Bomberman.posY + loop_Bomberman.height + loop_Bomberman.speed >= savedPos[1] + 48) {
					loop_Bomberman.posY -= loop_Bomberman.speed;
				}
			}
		}

		if (newHorizontal_align ^ newVertical_align) {
			/* collision check da fare in caso il player sia a cavallo fra due quadrati */
			if (newHorizontal_align) {
				if (rightSolid) {
					if (loop_Bomberman.posX + loop_Bomberman.width + loop_Bomberman.speed >= savedPos[0] + 48) {
						loop_Bomberman.posX -= loop_Bomberman.speed;
					}
				}
				if (leftSolid) {
					if (loop_Bomberman.posX - loop_Bomberman.speed <= savedPos[0]) {
						loop_Bomberman.posX += loop_Bomberman.speed;
					}
				}
			}
			if (newVertical_align) {
				if (upSolid) {
					if (loop_Bomberman.posY - loop_Bomberman.speed <= savedPos[1]) {
						loop_Bomberman.posY += loop_Bomberman.speed;
					}
				}
				if (downSolid) {
					if (loop_Bomberman.posY + loop_Bomberman.height + loop_Bomberman.speed >= savedPos[1] + 48) {
						loop_Bomberman.posY -= loop_Bomberman.speed;
					}
				}
			}
		}

		// se il player é a cavallo di 4 quadrati non serve fare alcun collision check.

	}
}
