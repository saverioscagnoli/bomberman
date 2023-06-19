package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpriteAnimation {
  // The width of each frame in the spritesheet
  public int width;

  // The height of each frame in the spritesheet
  public int height;

  // The vertical position (row) of frames in the spritesheet
  public int frameY;

  // The total number of frames in the animation
  public int maxFrames;

  // The interval between frames in the animation
  public int stagger;

  // The current frame index
  public int currentFrame;

  // The scaling factor for the animation
  public float scale;

  public SpriteAnimation(BufferedImage spritesheet, int rows, double absoluteMaxFrames, float scale, int frameY,
      int maxFrames, int stagger) {
    // Calculate the width of each frame based on the spritesheet dimensions and the
    // maximum number of frames
    this.width = (int) (spritesheet.getWidth() / absoluteMaxFrames);

    // Calculate the height of each frame based on the number of rows in the
    // spritesheet
    this.height = spritesheet.getHeight() / rows;

    // Set the scaling factor
    this.scale = scale;

    // Set the vertical position (row) of frames
    this.frameY = frameY;

    // Set the total number of frames in the animation
    this.maxFrames = maxFrames;

    // Set the interval between frames
    this.stagger = stagger;

    // Initialize the current frame index to 0
    this.currentFrame = 0;
  }

  // Update the animation based on the elapsed frames
  public void animate(int elapsedFrames) {
    if (elapsedFrames % this.stagger == 0) {
      // If the elapsed frames match the stagger interval, advance to the next frame
      if (this.currentFrame < this.maxFrames - 1) {
        this.currentFrame++;
      } else {
        // If the current frame is the last frame, wrap around to the first frame
        this.currentFrame = 0;
      }
    }
  }

  // Draw the current frame of the animation at the specified position
  public void draw(Graphics2D g2d, int x, int y, BufferedImage spritesheet, int... dims) {
    // Calculate the coordinates and dimensions of the current frame
    int currentWidth = this.width * this.currentFrame;
    int currentHeight = this.height * this.frameY;

    // Extract the current frame from the spritesheet
    BufferedImage spriteFrame = spritesheet.getSubimage(currentWidth, currentHeight, this.width, this.height);

    int scaledWidth = (int) (this.width * this.scale);
    int scaledHeight = (int) (this.height * this.scale);

    if (dims.length > 1) {
      scaledWidth = dims[0];
      scaledHeight = dims[1];
    }

    // Draw the current frame at the specified position with scaling
    g2d.drawImage(spriteFrame, x, y, scaledWidth, scaledHeight, null);
  }
}
