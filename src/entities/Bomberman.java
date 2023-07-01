package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import core.Loop;
import ui.Sprite;
import ui.SpriteAnimation;
import managers.BombManager;
import managers.TileManager;
import util.*;

public class Bomberman extends Entity {
	/* The bomb radius, so how much tile it takes to end the explosion */
	public int bombRadius;

	/* How many bombs the player can place at the same time */
	public int maxBombs;

	/* The hp of the player */
	public int health;

	/* A flag to determine if the player is immune, and will not take damage */
	public boolean immune;

	/* The lives of the player */
	public int lives;

	/* A flag to stop the animation if the player is not moving */
	public boolean stop;

	/* The keyboard keys pressed, determines movement, see Controller.java */
	public ArrayList<String> keys;

	/* The indexes on the grid */
	private int gridX = 1;
	private int gridY = 1;

	/* The tile on which the player was */
	private TileType prevTile = TileType.Wall;

	/* The score of the player */
	public int score;
	public String direction;

	public Bomberman(int posX, int posY) {
		/* Pass everything to the superclass Entity */
		super(posX, posY, 30, 30, 5, new Sprite("bomberman", 6.3, 5, "down",
				new SpriteAnimation[] {
						new SpriteAnimation("left", 3, 0, 10),
						new SpriteAnimation("down", 3, 1, 10),
						new SpriteAnimation("right", 3, 2, 10),
						new SpriteAnimation("up", 3, 3, 10),
						new SpriteAnimation("death", 6, 4, 10)
				},
				2.5f));

		/* Set the props to their initial states */
		this.keys = new ArrayList<>();
		this.health = 3;
		this.maxBombs = 3;
		this.bombRadius = 2;
		this.lives = 5;
		this.score = 0;
	}

	public void die() {
		this.keys.clear();
		this.lives--;
		this.sprite.setAnimation("death");
		this.dead = true;
		Loop.build().removeController();
		this.sprite.width = (int) (this.sprite.spritesheet.getWidth() / 6);
	}

	/*
	 * Place the bomb at a normalised given position, and set the grid tile to bomb
	 */
	public void placeBomb() {
		BombManager bombManager = BombManager.build();
		int[] pos = Utils.normalizePos(this.posX, this.posY);
		if (bombManager.bombs.size() >= this.maxBombs) {
			return;
		}
		// if the bomb is already there, don't add it
		int i = pos[1] / Consts.tileDims;
		int j = pos[0] / Consts.tileDims;
		if (TileManager.build().grid[i][j] == TileType.Bomb)
			return;

		bombManager.addBomb(new Bomb(pos[0], pos[1], this.bombRadius));
	}

	/* Damage the player */
	public void dealDamage(int damage) {
		if (!immune) {
			health -= damage;
			immune = true;
			Utils.setTimeout(() -> immune = false, 1000);
		}
	}

	public void update(int elapsed) {

		CollisionChecker.build().Collision_To_check();

		if (this.dead) {
			if (this.sprite.current < this.sprite.currentAnimation.maxFrames - 1) {
				this.sprite.update(elapsed);
			} else {
				this.sprite.current = this.sprite.currentAnimation.maxFrames - 1;
				if (this.lives > 0) {
					this.posX = 50;
					this.posY = 50;
					this.dead = false;
					this.sprite.setAnimation("down");
					Loop.build().addController();
					this.sprite.width = (int) (this.sprite.spritesheet.getWidth() / 6.3);
				}

			}
			return;
		}

		// TODO: sarebbe un po' piu organizzato creare un metodo fatto apposta nel
		// collision checker per il movimento del player.
		// probabilmente si potrebbe anche comprimere un po'.
		this.sprite.update(elapsed);
		this.speed = 5;

		if (!this.keys.isEmpty()) {

			TileManager tileManager = TileManager.build();



			/* Update the player position on the grid */
			int prevX = this.gridX;
			int prevY = this.gridY;
			int[] normPos = Utils.normalizePos((int) (this.posX + this.width * 0.5), (int) (this.posY + this.height * 0.5));
			this.gridX = normPos[0] / Consts.tileDims;
			this.gridY = normPos[1] / Consts.tileDims;

			/* Reset the tile on which the player was to what was originally */
			if (prevX != this.gridX || prevY != this.gridY) {
				if (tileManager.grid[prevY][prevX] == TileType.Bomberman) {
					tileManager.grid[prevY][prevX] = this.prevTile;
				}
				this.prevTile = tileManager.grid[this.gridY][this.gridX];
				tileManager.grid[this.gridY][this.gridX] = TileType.Bomberman;
			}
			direction = "";
			this.stop = false;

			switch (this.keys.get(0)) {
				case "A":

					posX -= speed;
					direction = "left";
					break;

				case "D":

					posX += speed;
					direction = "right";
					break;

				case "W":

					posY -= speed;
					direction = "up";
					break;

				case "S":

					posY += speed;
					direction = "down";
					break;
			}
		} else {
			/* Stop the animation */
			this.stop = true;
			this.sprite.current = 1;
		}
	}

	public void render(Graphics2D g2d) {
		this.sprite.draw(g2d, (int) this.posX, (int) this.posY - 30);

		// WritableRaster raster = this.sprite.spritesheet.getRaster();

		/*
		 * for (int i = 0; i < this.sprite.spritesheet.getWidth(); i++) {
		 * for (int j = 0; j < this.sprite.spritesheet.getHeight(); j++) {
		 * int[] pixels = raster.getPixel(i, j, (int[]) null);
		 * if (pixels[0] == 0 && pixels[1] == 0 && pixels[2] == 0)
		 * continue;
		 * pixels[0] = 255;
		 * pixels[1] = 255;
		 * pixels[2] = 255;
		 * raster.setPixel(i, j, pixels);
		 * }
		 * }
		 */

		// draw the health bar above the player with 5 squares for each health point

		// draw the hitbox as a gray square

		 g2d.setColor(Color.GRAY);
		 g2d.fillRect(posX, posY, width, height);

		g2d.setColor(Color.RED);
		g2d.fillRect((int) posX - 15, (int) posY - 20, 5 * 10, 5);
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) posX - 15, (int) posY - 20, health * 10, 5);
		g2d.setColor(Color.GRAY);
	}
}
