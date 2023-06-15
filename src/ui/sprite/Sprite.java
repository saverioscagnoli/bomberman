package ui.sprite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Sprite {
    public BufferedImage spritesheet;
    public int scale;

    public Sprite(String src) {
        BufferedImage img = this.loadImage(src);
        this.spritesheet = img;
    }

    protected BufferedImage loadImage(String src) {
        InputStream stream = getClass().getResourceAsStream(src);
        BufferedImage img = null;
        try {
            img = ImageIO.read(stream);
        } catch (IOException err) {
            err.printStackTrace();
        }
        return img;
    }
}
