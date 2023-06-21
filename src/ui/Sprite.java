package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import managers.AnimationManager;

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
  public int currentFrame;
  public int stagger;
  public int rows;
  public double absoluteFrames;
  public int spriteWidth;
  public int spriteHeight;

  private String spritesheetName;

  public Sprite(String spritesheetName, boolean isStatic, double absoluteFrames, int stagger, int rows) {

    // Initialize the sprite properties
    this.spritesheetName = spritesheetName;
    this.spritesheet = AnimationManager.spritesheets.get(spritesheetName);
    this.elapsedFrames = 0;
    this.scale = 1;
    this.isAnimated = true;
    this.isStatic = isStatic;
    this.currentFrame = 0;
    this.stagger = stagger;
    this.rows = rows;
    this.absoluteFrames = absoluteFrames;
    this.spriteWidth = (int) (spritesheet.getWidth() / absoluteFrames);
    this.spriteHeight = spritesheet.getHeight() / rows;
  }

  // Set the current animation for the sprite
  public void setAnimation(String animName) {
    this.animation = AnimationManager.animations.get(this.spritesheetName).get(animName);
  }

  // Set the scaling factor for the sprite
  public void setScale(float scale) {
    this.scale = scale;
  }

  // Update the sprite's animation
  public void updateSprite() {
    if (this.isAnimated && !this.isStatic) {
      this.elapsedFrames++;
      if (this.elapsedFrames % this.stagger == 0) {
        if (this.currentFrame < this.animation.maxFrames - 1) {
          this.currentFrame++;
        } else {
          this.currentFrame = 0;
        }
      }
    }
  }

  // Draw the sprite on the given graphics context at the specified position
  public void drawSprite(Graphics2D g2d, int x, int y, int... dims) {
    if (this.isStatic) {
      // If the sprite is static, draw the whole spritesheet or use custom dimensions
      // if provided
      int width = (int) (this.spriteWidth * this.scale);
      int height = (int) (this.spriteHeight * this.scale);
      if (dims.length == 2) {
        width = dims[0];
        height = dims[1];
      }
      int scaledX = (int) (width * this.scale);
      int scaledY = (int) (height * this.scale);
      g2d.drawImage(this.spritesheet, x, y, scaledX, scaledY, null);
    } else {
      // If the sprite is animated, delegate drawing to the current animation
      int spriteX = (int) this.currentFrame * this.spriteWidth;
      int spriteY = this.animation.frameY * this.spriteHeight;
      int width = (int) (this.spriteWidth * this.scale);
      int height = (int) (this.spriteHeight * this.scale);
      if (dims.length == 2) {
        width = dims[0];
        height = dims[1];
      }
      BufferedImage frame = this.spritesheet.getSubimage(spriteX, spriteY, this.spriteWidth, this.spriteHeight);
      g2d.drawImage(frame, x, y, width, height, null);
    }
  }
}
