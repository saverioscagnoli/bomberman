package entities;

import java.awt.Graphics2D;

import managers.TileManager;
import ui.Sprite;
import util.Consts;
import util.TileType;

/**
 * 
 * Represents a power-up entity in the game.
 */
public abstract class PowerUp extends Entity {
	public final String name;
	protected int gridX;
	protected int gridY;
	protected TileType prevTile;

	/**
	 * 
	 * Constructs a new PowerUp object with the specified parameters.
	 * 
	 * @param name   The name of the power-up.
	 * @param posX   The x-coordinate of the power-up's position.
	 * @param posY   The y-coordinate of the power-up's position.
	 * @param sprite The sprite representing the power-up.
	 */
	public PowerUp(String name, int posX, int posY, Sprite sprite) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, 0, sprite);
		this.name = name;
		this.gridX = posX / Consts.tileDims;
		this.gridY = posY / Consts.tileDims;
		this.prevTile = TileManager.build().grid[this.gridY][this.gridX];
	}

	/**
	 * 
	 * Performs the actions required when the power-up dies.
	 */
	public void die() {
		this.dead = true;
	}

	/**
	 * 
	 * Handles the pickup event for the power-up.
	 */
	public abstract void onPickup();

	/**
	 * 
	 * Updates the state of the power-up.
	 * 
	 * @param elapsed The elapsed time since the last update.
	 */
	public void update(int elapsed) {
		this.sprite.update(elapsed);
	}

	/**
	 * 
	 * Renders the power-up on the specified graphics context.
	 * 
	 * @param g2d The graphics context to render on.
	 */
	public void render(Graphics2D g2d) {
		this.sprite.draw(g2d, posX, posY, Consts.tileDims, Consts.tileDims);
	}
}