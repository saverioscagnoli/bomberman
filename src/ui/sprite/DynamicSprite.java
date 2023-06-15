package ui.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class DynamicSprite extends Sprite {
  public boolean isAnimated;
  public SpriteAnimation currentAnimation;

  private HashMap<String, SpriteAnimation> map;
  private int elapsedFrames;

  public DynamicSprite(String src) {
    super(src);
    this.map = new HashMap<>();
    this.elapsedFrames = 0;
    this.scale = 1;
    this.isAnimated = true;
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

  public void setScale(int scale) {
    this.scale = scale;
  }

  public void updateSprite() {
    if (this.isAnimated) {
      this.elapsedFrames++;
      this.currentAnimation.animate(this.elapsedFrames);
    }
  }

  public void drawSprite(Graphics2D g2d, int x, int y) {
    this.currentAnimation.draw(g2d, x, y, this.spritesheet);
  }
}
