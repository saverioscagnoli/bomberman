package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

import core.Loop;
import ui.Sprite;
import ui.SpriteAnimation;
import managers.BombManager;
import managers.EnemyManager;
import managers.LevelManager;
import managers.MouseManager;
import managers.SaveManager;
import managers.TileManager;
import util.*;

/**
 * The `Bomberman` class represents the player-controlled character in the game.
 * It extends the `Entity` class and includes properties and methods specific to
 * the player character.
 */
public class Bomberman extends Entity {

	/* The bomb radius, determining the range of the player's bombs */
	public int bombRadius;

	/* The maximum number of bombs the player can place at the same time */
	public int maxBombs;

	/* The health points of the player */
	public int health;

	/* A flag to determine if the player is immune and will not take damage */
	public boolean immune;

	/* A flag to allow the player to pass through walls */
	public boolean passThroughWalls;

	/* The number of lives the player has */
	public int lives;

	/* A flag to stop the animation if the player is not moving */
	public boolean stop;

	/* The keyboard keys pressed by the player to control movement */
	public ArrayList<String> keys;

	/* The player's position on the game grid */
	private int gridX = 1;
	private int gridY = 1;

	/* The type of tile on which the player was previously located */
	private TileType prevTile = TileType.Empty;

	/* The score of the player */
	public int score;

	/* The direction in which the player is facing */
	public String direction;

	/* A flag indicating whether the player has won the game */
	public boolean won;

	private BufferedImage blinkImage;
	private BufferedImage original;

	/**
	 * Constructs a new `Bomberman` object with the specified position.
	 *
	 * @param posX The X-coordinate of the player's position.
	 * @param posY The Y-coordinate of the player's position.
	 */
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

		/* Set the properties to their initial states */
		this.keys = new ArrayList<>();
		this.health = 1;
		this.maxBombs = 1;
		this.bombRadius = 1;
		this.lives = 5;
		this.score = 0;
		this.won = false;
		this.original = this.sprite.spritesheet;
		this.blinkImage = Utils.copyImage(this.sprite.spritesheet);
		this.passThroughWalls = false;

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

	/**
	 * Triggers the death of the player.
	 * This method is called when the player loses all health points and loses a
	 * life.
	 */
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
			Loop.build().setState(GameState.ContinueScreen);
			// game over
		} else {
			if (this.speed > 1) {
				this.speed--;
			}
			if (!CollisionChecker.SolidTiles.contains(TileType.Obstacle)) {
				CollisionChecker.SolidTiles.add(TileType.Obstacle);
				this.passThroughWalls = false;
			}

		}
	}

	/**
	 * Places a bomb at the player's current position if possible.
	 * This method is called when the player triggers the bomb placement action.
	 */
	public void placeBomb() {
		BombManager bombManager = BombManager.build();
		if (bombManager.bombs.size() >= this.maxBombs) {
			return;
		}
		if (this.prevTile != TileType.Empty)
			return;

		this.prevTile = TileType.Bomb;

		Utils.playSound(Consts.soundPath + "place-bomb.wav");
		TileManager.build().grid[this.gridY][this.gridX] = TileType.Bomb;

		int x = this.gridX * Consts.tileDims;
		int y = this.gridY * Consts.tileDims;
		bombManager.addBomb(new Bomb(x, y, this.bombRadius));
	}

	/**
	 * Triggers the player's victory.
	 * This method is called when the player successfully completes a level.
	 */
	public void win() {
		this.won = true;
		// this.sprite = new Sprite("bomberman-hatch", 9, 1, "idle", new
		// SpriteAnimation[] {
		// new SpriteAnimation("idle", 9, 0, 10)
		// }, 2.5f);
		SaveManager.incrementLevel();
		SaveManager.incrementWins();
		Loop.build().overlay.repaint();

		if (!(SaveManager.readProgress().get("level").equals("7"))) {
			Loop.build().setState(GameState.StageCleared);
		}
		LevelManager.build().loadNextLevel();

		this.won = false;
		this.keys.clear();
	}

	@Override
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

					MouseManager mouseManager = MouseManager.build();
					if (mouseManager.enabled) {
						mouseManager.enabled = false;
						Utils.setTimeout(() -> {
							mouseManager.horizontalMovement = false;
							mouseManager.verticalMovement = false;
							mouseManager.enabled = true;
						}, 100);
					}
				}
			}
			return;
		}

		// TODO: It would be more organized to create a dedicated method in the
		// collision checker for player movement.
		// It could potentially be compressed further.
		this.sprite.update(elapsed);
		this.speed = 5;

		if (!this.keys.isEmpty()) {

			if (!this.sprite.currentAnimation.name.equals(this.direction)) {
				this.sprite.setAnimation(this.direction);
			}

			TileManager tileManager = TileManager.build();

			/* Update the player position on the grid */
			int prevX = this.gridX;
			int prevY = this.gridY;
			int[] normPos = Utils.normalizePos((int) (this.posX + this.width * 0.5), (int) (this.posY + this.height * 0.5));
			this.gridX = normPos[0] / Consts.tileDims;
			this.gridY = normPos[1] / Consts.tileDims;

			/* Reset the tile on which the player was to its original state */

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

	@Override
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
	}
}
