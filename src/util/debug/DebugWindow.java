package util.debug;

import javax.swing.JFrame;

/* Create a new window for debugging purposes */
public class DebugWindow extends JFrame {
  public DebugWindow() {
    super("Debug Window");
    setContentPane(new DrawPane());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setVisible(true);
  }
}
