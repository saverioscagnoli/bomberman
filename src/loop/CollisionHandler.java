package loop;

import entities.GameCharacter;
import utils.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Tool;

import utils.*;
import entities.*;

public class CollisionHandler {
	private GameLoop gameLoop;
	private GameCharacter character;
	private List<Entity> entities;
	public List<int[]> tileToCheck = new ArrayList<int[]>();
	private double pX, pY;
	private String direction;
	private int tile1,tile2;


	public CollisionHandler(GameLoop gameLoop, GameCharacter character) {
		this.gameLoop = gameLoop;
		this.character = character;

	}

	public void CheckCollisions(List<Entity> entities){
		pX = gameLoop.character.posX + gameLoop.character.width * 0.5;
		pY = gameLoop.character.posY + gameLoop.character.height * 0.5;  // TODO - levare sta cosa e metterlo su utils, forse
		this.entities = entities;
		// First, we need to check for the character's direction.
		if(gameLoop.keyHandler.buttonPriorities.isEmpty()){} else {
			this.direction = gameLoop.keyHandler.buttonPriorities.get(0); // gets direction from keyHandler
		// Then, based on the direction, we need to check for the two grid squares that the character occupies in the given direction.

		if(this.direction=="W" || this.direction=="S"){ // if the direction was vertical, we need to check the left and right grid squares.
				// if the direction is up, we need to check the tile above the character, else the one below.
					int[] currentPos = Tools.getGridPos(character);  // get nearest grid position for the first tile
					int tile1 = currentPos[0];
					if (pX % Consts.tileDims<=24) // chooses the left or right tile based on the character's sub-grid position in the centermost tile
						{tile2 = tile1 - Consts.tileDims;} 
						else{tile2 = tile1 + Consts.tileDims;}
					int tileY = currentPos[1]/Consts.tileDims;
					if (this.direction == "W"){tileY-=1;}else{tileY+=1;}
					int[] tile1arr = {tile1/Consts.tileDims,tileY};
					int[] tile2arr = {tile2/Consts.tileDims,tileY};
					if(character.posX%48<=19){System.out.println("in block");} // TODO usarlo per poter passare in mezzo ai blocchi.
					else { // codice da eseguire se sta a cavallo fra due blocchi della grid
						switch(this.direction){
							case "W":
							
						}
					}
					System.out.println(Arrays.toString(tile1arr)+ " " + Arrays.toString(tile2arr)); // debug
			}
		}
	}
}