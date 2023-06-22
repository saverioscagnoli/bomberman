package loop;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.swing.JPanel;
import java.util.ArrayList;
import entities.Bomberman;
import managers.AnimationManager;
import managers.BombManager;
import managers.EnemyManager;
import managers.MusicManager;
import managers.PowerupManager;
import managers.TileManager;
import ui.Button;
import ui.Menus;
import util.Consts;

public class Loop extends JPanel implements Runnable {
	private static Loop instance = null;
	private Thread thread; // Thread for the game loop
	private boolean open; // Flag to start adn stop the game loop
	private final int FPS = 60; // Frames per second (Editable as needed)
	private final double conversionToSec = 1000000000.0;

	private int elapsed = 0;

	public static EnemyManager enemyManager;
	public static TileManager tileManager;
	public static BombManager bombManager;

	public int gameState = Consts.MENU;
	public ArrayList<Button> buttons;
	public static Bomberman character;
	public float dt = 0;

	private Font customFont;

	private Loop() {
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/customFont.ttf")).deriveFont(20f);
			System.out.println("Font loaded");
		} catch (IOException | FontFormatException e) {
			// Handle exception
			System.out.println("Font not found");
		}
		AnimationManager.build();
		this.buttons = new ArrayList<Button>();

		tileManager = TileManager.build();
		enemyManager = EnemyManager.getInstance();
		enemyManager.instanciateEnemies(3);
		bombManager = BombManager.build();
		this.elapsed = 0;
		this.setFocusable(true); // Make the GameLoop focusable
		this.setDoubleBuffered(true);
	}

	public static synchronized Loop build() {
		if (instance == null) {
			instance = new Loop();
		}
		return instance;
	}

	public void start() {
		character = new Bomberman(50, 50);
		this.addKeyListener(Controller.build()); // Add KeyHandler as a key listener
		this.thread = new Thread(this);
		this.open = true; // Set flag to open to start the loop
		this.thread.start();
	}

	public void stop() {
		this.open = false; // Set the flag to false to stop the loop
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
		MusicManager.getInstance().ost(this.gameState);
	}

	@Override
	public void run() {
		double last = System.nanoTime();
		dt = 0;
		double rate = 1.0 / this.FPS; // Fixed update rate (1 / FPS)

		while (this.open) {
			double now = System.nanoTime();
			dt += (now - last) / this.conversionToSec; // Convert from nanoseconds to seconds
			last = now;

			if (dt >= rate) {
				this.update(rate); // Update with the fixed rate
				dt -= rate;
			}

			// Sleep for a short duration to avoid high CPU usage
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(double dt) {
		switch (gameState) {
			case Consts.MENU:
				break;
			case Consts.IN_GAME: // In game
				this.elapsed++;
				character.update(elapsed);
				tileManager.updateTiles(elapsed);
				bombManager.updateBombs(elapsed);
				bombManager.updateExplosions(elapsed);
				enemyManager.updateEnemies(elapsed);
				PowerupManager.UpdatePowerup(elapsed);
				//CollisionChecker.updateAdjacentEntities(character);
				break;
		}
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(customFont); // TODO : fare in modo che il font venga settato una volta sola.

		switch (gameState) {
			case Consts.MENU:
				Menus.mainMenu.draw(g2d);
				break;
			case Consts.IN_GAME:
				tileManager.drawObstacles(g2d);
				tileManager.drawBasicTiles(g2d);
				character.render(g2d);
				bombManager.drawBombs(g2d);
				bombManager.drawExplosions(g2d);
				enemyManager.drawEnemies(g2d);
				PowerupManager.RenderPowerup(g2d);

				// draw player lives number in top left corner
				g2d.setColor(Color.BLACK);
				g2d.drawString("Lives: " + character.lives, 10, 20);

				break;
			case Consts.PAUSE: {
				Menus.pauseMenu.draw(g2d);
				break;
			}
		}
	}
}
