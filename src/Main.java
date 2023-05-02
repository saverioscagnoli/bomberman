import java.awt.Dimension;
import javax.swing.JFrame;

import entities.Obstacle;
import loop.GameLoop;

import java.util.ArrayList;
import java.util.Random;

class Main {
    private static final int screenWidth = 1296;
    private static final int screenHeight = 768;
    private static ArrayList<String> obstacleCoords = new ArrayList<String>(); 
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true"); // Enabling OpenGL Drivers. Only tested on my machine, but
                                                         // incredible performance gain!
        JFrame win = new JFrame("bomberman");
        win.setPreferredSize(new Dimension(Main.screenWidth, Main.screenHeight));
        win.setResizable(false);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameLoop loop = new GameLoop(win);
        generateObstacles(10);
        win.add(loop);
        win.pack();
        win.setVisible(true);
        loop.requestFocus();
    }

    public static void generateObstacles(int n) {
        // Generate n random red squares in tiles
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(screenWidth) + 1;
            int y = rand.nextInt(screenHeight) + 1;
            while (obstacleCoords.contains(x + "" + y)) {
                // To prevent stacking obstacles
                x = rand.nextInt(screenWidth) + 1;
                y = rand.nextInt(screenHeight) + 1; 
            }
            obstacleCoords.add(x + "" + y);
            Obstacle ob = new Obstacle(x, y);
            ob.normalizePos();
            GameLoop.entities.add(ob);
        }
    }
}