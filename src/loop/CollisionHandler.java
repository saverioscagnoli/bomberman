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
	private int tile1,tile2;


	public CollisionHandler(GameLoop gameLoop, GameCharacter character) {
		this.gameLoop = gameLoop;
		this.character = character;

	}

	public void CheckCollisions(List<Entity> entities){
		pX = gameLoop.character.posX + gameLoop.character.width * 0.5;
		pY = gameLoop.character.posY + gameLoop.character.height * 0.5;  // TODO - levare sta cosa e metterlo su utils
		this.entities = entities;
		// First, we need to check for the character's direction.
		if(gameLoop.keyHandler.buttonPriorities.isEmpty()){} else {
			this.direction = gameLoop.keyHandler.buttonPriorities.get(0); // gets direction from keyHandler
		// Then, based on the direction, we need to check for the two grid squares that the character occupies in the given direction.

		if(this.direction=="W" || this.direction=="S"){ // if the direction was vertical, we need to check the left and right grid squares.
				// if the direction is up, we need to check the tile above the character, else the one below.
					tile1 = (int) (pX - (pX % Consts.tileDims));  // TODO - levare sta cosa e metterlo su utils .  // Considers the centermost tile as the first one
					if (pX % Consts.tileDims<=24) // chooses the left or right tile based on the character's sub-grid position in the centermost tile
						{tile2 = tile1 - Consts.tileDims;} 
						else{tile2 = tile1 + Consts.tileDims;}
					System.out.println(tile1 + " " + tile2);
			}

		else if(this.direction=="A" || this.direction=="D"){
			// if the direction is left, we need to check the tile to the left of the character, else the one to the right.
				tile1 = (int) (pY - (pY % Consts.tileDims));  // TODO - levare sta cosa e metterlo su utils .  // Considers the centermost tile as the first one
				if (pY % Consts.tileDims<=24) // chooses the left or right tile based on the character's sub-grid position in the centermost tile
					{tile2 = tile1 - Consts.tileDims;} 
					else{tile2 = tile1 + Consts.tileDims;}
				System.out.println(tile1 + " " + tile2);
		}

		}}}