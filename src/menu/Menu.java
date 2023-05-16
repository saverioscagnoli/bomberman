package menu;

import java.util.ArrayList;

import loop.GameLoop;
import utils.Consts;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Menu {
  public static ArrayList<Button> buttons = new ArrayList<Button>();
  private GameLoop loop;

  public Menu(GameLoop loop) {
    this.loop = loop;
    this.init();
  }

  public void init() {
    int buttonW = 150;
    int buttonH = 50;
    int startButtonX = (int) (Consts.screenWidth * 0.5 - buttonW * 0.5);
    int startButtonY = (int) (Consts.screenHeight * 0.5 - 30 - buttonH * 0.5);
    Button startButton = new Button("s", "Start", true,  startButtonX, startButtonY, buttonW, buttonH);
    this.addButton(startButton);

    int quitButtonX = startButtonX;
    int quitButtonY = (int) (Consts.screenHeight * 0.5 + 30 - buttonH * 0.5);
    Button quitButton = new Button("q", "Quit", true, quitButtonX, quitButtonY, buttonW, buttonH);
    this.addButton(quitButton);
  }

  public void addButton(Button button) {
    buttons.add(button);
  }

  public void draw(Graphics2D g2d) {
    FontMetrics metrics = g2d.getFontMetrics(loop.getFont());

    for (Button btn : buttons) {
     g2d.drawRect(btn.x, btn.y, btn.width, btn.height); 
     int len = metrics.stringWidth(btn.label);
     int x = (int) (btn.x + btn.width * 0.5 - len);
     int y = (int) (btn.y + btn.height * 0.5 + 10);
     g2d.drawString(btn.label, x, y);
    }
  }
}
