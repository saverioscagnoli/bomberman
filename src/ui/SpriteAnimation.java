package ui;

public class SpriteAnimation {
  public String name;
  public int maxFrames;
  public int frameY;
  public int stagger;

  public SpriteAnimation(String name, int maxFrames, int frameY, int stagger) {
    this.name = name;
    this.maxFrames = maxFrames;
    this.frameY = frameY;
    this.stagger = stagger;
  }
}