package loop;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.GameCharacter;
import entities.PowerUp;
import ui.Button;
import ui.Menus;
import util.CollisionChecker;
import util.Consts;

public class GameLoop extends JPanel implements Runnable {
    private Thread thread; // Thread for the game loop
    private boolean open; // Flag to start adn stop the game loop
    private final int FPS = 60; // Frames per second (Editable as needed)
    private final int tileDims = 48;
    private final double conversionToSec = 1000000000.0;

    public int gameState = Consts.MENU;

    public ArrayList<Button> buttons;

    public static float characterX = 100;
    public static float characterY = 100;

    public static List<Entity> entities = new ArrayList<Entity>();

    public Controller keyHandler; // Delcaring keyhandler
    public GameCharacter character;

    public float dt = 0;

    public GameLoop() {
        this.buttons = new ArrayList<Button>();
        keyHandler = new Controller(this); // Create an instance of KeyHandler and passes the gameloop to it
        character = new GameCharacter(characterX, characterY, 30, 30, 5, keyHandler, this); // Initialize character
        // after keyHandler

        // create a powerup in a random location aligned to grid tiles
        entities.add(new PowerUp((int) (Math.random() * 10) * tileDims, (int) (Math.random() * 10) * tileDims, tileDims,
                tileDims, 0, "speed"));

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
            case Consts.MENU:
                break;
            case Consts.IN_GAME: // In game
                CollisionChecker.updateAdjacentEntities(character, entities);
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
            case Consts.MENU:
                Menus.mainMenu.draw(g2d);
                break;
            case Consts.IN_GAME:
                character.render(g2d);
                g2d.fillRect((int) character.posX, (int) character.posY, (int) character.width, (int) character.height);

                for (int i = 0; i <= 1296; i += this.tileDims) {
                    g2d.drawLine(i, 0, i, 768);
                }

                for (int i = 0; i <= 768; i += this.tileDims) {
                    g2d.drawLine(0, i, 1296, i);
                }

                Iterator<Entity> iterator = entities.iterator();

                while (iterator.hasNext()) {
                    Entity e = iterator.next();
                    if (e.dead) {
                        iterator.remove();
                    } else {
                        e.update();
                        e.render(g2d);
                    }
                }

                break;
            case Consts.PAUSE: {
                Menus.pauseMenu.draw(g2d);
                break;
            }
        }
    }
}
