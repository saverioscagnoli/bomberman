package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import util.Utils;

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
  public SpriteAnimation currentAnimation;

  // A map of animation names to SpriteAnimation objects
  private HashMap<String, SpriteAnimation> map;

  // The number of frames that have elapsed for the current animation
  private int elapsedFrames;

  public Sprite(String src, boolean isStatic) {
    // Load the image from the given source
    BufferedImage img = Utils.loadImage(src);

    // Initialize the sprite properties
    this.spritesheet = img;
    this.map = new HashMap<>();
    this.elapsedFrames = 0;
    this.scale = 1;
    this.isAnimated = true;
    this.isStatic = isStatic;
  }

  // Add an animation to the sprite
  public void addAnimation(String name, SpriteAnimation anim) {
    if (this.map.size() == 0) {
      // If this is the first animation added, set it as the current animation
      this.currentAnimation = anim;
    }
    this.map.put(name, anim);
  }

  // Set the current animation for the sprite
  public void setAnimation(String name) {
    this.currentAnimation = this.map.get(name);
  }


  // Set the scaling factor for the sprite
  public void setScale(float scale) {
    this.scale = scale;
  }

  // Update the sprite's animation
  public void updateSprite() {
    if (this.isAnimated && !this.isStatic) {
      this.elapsedFrames++;
      this.currentAnimation.animate(this.elapsedFrames);
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
      this.currentAnimation.draw(g2d, x, y, this.spritesheet, dims);
    }
  }
}
