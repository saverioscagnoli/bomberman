package ui.sprite;

import java.awt.Graphics2D;

public class StaticSprite extends Sprite {
    public int width;
    public int height;

    public StaticSprite(String src) {
        super(src);
        this.width = this.spritesheet.getWidth();
        this.height = this.spritesheet.getHeight();
    }

    public void drawSprite(Graphics2D g2d, int x, int y) {
        g2d.drawImage(spritesheet, x, y, this.width * this.scale, this.height * this.scale, null);
    }
}
