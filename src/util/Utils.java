package util;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import entities.*;
import managers.BombManager;
import managers.TileManager;
import ui.Button;
import util.debug.DebugWindow;

public abstract class Utils {

	public static int rng(int min, int max) {
		Random rnd = new Random();
		return rnd.nextInt(min, max);
	}

	public static <T> T pick(T[] arr) {
		return arr[rng(0, arr.length)];
	}

	public static int[] normalizePos(int x, int y) {
		int gridX = ((int) (x - (x % Consts.tileDims)));
		int gridY = ((int) (y - (y % Consts.tileDims)));
		int[] gridArray = { gridX, gridY };
		return gridArray;
	}

	public static int[] normalizeEntityPos(Entity entity) {
		int pX = (int) (entity.posX + entity.width * 0.5);
		int pY = (int) (entity.posY + entity.height * 0.5);
		return normalizePos(pX, pY);
	}

	public static boolean enemyCollision(Enemy enemy, String direction) {
		// if the enemy's normalized position is just about to hit a solid entity,
		// change direction
		int[] normalizedPos = { (int) enemy.posX, (int) enemy.posY };
		switch (direction) {
			case "left":
				normalizedPos[0] -= enemy.width;
				break;
			case "up":
				normalizedPos[1] -= enemy.width;
				break;
			case "right":
				normalizedPos[0] += enemy.width;
				break;
			case "down":
				normalizedPos[1] += enemy.width;
				break;
		}

		ArrayList<Obstacle> obstacles = TileManager.build().walls;
		ArrayList<Bomb> bombs = BombManager.build().bombs;
		ArrayList<Entity> obstaclesAndBombs = new ArrayList<>();
		obstaclesAndBombs.addAll(obstacles);
		obstaclesAndBombs.addAll(bombs);

		for (Entity e : obstaclesAndBombs) {
			if (e.posX == normalizedPos[0] && e.posY == normalizedPos[1]) {
				return true;
			}
		}

		return false;
	}

	public static boolean buttonClick(int mouseX, int mouseY, Button btn) {
		return ((mouseX >= btn.x && mouseX <= btn.x + btn.width) && (mouseY >= btn.y && mouseY <= btn.y + btn.height));
	}

	public static void setTimeout(Runnable runnable, int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e) {
				System.err.println(e);
			}
		}).start();
	}

	public static Clip playSound(String src) {
		Clip clip = null;
		try {
			File path = new File(src);
			AudioInputStream audio = AudioSystem.getAudioInputStream(path);
			clip = AudioSystem.getClip();
			clip.open(audio);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clip;
	}

	// Load an image from the given source
	public static BufferedImage loadImage(String src) {
		File path = new File(src);
		BufferedImage img = null;
		try {
			InputStream stream = new FileInputStream(path);
			img = ImageIO.read(stream);
		} catch (IOException err) {
			err.printStackTrace();
		}
		return img;
	}

	public static void createFrame() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new DebugWindow();
			}
		});
	}

}
