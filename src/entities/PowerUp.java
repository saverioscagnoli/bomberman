package entities;

import loop.PowerupManager;

import java.awt.Graphics2D;

import util.Consts;
import util.Utils;

public class PowerUp extends Entity {
	public final String name;

	public PowerUp(float posX, float posY, int width, int height, int speed, String name) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, speed, "");
		this.name = name;
	}

	public void onPickup(int time, Runnable buff, Runnable expire) {
		// buff and expire are 2 lambda functions that will be executed when the powerup
		// gets picked up and expires
		// time is the expiration time in milliseconds
		this.die();
		buff.run();
		Utils.setTimeout(() -> expire.run(), time);
	}

	public void render(Graphics2D g2d) {
		PowerupManager.RenderPowerup(this, g2d);
	}
}
