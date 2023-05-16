package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import loop.GameLoop;
import utils.Utils;

public class Bomb extends Entity {
    public Bomb(float posX, float posY, int width, int height, int speed) {
        super(posX, posY, width, height, speed);
    }

    @Override
    public void update() {
        Utils.setTimeout(() -> GameLoop.entities.remove(this), 3000);
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect((int) posX, (int) posY, width, height);
    }
}
