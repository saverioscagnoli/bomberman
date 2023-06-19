package ui;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import loop.Loop;
import util.Consts;

public class Page {
  public final ArrayList<Button> buttons;
  public final Loop loop;

  public Page(Loop loop) {
    this.buttons = new ArrayList<Button>();
    this.loop = loop;
  }

  public void addButton(Button btn) {
    this.buttons.add(btn);
  }

  public void draw(Graphics2D g2d) {
    FontMetrics metrics = g2d.getFontMetrics(this.loop.getFont());
    for (Button btn : this.buttons) {
      int buttonX = (int) ((Consts.screenWidth * 0.5) - (btn.width * 0.5));
      int buttonY = (int) ((Consts.screenHeight * 0.5) - (btn.height * 0.5) + btn.offsetY);
      int len = metrics.stringWidth(btn.label);
      int fontX = (int) (btn.x + btn.width * 0.5 - len);
      int fontY = (int) (btn.y + btn.height * 0.5 + 10);
      g2d.drawRect(buttonX, buttonY, btn.width, btn.height);
      g2d.drawString(btn.label, fontX, fontY);
    }
  }
}
