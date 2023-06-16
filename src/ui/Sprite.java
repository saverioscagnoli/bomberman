package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Sprite {
  public BufferedImage spritesheet;
  public int scale;
  public boolean isAnimated;
  public boolean isStatic;
  public SpriteAnimation currentAnimation;

  private HashMap<String, SpriteAnimation> map;
  private int elapsedFrames;

  public Sprite(String src, boolean isStatic) {
    BufferedImage img = this.loadImage(src);
    this.spritesheet = img;
    this.map = new HashMap<>();
    this.elapsedFrames = 0;
    this.scale = 1;
    this.isAnimated = true;
    this.isStatic = isStatic;
  }

  public void addAdimation(String name, SpriteAnimation anim) {
    if (this.map.size() == 0) {
      this.currentAnimation = anim;
    }
    this.map.put(name, anim);
  }

  public void setAnimation(String name) {
    this.currentAnimation = this.map.get(name);
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

  public void setScale(int scale) {
    this.scale = scale;
  }

  public void updateSprite() {
    if (this.isAnimated && !this.isStatic) {
      this.elapsedFrames++;
      this.currentAnimation.animate(this.elapsedFrames);
    }
  }

  public void drawSprite(Graphics2D g2d, int x, int y, int... dims) {
    if (this.isStatic) {
      int width = this.spritesheet.getWidth();
      int height = this.spritesheet.getHeight();
      if (dims.length == 2) {
        width = dims[0];
        height = dims[1];
      }
      int scaledX = width * this.scale;
      int scaledY = height * this.scale;
      g2d.drawImage(this.spritesheet, x, y, scaledX, scaledY, null);
    } else {
      this.currentAnimation.draw(g2d, x, y, this.spritesheet);
    }
  }
}
