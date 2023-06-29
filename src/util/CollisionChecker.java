package util;


import core.Loop;
import entities.Bomberman;
import managers.TileManager;
import entities.Entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CollisionChecker {

	private static CollisionChecker instance = null;
	public static Entity[] adjacentEntities = new Entity[9];

	private boolean vertical_align;
	private boolean horizontal_align;
	private int hor_maxDistance;
	private int vert_maxDistance;
	private final Bomberman loop_Bomberman = Loop.build().bomberman;
	private int savedPlayerX;
	private int savedPlayerY;

	private CollisionChecker() {
		hor_maxDistance = Consts.tileDims - Loop.build().bomberman.width;
		vert_maxDistance = Consts.tileDims - Loop.build().bomberman.height;
		this.horizontal_align = true;
		this.vertical_align = true;
	}

	public static CollisionChecker build() {
		if (instance == null) {
			instance = new CollisionChecker();
		}
		return instance;
	}

	public void Collision_To_check() {


		
		if (horizontal_align) { // se il player nell'ultimo check era allineato orizzontalmente
			// controllare se lo é ancora
			boolean newHorizontal_align = loop_Bomberman.posX % 48 <= hor_maxDistance;
			if (!newHorizontal_align) { // se si é disallineato
				// salvare la posizione in cui era prima
				int[] savedPos = Utils.normalizeEntityPos(loop_Bomberman);

				// in base alla direzione, salvare anche l'array
				int[][] tilesToCheck;
				if (Objects.equals(loop_Bomberman.direction, "right")){ // if going left
					tilesToCheck = new int[][]{savedPos, {savedPos[1], (savedPos[0] + 48)}};
				}

				else { // if going right
					tilesToCheck = new int[][]{savedPos, {savedPos[1], (savedPos[0] - 48)}};
				}

				System.out.println(Arrays.deepToString(tilesToCheck));
				horizontal_align = false;
			}
		}
		if (vertical_align) {
			boolean newVertical_align = loop_Bomberman.posY % 48 <= vert_maxDistance;
			if (!newVertical_align) {

				int[] savedPos = Utils.normalizeEntityPos(loop_Bomberman);

				int[][] tilesToCheck;
				if (Objects.equals(loop_Bomberman.direction, "up")){ // if going up
					tilesToCheck = new int[][]{savedPos, {(savedPos[1] - 48), savedPos[0]}};
				}

				else {
					tilesToCheck = new int[][]{savedPos, {(savedPos[1] + 48), savedPos[0]}};
				}

				System.out.println(Arrays.deepToString(tilesToCheck));
				vertical_align = false;
			}
		}
			horizontal_align = loop_Bomberman.posX % 48 <= hor_maxDistance;
			vertical_align = loop_Bomberman.posY % 48 <= vert_maxDistance;

//		 se tutti e due sono negativi o se tutti e due positivi non devi checkare nulla

			if ((horizontal_align && vertical_align) || (!horizontal_align && !vertical_align)) {
//				System.out.println("non a cavallo fra due tile");
			}

			if (horizontal_align ^ vertical_align) {
				if (horizontal_align) {
					// se il player é disallineato orizzontalmente, bisogna checkare le 2 tile sopra e sotto

				}
			}
		}
	}