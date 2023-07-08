package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import managers.AnimationManager;

/**
 * The Sprite class represents a sprite with animations. It manages the
 * spritesheet, dimensions,
 * current frame, scale, animations, and provides methods for updating and
 * drawing the sprite.
 */
public class Sprite {
  /* A buffered image for the spritesheet */
  public BufferedImage spritesheet;

  /* Width and height of the sprite */
  public int width, height;

  /* The current frame of the animation */
  public int current;

  /* The scale of the sprite */
  public float scale;

  /*
   * The current animation of the sprite (every animation can have different
   * frames)
   */
  public SpriteAnimation currentAnimation;

  /* The array of animations */
  public SpriteAnimation[] animations;

  public int cycles;

  /**
   * Constructs a Sprite object with the specified parameters.
   *
   * @param spriteName      The name of the spritesheet.
   * @param abs             The absolute value used to calculate the width of each
   *                        frame.
   * @param rows            The number of rows in the spritesheet.
   * @param currentAnimName The name of the initial animation.
   * @param anims           The array of sprite animations.
   * @param scale           The scale of the sprite.
   */
  public Sprite(String spriteName, double abs, int rows, String currentAnimName, SpriteAnimation[] anims, float scale) {
    /* Set all the properties to their initial values */
    this.spritesheet = AnimationManager.build().getSprite(spriteName);
    this.width = (int) (this.spritesheet.getWidth() / abs);
    this.height = this.spritesheet.getHeight() / rows;
    this.scale = scale;
    this.current = 0;
    this.animations = anims;
    if (this.animations != null) {
      this.setAnimation(currentAnimName);
    }
    this.cycles = 0;
  }

  /**
   * Sets the current animation based on its name.
   *
   * @param name The name of the animation to set.
   */
  public void setAnimation(String name) {
    for (SpriteAnimation a : this.animations) {
      if (a.name.equals(name)) {
        this.currentAnimation = a;
        this.current = 0;
        break;
      }
    }
  }

  /**
   * Updates the sprite animation.
   *
   * @param elapsed The elapsed time since the last update.
   */
  public void update(int elapsed) {
    /* Slow down the animation by staggering it */
    if (elapsed % this.currentAnimation.stagger == 0) {
      /*
       * Set the current frame to the next, or if the frame has reached the max frames
       * of the animation, reset it to 0
       */
      this.current = (this.current + 1) % this.currentAnimation.maxFrames;
      if (this.current == 0) {
        this.cycles++;
      }
    }
  }

  /**
   * Draws the sprite on the specified graphics context.
   *
   * @param g2d  The graphics context.
   * @param x    The x-coordinate of the sprite's position.
   * @param y    The y-coordinate of the sprite's position.
   * @param dims Optional dimensions for the sprite. If provided, the sprite will
   *             be drawn
   *             with the specified width and height, ignoring the scale of the
   *             sprite.
   */
  public void draw(Graphics2D g2d, double x, double y, int... dims) {
    int width = (int) (this.width * this.scale);
    int height = (int) (this.height * this.scale);

    if (dims.length == 2) {
      width = dims[0];
      height = dims[1];
    }

    int frameX = this.current * this.width;
    int frameY = this.currentAnimation.frameY * this.height;
    BufferedImage frame = this.spritesheet.getSubimage(frameX, frameY, this.width, this.height);
    g2d.drawImage(frame, (int) x, (int) y, width, height, null);
  }
}
