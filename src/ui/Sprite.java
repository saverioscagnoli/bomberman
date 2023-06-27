package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import managers.AnimationManager;

public class Sprite {
  /* A buffered image for the spritesheet */
  public BufferedImage spritesheet;

  /* Width and height of the sprite */
  public int width, height;

  /* The current frame of the animation */
  public int current;

  /* The scale of the sprite */
  public float scale;

  /* The current animation of the sprite (every animation can have different ) */
  public SpriteAnimation currentAnimation;

  /* The array of animations */
  public SpriteAnimation[] animations;

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
  }

  /* Set the current animation, given its name */
  public void setAnimation(String name) {
    for (SpriteAnimation a : this.animations) {
      if (a.name == name) {
        this.currentAnimation = a;
        this.current = 0;
        break;
      }
    }
  }

  /* Update the sprite */
  public void update(int elapsed) {
    /* Slow down the animtion by staggering it */
    if (elapsed % this.currentAnimation.stagger == 0) {
      /*
       * Set the current frame to the next, or if the frame has reached the max frames
       * of the animation, reset it to 0
       */
      this.current = (this.current + 1) % this.currentAnimation.maxFrames;
    }
  }

  /*
   * Draw the sprite, if the parameter dims: [width, height] is passed, it will be
   * drawn with that dimensions, ignoring the scale of the sprite.
   */
  public void draw(Graphics2D g2d, int x, int y, int... dims) {
    int width = (int) (this.width * this.scale);
    int height = (int) (this.height * this.scale);

    if (dims.length == 2) {
      width = dims[0];
      height = dims[1];
    }

    int frameX = this.current * this.width;
    int frameY = this.currentAnimation.frameY * this.height;
    BufferedImage frame = this.spritesheet.getSubimage(frameX, frameY, this.width, this.height);
    g2d.drawImage(frame, x, y, width, height, null);
  }
}
