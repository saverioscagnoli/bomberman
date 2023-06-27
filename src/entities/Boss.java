package entities;

import ui.Sprite;

public abstract class Boss extends Entity {

  public Boss(int posX, int posY, int width, int height, int speed, Sprite sprite) {
    super(posX, posY, width, height, speed, sprite);
  }
}
