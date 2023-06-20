package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Sprite {
  // The image used for the sprite
  public BufferedImage spritesheet;

  // The scaling factor for the sprite
  public float scale;

  // Determines if the sprite has animations
  public boolean isAnimated;

  // Determines if the sprite is static (non-animated)
  public boolean isStatic;

  // The currently active animation for the sprite
  public SpriteAnimation animation;


  // The number of frames that have elapsed for the current animation
  private int elapsedFrames;

  public Sprite(BufferedImage spritesheet, boolean isStatic) {
    // Load the image from the given source

    // Initialize the sprite properties
    this.spritesheet = spritesheet;
    this.elapsedFrames = 0;
    this.scale = 1;
    this.isAnimated = true;
    this.isStatic = isStatic;
  }

  // Add an animation to the sprite


  // Set the current animation for the sprite
  public void setAnimation(SpriteAnimation anim) {
    this.animation= anim;
  }

  // Set the scaling factor for the sprite
  public void setScale(float scale) {
    this.scale = scale;
  }

  // Update the sprite's animation
  public void updateSprite() {
    if (this.isAnimated && !this.isStatic) {
      this.elapsedFrames++;
      this.animation.animate(this.elapsedFrames);
    }
  }

  // Draw the sprite on the given graphics context at the specified position
  public void drawSprite(Graphics2D g2d, int x, int y, int... dims) {
    if (this.isStatic) {
      // If the sprite is static, draw the whole spritesheet or use custom dimensions
      // if provided
      int width = this.spritesheet.getWidth();
      int height = this.spritesheet.getHeight();
      if (dims.length == 2) {
        width = dims[0];
        height = dims[1];
      }
      int scaledX = (int) (width * this.scale);
      int scaledY = (int) (height * this.scale);
      g2d.drawImage(this.spritesheet, x, y, scaledX, scaledY, null);
    } else {
      // If the sprite is animated, delegate drawing to the current animation
      this.animation.draw(g2d, x, y, this.spritesheet, dims);
    }
  }
}
