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
import managers.BombManager;
import managers.EnemyManager;
import managers.MusicManager;
import managers.TileManager;
import ui.Button;
import ui.Menus;
import util.CollisionChecker;
import util.Consts;

public class GameLoop extends JPanel implements Runnable {
    private Thread thread; // Thread for the game loop
    private boolean open; // Flag to start adn stop the game loop
    private final int FPS = 60; // Frames per second (Editable as needed)
    private final double conversionToSec = 1000000000.0;

    public static EnemyManager enemyManager;
    public static TileManager tileManager;
    public static BombManager bombManager;

    public int gameState = Consts.MENU;
    public ArrayList<Button> buttons;
    public static float characterX = 100;
    public static float characterY = 100;
    public Controller keyHandler; // Delcaring keyhandler
    public Bomberman character;
    public float dt = 0;

    private Font customFont;

    public GameLoop() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/customFont.ttf")).deriveFont(20f);
            System.out.println("Font loaded");
        } catch (IOException | FontFormatException e) {
            // Handle exception
            System.out.println("Font not found");
        }

        this.buttons = new ArrayList<Button>();

        keyHandler = new Controller(this); // Create an instance of KeyHandler and passes the gameloop to it
        character = new Bomberman(characterX, characterY, 30, 30, 5, keyHandler, this);
        character.setScale(2);

        tileManager = TileManager.getInstance();
        enemyManager = EnemyManager.getInstance();
        enemyManager.instanciateEnemies(3);
        bombManager = BombManager.getInstance();

        this.addKeyListener(keyHandler); // Add KeyHandler as a key listener
        this.setFocusable(true); // Make the GameLoop focusable
        this.setDoubleBuffered(true);
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
                Thread.sleep(1);
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
                character.update();
                tileManager.updateTiles();
                bombManager.updateBombs();
                enemyManager.updateEnemies();
                CollisionChecker.updateAdjacentEntities(character);
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
                g2d.fillRect((int) character.posX, (int) character.posY, (int) character.width, (int) character.height);

                tileManager.drawObstacles(g2d);
                character.render(g2d);
                tileManager.drawBasicTiles(g2d);
                bombManager.drawBombs(g2d);
                enemyManager.drawEnemies(g2d);

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
