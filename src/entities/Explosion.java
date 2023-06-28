package entities;

import java.awt.Graphics2D;

import entities.bosses.ClownMask;
import managers.EnemyManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;

public class Explosion extends Entity {
	private boolean isBoss;

	public Explosion(int posX, int posY, String direction, int frameY) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0,
				new Sprite("explosion", 9, 7, direction,
						new SpriteAnimation[] { new SpriteAnimation(direction, 9, frameY, 5) }, 1));

		this.isBoss = EnemyManager.build().enemies.size() == 1;
	}

	public void die() {
		this.dead = true;
	}

	@Override
	public void update(int elapsed) {
		this.sprite.update(elapsed);

		if (this.isBoss && EnemyManager.build().enemies.size() > 0) {
			Enemy b = EnemyManager.build().enemies.get(0);
			int thisLeft = this.posX;
			int thisRight = this.posX + this.width;
			int thisTop = this.posY;
			int thisBottom = this.posY + this.height;

			// Calculate the bounding boxes for 'b' object
			int bLeft = b.posX;
			int bRight = b.posX + b.width;
			int bTop = b.posY;
			int bBottom = b.posY + b.height;

			// Check for collision using bounding boxes
			if (thisRight >= bLeft && thisLeft <= bRight && thisBottom >= bTop && thisTop <= bBottom) {
				// Collision occurred

				if (b instanceof ClownMask) {
					ClownMask cm = (ClownMask) b;
					if (!cm.hit) {
						cm.stop = true;
						cm.hit = true;
					}
				}
			}
		}

		/* If the animation is finished, die */
		if (this.sprite.current == this.sprite.currentAnimation.maxFrames - 1) {
			this.die();
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		int dim = Consts.tileDims + 3;
		this.sprite.draw(g2d, this.posX, this.posY, dim, dim);
	}
}
