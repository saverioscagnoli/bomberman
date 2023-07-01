package util;


import core.Loop;
import entities.Bomberman;
import entities.Entity;
import managers.TileManager;

import java.util.Arrays;
import java.util.Objects;

public class CollisionChecker {

	private static CollisionChecker instance = null;
	private boolean old_verticalAlign;
	private boolean old_horizontalAlign;
	private int hor_maxDistance;
	private int vert_maxDistance;
	private final Bomberman loop_Bomberman = Loop.build().bomberman;
	private int savedPlayerX;
	private int savedPlayerY;
	boolean newHorizontal_align;
	boolean newVertical_align;
	private TileType[] SolidTiles = {TileType.Wall, TileType.Bomb, TileType.Obstacle};

	private boolean upSolid;
	private boolean downSolid;
	private boolean rightSolid;
	private boolean leftSolid;

	int[] savedPos;

	private CollisionChecker() {
		hor_maxDistance = Consts.tileDims - Loop.build().bomberman.width;
		vert_maxDistance = Consts.tileDims - Loop.build().bomberman.height;
		this.old_horizontalAlign = true;
		this.old_verticalAlign = true;
	}

	public static CollisionChecker build() {
		if (instance == null) {
			instance = new CollisionChecker();
		}
		return instance;
	}

