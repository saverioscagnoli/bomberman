package ui;
import loop.GameLoop;
import util.Consts;


public class MainMenu extends Page {
  public MainMenu(GameLoop loop) {
    super(loop);
    this.init();
  }

  public void init() {
    int startButtonX = (int) (Consts.screenWidth * 0.5 - Consts.buttonWidth * 0.5);
    int startButtonY = (int) (Consts.screenHeight * 0.5 - 30 - Consts.buttonHeight * 0.5);
    Button startButton = new Button("s", "Start", startButtonX, startButtonY,-30, Consts.buttonWidth, Consts.buttonHeight);
    addButton(startButton);

    int quitButtonX = startButtonX;
    int quitButtonY = (int) (Consts.screenHeight * 0.5 + 30 - Consts.buttonHeight * 0.5);
    Button quitButton = new Button("q", "Quit", quitButtonX, quitButtonY, 30, Consts.buttonWidth, Consts.buttonHeight);
    addButton(quitButton);
  }
}
