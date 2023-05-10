package loop;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.GameCharacter;
import menu.Button;
import menu.Menu;

public class GameLoop extends JPanel implements Runnable {
    private Thread thread; // Thread for the game loop
    private boolean open; // Flag to start adn stop the game loop
    private final int FPS = 60; // Frames per second (Editable as needed)
    private final int tileDims = 48;
    private final double conversionToSec = 1000000000.0;

    public int gameState = 1; // 1 - Menu 2 - In game 3 - Editor 4 - Pause

    public ArrayList<Button> buttons;

    public static float characterX = 100;
    public static float characterY = 100;

    public static List<Entity> entities = new ArrayList<Entity>();

    public KeyHandler keyHandler; // Delcaring keyhandler
    public GameCharacter character;
	 public CollisionHandler collisionHandler;

    public float dt = 0;

    public GameLoop() {
        this.buttons = new ArrayList<Button>();
        keyHandler = new KeyHandler(this); // Create an instance of KeyHandler and passes the gameloop to it
        character = new GameCharacter(characterX, characterY, 30, 30, 5, keyHandler, this); // Initialize character
		  collisionHandler = new CollisionHandler(this, character);
        // after keyHandler
        this.addKeyListener(keyHandler); // Add KeyHandler as a key listener
        this.setFocusable(true); // Make the GameLoop focusable
        setDoubleBuffered(true);
        this.start();
    }

    public void start() {
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
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(double dt) {
        switch (this.gameState) {
            case 1: // Menu
                break;
            case 2: // In game
				collisionHandler.CheckCollisions(entities);
                character.update();
                break;
        }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font(getFont().getName(), Font.PLAIN, 20));

        switch (this.gameState) {
            case 1:
                Menu mainMenu = new Menu(this);
                mainMenu.draw(g2d);
                break;
            case 2:
                character.render(g2d);

                for (int i = 0; i <= 1296; i += this.tileDims) {
                    g2d.drawLine(i, 0, i, 768);
                }

                for (int i = 0; i <= 768; i += this.tileDims) {
                    g2d.drawLine(0, i, 1296, i);
                }

                for (Entity e : entities) { // Render and update all entities
                    e.update();
                    e.render(g2d);
                }
                break;
        }
    }
}
