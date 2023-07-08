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

/**
 * The Utils class provides various utility methods for the game.
 */
public abstract class Utils {

	/**
	 * Generates a random integer within the specified range.
	 *
	 * @param min The minimum value (inclusive).
	 * @param max The maximum value (exclusive).
	 * @return The random integer.
	 */
	public static int rng(int min, int max) {
		Random rnd = new Random();
		return rnd.nextInt(min, max);
	}

	/**
	 * Picks a random element from an array.
	 *
	 * @param arr The array to pick from.
	 * @param <T> The type of elements in the array.
	 * @return The randomly picked element.
	 */
	public static <T> T pick(T[] arr) {
		return arr[rng(0, arr.length)];
	}

	/**
	 * Normalizes the given coordinates to the nearest multiple of the tile
	 * dimension.
	 *
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return The normalized coordinates as an array [x, y].
	 */
	public static int[] normalizePos(int x, int y) {
		int gridX = ((int) (x - (x % Consts.tileDims)));
		int gridY = ((int) (y - (y % Consts.tileDims)));
		int[] gridArray = { gridX, gridY };
		return gridArray;
	}

	/**
	 * Normalizes the position of the given entity.
	 *
	 * @param entity The entity to normalize.
	 * @return The normalized position of the entity as an array [x, y].
	 */
	public static int[] normalizeEntityPos(Entity entity) {
		int pX = (int) (entity.posX + entity.width * 0.5);
		int pY = (int) (entity.posY + entity.height * 0.5);
		return normalizePos(pX, pY);
	}

	/**
	 * Executes the specified runnable after a delay, similar to the setTimeout
	 * function in JavaScript.
	 *
	 * @param runnable The runnable to execute.
	 * @param delay    The delay in milliseconds.
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

	/**
	 * Plays a sound from the specified path.
	 *
	 * @param src The path to the sound file.
	 * @return The Clip object representing the played sound.
	 */
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

	/**
	 * Loads an image from the specified path.
	 *
	 * @param src The path to the image file.
	 * @return The loaded BufferedImage object.
	 */
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

	/**
	 * Loads a custom font with the specified size.
	 *
	 * @param size The size of the font.
	 * @return The loaded Font object.
	 */
	public static Font loadFont(float size) {
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/customFont.ttf")).deriveFont(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return font;
	}

	/**
	 * Creates a new JFrame for debugging purposes. See DebugWindow.java.
	 */
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

	/**
	 * Reads a level from the specified path and returns a grid representation.
	 *
	 * @param path The path to the level file.
	 * @return The grid representing the level.
	 */
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
