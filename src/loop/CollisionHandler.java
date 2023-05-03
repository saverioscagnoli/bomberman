package loop;

import entities.GameCharacter;
import utils.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.*;

public class CollisionHandler {
	private GameLoop gameLoop;
	private GameCharacter character;
	private List<Entity> entities;
	public List<int[]> tileToCheck = new ArrayList<int[]>();
	private double pX, pY;
	private String direction;


	public CollisionHandler(GameLoop gameLoop, GameCharacter character) {
		this.gameLoop = gameLoop;
		this.character = character;

	}

	public void CheckCollisions(List<Entity> entities){
		pX = gameLoop.character.posX + gameLoop.character.width * 0.5;
		pY = gameLoop.character.posY + gameLoop.character.height * 0.5;  // TODO - levare sta cosa e metterlo su utils
		this.entities = entities;
		// First, we need to check for the character's direction.
		if(!gameLoop.keyHandler.buttonPriorities.isEmpty()){
			this.direction = gameLoop.keyHandler.buttonPriorities.get(0); // gets direction from keyHandler
		// Then, based on the direction, we need to check for the two grid squares that the character occupies in the given direction.
		if(this.direction=="W" || this.direction=="S"){ // if the direction was vertical, we need to check the left and right grid squares.
			if(pX % Consts.tileDims==0){ // if the division remainder is zero, it means it is centered, so we just need to check a single tile.
				// if the direction is up, we need to check the tile above the character, else the one below.
				if(this.direction=="W"){
					int[] currentTile = {(int) (pX - (pX % Consts.tileDims)),(int) (pY - (pY % Consts.tileDims))-1};
					tileToCheck.clear();
					System.out.println(Arrays.toString(currentTile));
					tileToCheck.add(currentTile);
				}
			}
		}

		else if(this.direction=="A" || this.direction=="D"){}

		}
	}
}
