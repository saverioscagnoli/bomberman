import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import loop.Loop;
import managers.MusicManager;
import ui.MainMenu;
import ui.Menus;
import ui.PauseMenu;
import util.Consts;

class Main {
    public static void main(String[] args) throws IOException {

        JFrame win = new JFrame("bomberman");
        win.setPreferredSize(new Dimension(Consts.screenWidth + 16, Consts.screenHeight + 35));
        win.setResizable(false);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Loop loop = new Loop();
        Menus.mainMenu = new MainMenu(loop);
        Menus.pauseMenu = new PauseMenu(loop);

        win.add(loop);
        win.pack();
        win.setLocationRelativeTo(null);
        win.setVisible(true);
        loop.requestFocus();
        MusicManager.getInstance();

    }
}