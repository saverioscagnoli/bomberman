package entities.enemies;

import java.awt.Graphics2D;
import entities.Enemy;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

/**
 * The Puropen class represents an enemy character called Puropen in the game.
 * It extends the Enemy class and defines the behavior and rendering of the
 * Puropen enemy.
 */
public class Puropen extends Enemy {

	/**
	 * Constructs a Puropen object with the specified position.
	 * 
	 * @param posX The x-coordinate of the Puropen's position.
	 * @param posY The y-coordinate of the Puropen's position.
	 */
	public Puropen(int posX, int posY) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 1,
				new Sprite("enemy-1", 4, 4, "left", new SpriteAnimation[] {
						new SpriteAnimation("down", 4, 0, 3),
						new SpriteAnimation("up", 4, 1, 3),
						new SpriteAnimation("left", 4, 2, 3),
						new SpriteAnimation("right", 4, 3, 3)
				}, 2.5f));
		this.health = 1;
		this.score = 100;

		String[] dirs = { "up", "down", "left", "right" };
		String dir = Utils.pick(dirs);
		int i = 0;
		while (this.collide(dir) && i < Consts.maxIterations) {
			dir = Utils.pick(dirs);
			i++;
		}
		this.direction = dir;
		this.sprite.setAnimation(this.direction);
	}

	/**
	 * Updates the Puropen's state, including movement and grid updates.
	 * 
	 * @param elapsed The elapsed time since the last update.
	 */
	public void update(int elapsed) {
		super.update(elapsed);

		if (this.health == 0)
			return;

		this.move(true);
		this.updateGrid();
	}

	/**
	 * Renders the Puropen on the graphics context.
	 * 
	 * @param g2d The graphics context to render on.
	 */
	public void render(Graphics2D g2d) {
		int offsetY = 0;
		if (this.sprite.currentAnimation.name == "explosion") {
			offsetY = -30;
		}
		this.sprite.draw(g2d, posX - 5, posY - 15 + offsetY);
	}
}
