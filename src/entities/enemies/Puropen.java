package entities.enemies;

import java.awt.Graphics2D;
import entities.Enemy;
import ui.Sprite;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;

public class Puropen extends Enemy {
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

	public void update(int elapsed) {

		super.update(elapsed);
		this.move(true);
		this.updateGrid();
	}

	public void render(Graphics2D g2d) {
		int offsetY = 0;
		if (this.sprite.currentAnimation.name == "explosion") {
			offsetY = -30;
		}
		this.sprite.draw(g2d, posX - 5, posY - 15 + offsetY);
	}
}
