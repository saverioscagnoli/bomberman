package loop;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GameLoop extends JPanel implements Runnable {
    private Thread thread; // Thread for the game loop
    private boolean open; // Flag to start adn stop the game loop
    private final int FPS = 60; // Frames per second (Editable as needed)
    private final long TIME_TICK = 1000000000 / this.FPS; // Running at n FPS (change the FPS prop)

    public GameLoop() {
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
        double last = System.nanoTime(); // Time of last update
        double dt = 0; // Delta time (time since last update)

        while (this.open) {
            double now = System.nanoTime();
            dt += (now - last) / this.TIME_TICK; // Add the time since the last update to the accumulator
            if (dt >= 1) { // If the accumulator is greater than 1, update the game
                this.update(dt);
                dt--;
            }
        }
    }

    public void update(double dt) {
        // Update the game's logic here
    }

    @Override
    public void paintComponent(Graphics g) { // Drawing functon (draws a red rectangle for now)
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.fillRect(100, 100, 50, 50);
    }
}
