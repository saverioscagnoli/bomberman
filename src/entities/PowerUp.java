package entities;

import java.awt.Graphics2D;

import managers.TileManager;
import ui.Sprite;
import util.Consts;
import util.TileType;

public abstract class PowerUp extends Entity {
	public final String name;
	protected int gridX;
	protected int gridY;
	protected TileType prevTile;

	public PowerUp(String name, int posX, int posY, Sprite sprite) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0, sprite);
		this.name = name;
		this.gridX = posX / Consts.tileDims;
		this.gridY = posY / Consts.tileDims;
		this.prevTile = TileManager.build().grid[this.gridY][this.gridX];
	}

	public void die() {
		this.dead = true;
	}

	public abstract void onPickup();

	public void update(int elapsed) {
		this.sprite.update(elapsed);
	}

	public void render(Graphics2D g2d) {
		this.sprite.draw(g2d, posX, posY, Consts.tileDims, Consts.tileDims);
	}
}
