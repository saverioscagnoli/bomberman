import java.awt.Dimension;
import javax.swing.JFrame;
import loop.GameLoop;

class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true"); // Enabling OpenGL Drivers. Only tested on my machine, but
                                                         // incredible performance gain!
        JFrame win = new JFrame("bomberman");
        win.setPreferredSize(new Dimension(800, 600));
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameLoop loop = new GameLoop(win);
        win.add(loop);
        win.pack();
        win.setVisible(true);
        loop.requestFocus();
    }
}