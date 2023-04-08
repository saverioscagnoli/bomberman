package loop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;

public class GameLoop extends JPanel implements Runnable {
    private Thread thread; // Thread for the game loop
    private boolean open; // Flag to start adn stop the game loop
    private final int FPS = 60; // Frames per second (Editable as needed)
    private KeyHandler keyHandler; // Delcaring keyhandler
    
    public static float characterX = 100;
    public static float characterY = 100;

    public static List<Entity> entities = new ArrayList<Entity>();


    public GameLoop() {
        keyHandler = new KeyHandler(); // Create an instance of KeyHandler
        this.addKeyListener(keyHandler); // Add KeyHandler as a key listener
        this.setFocusable(true); // Make the GameLoop focusable
        this.requestFocusInWindow(); // Request focus for the GameLoop
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
        double dt = 0;
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
        float speed = 120.0f;
        characterX += keyHandler.playerX * speed * dt;
        characterY += keyHandler.playerY * speed * dt;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.fillRect((int) characterX, (int) characterY, 50, 50);

        for (Entity e : entities) {
            e.update();
            g2d.setColor(Color.BLUE);
            g2d.fillRect((int)e.posX, (int)e.posY, e.width, e.height);
        }
    }
}
