package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import core.Loop;
import entities.bosses.ClownMask;
import entities.bosses.FaralsBoss;
import ui.Sprite;
import ui.SpriteAnimation;
import managers.BombManager;
import managers.EnemyManager;
import managers.LevelManager;
import managers.MouseManager;
import managers.PowerupManager;
import managers.SaveManager;
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
	private TileType prevTile = TileType.Empty;

	/* The score of the player */
	public int score;
	public String direction;

	public boolean won;

	private BufferedImage blinkImage;
	private BufferedImage original;

	public Bomberman(int posX, int posY) {
		/* Pass everything to the superclass Entity */
		super(posX, posY, 20, 20, 5, new Sprite("bomberman", 6.3, 5, "down",
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
		this.health = 1;
		this.maxBombs = 1;
		this.bombRadius = 1;
		this.lives = 5;
		this.score = 0;
		this.won = false;
		this.original = this.sprite.spritesheet;
		this.blinkImage = Utils.copyImage(this.sprite.spritesheet);
		this.immune = true;

		WritableRaster raster = this.blinkImage.getRaster();

		for (int i = 0; i < this.sprite.spritesheet.getWidth(); i++) {
			for (int j = 0; j < this.sprite.spritesheet.getHeight(); j++) {
				int[] pixels = raster.getPixel(i, j, (int[]) null);
				if (pixels[0] == 0 && pixels[1] == 0 && pixels[2] == 0)
					continue;
				pixels[0] = 255;
				pixels[1] = 255;
				pixels[2] = 255;
				raster.setPixel(i, j, pixels);
			}
		}
	}

	public void die() {
		this.keys.clear();
		MouseManager.build().tileClicked[0] = 48;
		MouseManager.build().tileClicked[1] = 48;
		this.lives--;
		this.sprite.setAnimation("death");
		this.dead = true;
		Loop.build().removeController();
		Loop.build().overlay.repaint();
		this.sprite.width = (int) (this.sprite.spritesheet.getWidth() / 6);
		if (this.lives == 0) {
			SaveManager.incrementLosses();
			// gameover
		} else {
			if (this.speed > 1) {
				this.speed--;
			}
			if (!CollisionChecker.SolidTiles.contains(TileType.Obstacle)) {
				CollisionChecker.SolidTiles.add(TileType.Obstacle);
			}
		}
		Loop.build().setState(GameState.ContinueScreen); // da spostare a lives ==0
	}

	/*
	 * Place the bomb at a normalised given position, and set the grid tile to bomb
	 */
	public void placeBomb() {
		BombManager bombManager = BombManager.build();
		if (bombManager.bombs.size() >= this.maxBombs) {
			return;
		}
		System.out.println(this.prevTile);
		if (this.prevTile != TileType.Empty)
			return;

		this.prevTile = TileType.Bomb;

		Utils.playSound(Consts.soundPath + "place-bomb.wav");
		TileManager.build().grid[this.gridY][this.gridX] = TileType.Bomb;

		int x = this.gridX * Consts.tileDims;
		int y = this.gridY * Consts.tileDims;
		bombManager.addBomb(new Bomb(x, y, this.bombRadius));
	}

	public void win() { // TODO : Riportare a private
		this.won = true;
		// this.sprite = new Sprite("bomberman-hatch", 9, 1, "idle", new
		// SpriteAnimation[] {
		// new SpriteAnimation("idle", 9, 0, 10)
		// }, 2.5f);
		SaveManager.incrementLevel();
		SaveManager.incrementWins();
		Loop.build().overlay.repaint();

		LevelManager.build().loadNextLevel();
		Loop.build().setState(GameState.StageCleared);

		this.won = false;
		this.keys.clear();
	}

	public void update(int elapsed) {

		if (this.won) {
			if (this.sprite.current < this.sprite.currentAnimation.maxFrames - 1) {
				this.sprite.update(elapsed);
			}
			return;
		}

		CollisionChecker.build().Collision_To_check();

		if (this.prevTile == TileType.Hatch) {
			if (EnemyManager.build().enemies.size() == 0) {
				if (!this.won) {
					this.win();
				}
			}
		}

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
					this.immune = true;
					Utils.setTimeout(() -> this.immune = false, 10000);
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

		if (this.prevTile == TileType.Enemy) {
			this.prevTile = TileType.Empty;
		}

		TileManager.build().grid[this.gridY][this.gridX] = TileType.Bomberman;
	}

	public void render(Graphics2D g2d) {
		if (this.won) {
			if (this.sprite.current == this.sprite.currentAnimation.maxFrames - 1) {
				return;
			}
		}

		if (immune && System.currentTimeMillis() / 100 % 2 == 0) {
			this.sprite.spritesheet = blinkImage;
		} else {
			this.sprite.spritesheet = this.original;
		}
		this.sprite.draw(g2d, (int) this.posX - 10, (int) this.posY - 35);

		// draw the health bar above the player with 5 squares for each health point

		// draw the hitbox as a gray square

		// g2d.setColor(Color.GRAY);
		// g2d.fillRect(posX, posY, width, height);

		g2d.setColor(Color.RED);
		g2d.fillRect((int) posX - 15, (int) posY - 20, 5 * 10, 5);
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) posX - 15, (int) posY - 20, health * 10, 5);
		g2d.setColor(Color.GRAY);
	}
}
