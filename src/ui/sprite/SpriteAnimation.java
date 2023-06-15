package ui.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpriteAnimation {
  public int width;
  public int height;
  public int frameY;
  public int maxFrames;
  public int stagger;
  public int currentFrame;
  public int scale;

  public SpriteAnimation(BufferedImage spritesheet, int rows, int scale, int frameY, int maxFrames, int stagger) {
    this.width = (int) (spritesheet.getWidth() / 6.3);
    this.height = spritesheet.getHeight() / rows;
    this.scale = scale;
    this.frameY = frameY;
    this.maxFrames = maxFrames;
    this.stagger = stagger;
    this.currentFrame = 0;
  }

  public void animate(int elapsedFrames) {
    if (elapsedFrames % this.stagger == 0) {
      if (this.currentFrame < this.maxFrames - 1) {
        this.currentFrame++;
      } else {
        this.currentFrame = 0;
      }
    }
  }

  public void draw(Graphics2D g2d, int x, int y, BufferedImage spritesheet) {
    int currentWidth = this.width * this.currentFrame;
    int currentHeight = this.height * this.frameY;
    BufferedImage spriteFrame = spritesheet.getSubimage(currentWidth, currentHeight, this.width, this.height);
    g2d.drawImage(spriteFrame, x, y, this.width * this.scale, this.height * this.scale, null);
  }
}
