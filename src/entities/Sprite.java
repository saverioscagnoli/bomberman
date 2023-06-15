package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Sprite {
  public int width;
  public int height;
  public int maxFrames;
  public int hold;
  public int scale;
  public boolean isAnimated;
  public int currentFrame;

  private HashMap<String, BufferedImage> map;
  private int elapsedFrames;
  private BufferedImage currentImage;

  public Sprite(int maxFrames, int hold) {
    this.map = new HashMap<>();
    this.maxFrames = maxFrames;
    this.hold = hold;
    this.currentFrame = 0;
    this.elapsedFrames = 0;
    this.scale = 1;
    this.isAnimated = true;
  }

  public void addImage(String name, String src) {
    BufferedImage img = this.loadImage(src);
    if (map.size() == 0) {
      this.currentImage = img;
      this.width = img.getWidth() / maxFrames;
      this.height = img.getHeight();
    }
    this.map.put(name, img);
  }

  private BufferedImage loadImage(String src) {
    InputStream stream = getClass().getResourceAsStream(src);
    BufferedImage img = null;
    try {
      img = ImageIO.read(stream);
    } catch (IOException err) {
      err.printStackTrace();
    }
    return img;
  }

  public void switchImage(String name) {
    this.currentImage = this.map.get(name);
    this.width = this.currentImage.getWidth() / maxFrames;
    this.height = this.currentImage.getHeight();
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public void animate() {
    if (this.isAnimated) {
      this.elapsedFrames++;
      if (this.elapsedFrames % this.hold == 0) {
        if (this.currentFrame < this.maxFrames - 1) {
          this.currentFrame++;
        } else {
          this.currentFrame = 0;
        }
      }
    }
  }

  public void draw(Graphics2D g2d, int x, int y) {
    int currentWidth = this.width * this.currentFrame;
    BufferedImage currentFrame = this.currentImage.getSubimage(currentWidth, 0, this.width, this.height);
    g2d.drawImage(currentFrame, x, y, this.width * this.scale, this.height * this.scale, null);
  }
}
