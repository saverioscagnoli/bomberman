package entities.enemies;

import java.awt.Graphics2D;
import entities.Enemy;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

/**
 * The Denkyun class represents an enemy character called Denkyun in the game.
 * It extends the Enemy class and defines the behavior and rendering of the
 * Denkyun enemy.
 */
public class Denkyun extends Enemy {

	/**
	 * Constructs a Denkyun object with the specified position.
	 * 
	 * @param posX The x-coordinate of the Denkyun's position.
	 * @param posY The y-coordinate of the Denkyun's position.
	 */
	public Denkyun(int posX, int posY) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
				new Sprite("denkyun", 10, 1, "idle", new SpriteAnimation[] {
						new SpriteAnimation("idle", 10, 0, 15),
				}, 2.5f));
		this.health = 1;
		this.gridX = posX / Consts.tileDims;
		this.gridY = posY / Consts.tileDims;
		this.score = 400;
		this.health = 2;

		String[] dirs = { "up", "down", "left", "right" };
		String dir = Utils.pick(dirs);
		int i = 0;
		while (this.collide(dir) && i < Consts.maxIterations) {
			dir = Utils.pick(dirs);
			i++;
		}
		this.direction = dir;
	}

	/**
	 * Updates the Denkyun's state, including movement and grid updates.
	 * 
	 * @param elapsed The elapsed time since the last update.
	 */
	public void update(int elapsed) {
		// The enemy moves in a direction until it hits a wall, then it changes
		// direction
		super.update(elapsed);

		this.move(false);
		this.updateGrid();
	}

	/**
	 * Renders the Denkyun on the graphics context.
	 * 
	 * @param g2d The graphics context to render on.
	 */
	public void render(Graphics2D g2d) {
		int offsetX = 0;
		int offsetY = 0;

		if (this.sprite.currentAnimation.name == "explosion") {
			offsetX = -5;
			offsetY = -60;
		}
		this.sprite.draw(g2d, posX + 3 + offsetX, posY + 3 + offsetY);
	}
}
