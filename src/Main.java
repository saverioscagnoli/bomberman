import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;
import loop.GameLoop;
import lvl.LevelManager;
import ui.MainMenu;
import ui.Menus;
import ui.PauseMenu;
import util.Consts;

class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("sun.java2d.opengl", "true"); // Enabling OpenGL Drivers. Only tested on my machine, but
        // incredible performance gain!
        JFrame win = new JFrame("bomberman");
        win.setPreferredSize(new Dimension(Consts.screenWidth, Consts.screenHeight));
        win.setResizable(false);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameLoop loop = new GameLoop();
        Menus.mainMenu = new MainMenu(loop);
        Menus.pauseMenu = new PauseMenu(loop);

        LevelManager.genLevel(10);

        win.add(loop);
        win.pack();
        win.setVisible(true);
        loop.requestFocus();
    }
}