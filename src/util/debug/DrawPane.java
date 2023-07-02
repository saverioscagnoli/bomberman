package util.debug;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import managers.TileManager;
import util.TileType;

/* 
 * This class draws on a secondary JFrame. 
 * It draws the grid and its components as colored squares, 
 * which will be useful to see what's actually happening under the hood. 
 */

public class DrawPane extends JPanel implements Runnable {
  private Thread thread;
  private float dt;

  public DrawPane() {
    this.thread = new Thread(this);
    this.start();
  }

  public void start() {
    this.thread.start();
  }

  @Override
  public void run() {
    double last = System.nanoTime();
    dt = 0;
    double rate = 1.0 / 60; // Fixed update rate (1 / FPS)

    while (true) {
      double now = System.nanoTime();
      dt += (now - last) / 1000000000.0; // Convert from nanoseconds to seconds
      last = now;

      if (dt >= rate) {
        this.repaint(); // Update with the fixed rate
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

  public void paintComponent(Graphics g) {
    TileType[][] grid = TileManager.build().grid;
    Graphics2D g2d = (Graphics2D) g;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        TileType n = grid[i][j];

        switch (n) {
          case Empty:
            g2d.setColor(Color.GRAY);
            break;
          case Wall:
            g2d.setColor(Color.RED);
            break;
          case Obstacle:
            g2d.setColor(Color.ORANGE);
            break;
          case Bomb:
            g2d.setColor(Color.BLACK);
            break;
          case Bomberman:
            g2d.setColor(Color.GREEN);
            break;
          case Enemy: 
            g2d.setColor(Color.CYAN);
            break;
          case Hatch:
            g2d.setColor(Color.MAGENTA);
            break;
          default:
            break;
        }

        g2d.fillRect(j * 30, i * 30, 30, 30);
      }
    }

  }
}
