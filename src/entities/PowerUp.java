package entities;

import loop.GameLoop;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import loop.GameLoop;
import utils.Consts;
import utils.Utils;
import javax.imageio.ImageIO;
import java.io.IOException;

public class PowerUp extends Entity {
	public PowerUp(float posX, float posY, int width, int height, int speed) {
		super(posX, posY, Consts.tileDims, Consts.tileDims, speed);
	}

	public void onPickup(GameCharacter character) {
		GameLoop.entities.remove(this);
		character.speed +=5;
	}

	public void render(Graphics2D g2d){
		// draw an orange rectangle for the powerup
		g2d.setColor(Color.ORANGE);
		g2d.fillRect((int) posX, (int) posY, width, height);

	}
}
