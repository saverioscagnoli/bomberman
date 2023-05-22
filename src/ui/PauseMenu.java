package ui;

import loop.GameLoop;
import util.Consts;

public class PauseMenu extends Page {
  public PauseMenu(GameLoop loop) {
    super(loop);
    this.init();
  }

  private void init() {
    int resumeButtonX = (int) (Consts.screenWidth * 0.5 - Consts.buttonWidth * 0.5);
    int resumeButtonY = (int) (Consts.screenHeight * 0.5 - Consts.buttonHeight * 0.5);
    Button resumeButton = new Button("r", "Resume", resumeButtonX, resumeButtonY, 0, Consts.buttonWidth, Consts.buttonHeight);
    this.addButton(resumeButton);
  }
}
