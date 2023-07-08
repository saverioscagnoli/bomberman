package entities;

import java.awt.Graphics2D;
import entities.bosses.ClownMask;
import entities.bosses.FaralsBoss;
import managers.EnemyManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import core.Loop;

/**
 * 
 * Represents an explosion entity in the game.
 */
public class Explosion extends Entity {

	/**
	 * 
	 * Constructs a new Explosion object with the specified parameters.
	 * 
	 * @param posX      The x-coordinate of the explosion's position.
	 * @param posY      The y-coordinate of the explosion's position.
	 * @param direction The direction of the explosion.
	 * @param frameY    The starting frame index of the explosion animation.
	 */
	public Explosion(int posX, int posY, String direction, int frameY) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0,
				new Sprite("explosion", 9, 7, direction,
						new SpriteAnimation[] { new SpriteAnimation(direction, 9, frameY, 5) }, 1));
	}

	/**
	 * 
	 * Performs the actions required when the explosion dies.
	 */
	public void die() {
		this.dead = true;
	}

	/**
	 * 
	 * Updates the state of the explosion.
	 * 
	 * @param elapsed The elapsed time since the last update.
	 */
	@Override
	public void update(int elapsed) {
		this.sprite.update(elapsed);

		/* If the animation is finished, die */
		if (this.sprite.current == this.sprite.currentAnimation.maxFrames - 1) {
			this.die();
		} else if (this.sprite.current == 1) {
			if (EnemyManager.build().enemies.size() > 0) {
				for (Enemy enemy : EnemyManager.build().enemies) {
					int thisLeft = this.posX;
					int thisRight = this.posX + this.width;
					int thisTop = this.posY;
					int thisBottom = this.posY + this.height;
					Bomberman player = Loop.build().bomberman;

					boolean AABB_player = (thisRight >= player.posX && thisLeft <= player.posX + player.width
							&& thisBottom >= player.posY && thisTop <= player.posY + player.height);

					boolean AABB_enemy = (thisRight >= enemy.posX && thisLeft <= enemy.posX + enemy.width
							&& thisBottom >= enemy.posY && thisTop <= enemy.posY + enemy.height);

					// Check for collision using bounding boxes
					if (AABB_enemy) {
						// Collision occurred
						if (enemy instanceof ClownMask) {
							ClownMask cm = (ClownMask) enemy;
							if (!cm.hit) {
								cm.stop = true;
								cm.hit = true;
							}
						} else if (enemy instanceof FaralsBoss) {
							FaralsBoss fb = (FaralsBoss) enemy;
							if (!fb.hit) {
								fb.stop = true;
								fb.hit = true;
							}
						} else {
							if (!enemy.dead) {
								enemy.dealDamage(1);
							}
						}
					}
					if (AABB_player) {
						// Collision occurred
						if (!player.dead && !player.immune) {
							player.die();
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * Renders the explosion on the specified graphics context.
	 * 
	 * @param g2d The graphics context to render on.
	 */
	@Override
	public void render(Graphics2D g2d) {
		int dim = Consts.tileDims + 3;
		this.sprite.draw(g2d, this.posX, this.posY, dim, dim);
	}
}