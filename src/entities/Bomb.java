package entities;

import java.awt.Graphics2D;

import loop.GameLoop;
import util.Consts;
import util.Utils;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Bomb extends Entity {

    public Bomb(float posX, float posY, int width, int height, int speed) {
        super(posX, posY, width, height, speed);
        this.isSolid = false;
        try {
            bombSprite = ImageIO.read(getClass().getResourceAsStream("/spritesheet/bombs.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.setTimeout(() -> this.explode(), 3000);
        // bombSprite();
        // direction="up";
    }

    @Override
    public void update() {
    }

    // public void bombSprite(){
    // try{
    // rightb=ImageIO.read(getClass().getResourceAsStream("/spritesheet/rightb.png"));
    // leftb=ImageIO.read(getClass().getResourceAsStream("/spritesheet/leftb.png"));
    // } catch (IOException e) {e.printStackTrace();}
    // }

    private void explode() {
        int[][] surr = {
                { (int) this.posX - Consts.tileDims, (int) this.posY },
                { (int) this.posX + Consts.tileDims, (int) this.posY },
                { (int) this.posX, (int) this.posY - Consts.tileDims },
                { (int) this.posX, (int) this.posY + Consts.tileDims }
        };

        for (int i = 0; i < surr.length; i++) {
            if (this.checkSolid(surr[i][0], surr[i][1]))
                continue;
            GameLoop.entities.add(new Explosion(surr[i][0], surr[i][1]));
        }
        this.die();
    }

    private boolean checkSolid(int posX, int posY) {
        for (int i = 0; i < GameLoop.entities.size(); i++) {
            Entity e = GameLoop.entities.get(i);
            if (e.posX == posX && e.posY == posY) {
                if (e.isSolid) {
                    Obstacle wall = (Obstacle) e;
                    if (wall.destructable) {
                        Utils.setTimeout(() -> wall.die(), 100);
                    }
                }
                return true;
            }

        }
        return false;
    }

    @Override
    public void render(Graphics2D g2d) {
        // g2d.setColor(Color.BLUE);
        // g2d.fillRect((int) posX, (int) posY, width, height);

        // BufferedImage bombSprite=null;
        // switch (direction){
        // case "right":{imageb=rightb;}
        // case "left":{imageb=leftb;}
        // case "up":{imageb=rightb;}
        // case "down":{imageb=leftb;}
        // }
        g2d.drawImage(bombSprite, (int) posX - 25, (int) posY - 25, width * 2, height * 2, null);
    }
}
