package entities;

import java.awt.Graphics2D;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;

public class Explosion extends Entity {

	public Explosion(int posX, int posY, String direction, int frameY) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0,
				new Sprite("explosion", 9, 7, direction,
						new SpriteAnimation[] { new SpriteAnimation(direction, 9, frameY, 5) }, 1));
	}

	public void die() {
		this.dead = true;
	}

	@Override
	public void update(int elapsed) {
		this.sprite.update(elapsed);
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
