package ui;

/**
 * The SpriteAnimation class represents an animation for a sprite. It stores the
 * name of the animation,
 * the maximum number of frames, the frame row, and the stagger value.
 */
public class SpriteAnimation {
  /* The name of the animation */
  public String name;

  /* The maximum number of frames of the animation */
  public int maxFrames;

  /* The number of row of the animation */
  public int frameY;

  /* The stagger. The greater the value, the slower the animation. */
  public int stagger;

  /**
   * Constructs a SpriteAnimation object with the specified parameters.
   *
   * @param name      The name of the animation.
   * @param maxFrames The maximum number of frames of the animation.
   * @param frameY    The number of the row of the animation.
   * @param stagger   The stagger value for the animation.
   */
  public SpriteAnimation(String name, int maxFrames, int frameY, int stagger) {
    this.name = name;
    this.maxFrames = maxFrames;
    this.frameY = frameY;
    this.stagger = stagger;
  }
}