	public void Collision_To_check() {

		/* vengono create le coordinate aggiornate*/
		newHorizontal_align = loop_Bomberman.posX % 48 <= hor_maxDistance;
		newVertical_align = loop_Bomberman.posY % 48 <= vert_maxDistance;

		if (old_horizontalAlign && !newHorizontal_align) { // se il player si é disallineato orizzontalmente
				// salvare la posizione in cui era prima
				savedPos = Utils.normalizeEntityPos(loop_Bomberman);

				// in base alla direzione, salvare anche l'altro quadrato su cui é appena andato
				int[][] occupiedTiles;

				/*decide which positions to save*/
				if (Objects.equals(loop_Bomberman.direction, "right")){ // if going left
					occupiedTiles = new int[][]{savedPos, {savedPos[0]+48, savedPos[1]}};
				}

				else { // if going right
					occupiedTiles = new int[][]{savedPos, {savedPos[0]-48, savedPos[1]}};
				}

				System.out.println(Arrays.deepToString(occupiedTiles));

				upSolid = (
						Arrays.asList(SolidTiles).contains(TileManager.build().grid[occupiedTiles[0][1]/48-1][occupiedTiles[0][0]/48])
						||
						Arrays.asList(SolidTiles).contains(TileManager.build().grid[occupiedTiles[1][1]/48-1][occupiedTiles[1][0]/48])
						);

				downSolid = (
						Arrays.asList(SolidTiles).contains(TileManager.build().grid[occupiedTiles[0][1]/48+1][occupiedTiles[0][0]/48])
						||
						Arrays.asList(SolidTiles).contains(TileManager.build().grid[occupiedTiles[1][1]/48+1][occupiedTiles[1][0]/48])
						);

			System.out.println("upsolid : " + upSolid + " downsolid : "+ downSolid);
		}

		if (old_verticalAlign && !newVertical_align) { // se il player si é disallineato verticalmente

				savedPos = Utils.normalizeEntityPos(loop_Bomberman);

				int[][] occupiedTiles;
				if (Objects.equals(loop_Bomberman.direction, "up")){ // if going up
					occupiedTiles = new int[][]{savedPos, {savedPos[0], savedPos[1]-48}};
				}

				else {
					occupiedTiles = new int[][]{savedPos, {savedPos[0], savedPos[1]+48}};
				}

				rightSolid = (
						Arrays.asList(SolidTiles).contains(TileManager.build().grid[occupiedTiles[0][1]/48][occupiedTiles[0][0]/48+1])
						||
						Arrays.asList(SolidTiles).contains(TileManager.build().grid[occupiedTiles[1][1]/48][occupiedTiles[1][0]/48+1])
						);

				leftSolid = (
						Arrays.asList(SolidTiles).contains(TileManager.build().grid[occupiedTiles[0][1]/48][occupiedTiles[0][0]/48-1])
						||
						Arrays.asList(SolidTiles).contains(TileManager.build().grid[occupiedTiles[1][1]/48][occupiedTiles[1][0]/48-1])
						);
			System.out.println("rightsolid : " + rightSolid + " leftsolid : "+ leftSolid);
		}

		if ((!old_verticalAlign && newVertical_align) || (!old_horizontalAlign && newHorizontal_align)){
			// se il player é tornato al centro di una grid
			// get player position
			TileType[][] gameGrid = TileManager.build().grid;
			savedPos = Utils.normalizeEntityPos(loop_Bomberman);
			upSolid = Arrays.asList(SolidTiles).contains(gameGrid[(savedPos[1])/48-1][(savedPos[0])/48]);
			downSolid = Arrays.asList(SolidTiles).contains(gameGrid[(savedPos[1])/48+1][(savedPos[0])/48]);
			rightSolid = Arrays.asList(SolidTiles).contains(gameGrid[(savedPos[1])/48][(savedPos[0])/48+1]);
			leftSolid = Arrays.asList(SolidTiles).contains(gameGrid[(savedPos[1])/48][(savedPos[0])/48-1]);


			System.out.println("up : " + upSolid + " right : "+ rightSolid + " down : "+downSolid+" left : "+leftSolid);
		}

		// save the directions for the next cycle
		old_horizontalAlign = newHorizontal_align;
		old_verticalAlign = newVertical_align;


			if ((newHorizontal_align && newVertical_align)) {
				/* collision check da fare in caso il player sia dentro un quadrato */
//				TileType[][] gameGrid = TileManager.build().grid;
//				System.out.println(Arrays.asList(SolidTiles).contains(gameGrid[1][2]));

				//TODO : mettere uno switch per le direzioni
				//TODO : aumentare di x pixel la tolleranza fra il blocco e il player.
				//questo crea l'effetto di "spinta" fuori dai blocchi.
				if (rightSolid){
					if (loop_Bomberman.posX+loop_Bomberman.width+loop_Bomberman.speed >= savedPos[0]+48){
						loop_Bomberman.posX -= loop_Bomberman.speed;
					}
				}
				if (leftSolid){
					if (loop_Bomberman.posX-loop_Bomberman.speed <= savedPos[0]){
						loop_Bomberman.posX += loop_Bomberman.speed;
					}
				}
				if (upSolid){
					if (loop_Bomberman.posY-loop_Bomberman.speed <= savedPos[1]){
						loop_Bomberman.posY += loop_Bomberman.speed;
					}
				}
				if (downSolid){
					if (loop_Bomberman.posY+loop_Bomberman.height+loop_Bomberman.speed >= savedPos[1]+48){
						loop_Bomberman.posY -= loop_Bomberman.speed;
					}
				}
			}

			if (newHorizontal_align ^ newVertical_align) {
				/* collision check da fare in caso il player sia a cavallo fra due quadrati */
				if (newHorizontal_align){
					if (rightSolid){
						if (loop_Bomberman.posX+loop_Bomberman.width+loop_Bomberman.speed >= savedPos[0]+48){
							loop_Bomberman.posX -= loop_Bomberman.speed;
						}
					}
					if (leftSolid){
						if (loop_Bomberman.posX-loop_Bomberman.speed <= savedPos[0]){
							loop_Bomberman.posX += loop_Bomberman.speed;
						}
					}
				}
				if (newVertical_align){
					if (upSolid){
						if (loop_Bomberman.posY-loop_Bomberman.speed <= savedPos[1]){
							loop_Bomberman.posY += loop_Bomberman.speed;
						}
					}
					if (downSolid){
						if (loop_Bomberman.posY+loop_Bomberman.height+loop_Bomberman.speed >= savedPos[1]+48){
							loop_Bomberman.posY -= loop_Bomberman.speed;
						}
					}
				}
			}

			// se il player é a cavallo di 4 quadrati  non serve fare alcun collision check.

		}
	}