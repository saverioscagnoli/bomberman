package ui;

public class SpriteAnimation {
  /* The name of the animation */
  public String name;

  /* The maximum number of frames of the animation */
  public int maxFrames;

  /* The number of row of the animation */
  public int frameY;

  /* The stagger. The greater, the slower the animation. */
  public int stagger;

  public SpriteAnimation(String name, int maxFrames, int frameY, int stagger) {
    this.name = name;
    this.maxFrames = maxFrames;
    this.frameY = frameY;
    this.stagger = stagger;
  }
}