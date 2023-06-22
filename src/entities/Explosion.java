package entities;

import java.awt.Graphics2D;

import managers.BombManager;
import managers.TileManager;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;

public class Explosion extends Entity {
	private int i = this.posY / Consts.tileDims;
	private int j = this.posX / Consts.tileDims;
	private int l = TileManager.build().grid.length - 1;

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
		if (this.sprite.current == this.sprite.currentAnimation.maxFrames - 1) {
			this.die();
		}
		if (this.sprite.current == 2 && this.sprite.currentAnimation.frameY != 1) {
			if (this.i >= 0 && this.i < this.l - 1 && this.j >= 0 && this.j < this.l - 1) {
				if (TileManager.build().grid[this.i][this.j] == "B") {
					BombManager
							.build().bombs
							.stream()
							.filter(b -> b.posX == this.posX && b.posY == this.posY)
							.findFirst()
							.get()
							.explode();
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		int dim = Consts.tileDims + 3;
		this.sprite.draw(g2d, this.posX, this.posY, dim, dim);
	}
}
