import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import core.Loop;
import managers.SaveManager;
import util.Consts;
import util.Utils;

class JBomberMan {
    private static final boolean DEBUG = false;

    public static void main(String[] args) throws IOException {
        JFrame win = new JFrame("Super Bomberman");
        SaveManager.resetLevel();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setResizable(false);
        win.setPreferredSize(new Dimension(Consts.screenWidth, Consts.screenHeight + 120));

        Container cp = win.getContentPane();

        JPanel container = new JPanel();
        Loop loop = Loop.build(container);
        container.setLayout(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        container.add(loop);

        cp.setLayout(new BorderLayout());

        cp.add(container, BorderLayout.CENTER);

        win.pack();
        win.setLocationRelativeTo(null);
        win.setVisible(true);

        loop.requestFocus();

        if (DEBUG) {
            Utils.createDebugWindow();
        }
    }
}