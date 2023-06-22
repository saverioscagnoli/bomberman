package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import managers.AnimationManager;

public class Sprite {
  public BufferedImage spritesheet;
  public int width, height;
  public int current;
  public float scale;
  public SpriteAnimation currentAnimation;
  public SpriteAnimation[] animations;

  public Sprite(String spriteName, double abs, int rows, String currentAnimName, SpriteAnimation[] anims, float scale) {
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

  public void setAnimation(String name) {
    for (SpriteAnimation a : this.animations) {
      if (a.name == name) {
        this.currentAnimation = a;
        this.current = 0;
        break;
      }
    }
  }

  public void update(int elapsed) {
    if (elapsed % this.currentAnimation.stagger == 0) {
      this.current = (this.current + 1) % this.currentAnimation.maxFrames;
    }
  }

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
