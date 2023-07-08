package util;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import entities.*;
import util.debug.DebugWindow;

public abstract class Utils {

	/* Random integer generator */
	public static int rng(int min, int max) {
		Random rnd = new Random();
		return rnd.nextInt(min, max);
	}

	/* Picks a random element from any array */
	public static <T> T pick(T[] arr) {
		return arr[rng(0, arr.length)];
	}

	/* Normalise any coordinates to the nearest number of 48 (tileDims) */
	public static int[] normalizePos(int x, int y) {
		int gridX = ((int) (x - (x % Consts.tileDims)));
		int gridY = ((int) (y - (y % Consts.tileDims)));
		int[] gridArray = { gridX, gridY };
		return gridArray;
	}

	/* Normalises the position of any entity */
	public static int[] normalizeEntityPos(Entity entity) {
		int pX = (int) (entity.posX + entity.width * 0.5);
		int pY = (int) (entity.posY + entity.height * 0.5);
		return normalizePos(pX, pY);
	}

	/*
	 * A setTimeout function. Similar to javascript, it executes a given function
	 * with a delay of a specific time in milliseconds.
	 */
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

	/* A function that plays a sound given its path */
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

	/* Loads an image from a given path */
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

	/* Loads a custom font */
	public static Font loadFont(float size) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/customFont.ttf")).deriveFont(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return font;
	}

	/* Creates new JFrame for debugging purposes. See DebugWindow.java */
	public static void createDebugWindow() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new DebugWindow();
			}
		});
	}

	public static BufferedImage copyImage(BufferedImage img) {
		ColorModel cm = img.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = img.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public static TileType[][] readLevel(String path) {
		TileType[][] grid = new TileType[Consts.gridHeight][Consts.gridWidth];
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(",");
				for (int j = 0; j < tokens.length; j++) {
					String token = tokens[j];
					if (token == " ")
						continue;
					switch (token) {
						case "W": {
							grid[i][j] = TileType.Wall;
							break;
						}
						case "WD": {
							grid[i][j] = TileType.Obstacle;
							break;
						}
						case "N": {
							grid[i][j] = TileType.Empty;
							break;
						}
					}

				}
				i++;
			}
		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
		}

		return grid;
	}
}
