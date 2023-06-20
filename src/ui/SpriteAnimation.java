package ui;

public class SpriteAnimation {
  public String name;
  public int maxFrames;
  public int frameY;

  public SpriteAnimation(String name, int maxFrames, int frameY) {
    this.name = name;
    this.maxFrames = maxFrames;
    this.frameY = frameY;
  }
}