package loop;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import entities.GameCharacter;

public class GameLoop extends JPanel implements Runnable {
    private Thread thread; // Thread for the game loop
    private boolean open; // Flag to start adn stop the game loop
    private final int FPS = 60; // Frames per second (Editable as needed)
    
    public static float characterX = 100;
    public static float characterY = 100;
    
    public static List<Entity> entities = new ArrayList<Entity>();
    
    public KeyHandler keyHandler; // Delcaring keyhandler
    public GameCharacter character;

    public float dt = 0;


    public GameLoop() {
        keyHandler = new KeyHandler(this); // Create an instance of KeyHandler and passes the gameloop to it
        character = new GameCharacter(characterX, characterY, 10, 10, 5, keyHandler, this); // Initialize character after keyHandler
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
            dt += (now - last) / 1000000000.0; // Convert from nanoseconds to seconds
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
        character.update();

		  for (Entity e : entities) {  // Update all entities
				e.update();
		  }
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        System.out.println("Rendering character at (" + character.posX + ", " + character.posY + ")"); // Debugging
        character.render(g2d);

        for (Entity e : entities) {  // Render and update all entities
            e.render(g2d);
        }
    }
}
