import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;
import loop.GameLoop;
import ui.MainMenu;
import ui.Menus;
import ui.PauseMenu;
import util.Consts;

class Main {
    public static void main(String[] args) throws IOException {
        // Enabling OpenGL Drivers. Only tested on my machine, but
        // incredible performance gain!
        System.setProperty("sun.java2d.opengl", "true");
        JFrame win = new JFrame("bomberman");
        win.setPreferredSize(new Dimension(Consts.screenWidth + 16, Consts.screenHeight + 35));
        win.setResizable(false);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameLoop loop = new GameLoop();
        Menus.mainMenu = new MainMenu(loop);
        Menus.pauseMenu = new PauseMenu(loop);

        win.add(loop);
        win.pack();
        win.setLocationRelativeTo(null);
        win.setVisible(true);
        loop.requestFocus();
    }
}