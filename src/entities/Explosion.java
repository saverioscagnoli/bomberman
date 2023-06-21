package entities;

import java.awt.Graphics2D;
import util.Consts;
import util.Utils;

public class Explosion extends Entity {

	public Explosion(int posX, int posY) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0, "explosion", false, 9, 5, 7);

		Utils.setTimeout(() -> this.die(), 500);
	}

	@Override
	public void update() {
		super.updateSprite();
	}

	@Override
	public void render(Graphics2D g2d) {
		super.drawSprite(g2d, (int) this.posX, (int) this.posY, Consts.tileDims + 3, Consts.tileDims + 3);
	}
}
