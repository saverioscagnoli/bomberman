package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import loop.GameLoop;
import utils.Utils;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Bomb extends Entity {
    public Bomb(float posX, float posY, int width, int height, int speed) {
        super(posX, posY, width, height, speed);
		  this.isSolid = false;
          try {bombSprite=ImageIO.read(getClass().getResourceAsStream("/spritesheet/bombs.png"));}
          catch (IOException e){e.printStackTrace();}
        //bombSprite();
        //update();
        //direction="up";
    }

    @Override
    public void update() {
        Utils.setTimeout(() -> GameLoop.entities.remove(this), 3000);
    }

    //public void bombSprite(){
    //    try{
    //        rightb=ImageIO.read(getClass().getResourceAsStream("/spritesheet/rightb.png"));
    //        leftb=ImageIO.read(getClass().getResourceAsStream("/spritesheet/leftb.png"));
    //    } catch (IOException e) {e.printStackTrace();}
    //}

    @Override
    public void render(Graphics2D g2d) {
        //g2d.setColor(Color.BLUE);
        //g2d.fillRect((int) posX, (int) posY, width, height);

        //BufferedImage bombSprite=null;
        //switch (direction){
        //    case "right":{imageb=rightb;}
        //    case "left":{imageb=leftb;}
        //    case "up":{imageb=rightb;}
        //    case "down":{imageb=leftb;}
        //}
        g2d.drawImage(bombSprite, (int) posX, (int) posY, width, height, null);
    }
}
