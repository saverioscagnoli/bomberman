package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import loop.GameLoop;
import loop.Controller;

public class GameCharacter extends Entity {

    Controller keyHandler;
    GameLoop gameLoop;

    public GameCharacter(float posX, float posY, int width, int height, int speed, Controller keyHandler,
            GameLoop gameLoop) {
        super(posX, posY, width, height, speed);
        this.gameLoop = gameLoop;
        this.keyHandler = keyHandler;
        playerSprite();
        direction="up";
    }

    public void update() {

        if (keyHandler.buttonPriorities.isEmpty() == false) {
            switch (keyHandler.buttonPriorities.get(0)) {
                case "A":
                    posX -= speed;
                    direction="left";
                    break;
                case "D":
                    posX += speed;
                    direction="right";
                    break;
                case "W":
                    posY -= speed;
                    direction="up";
                    break;
                case "S":
                    posY += speed;
                    direction="down";
                    break;
            }
            spriteCounter++;
            if (spriteCounter>2){
                if (spriteNum==1){spriteNum=2;}
                else if (spriteNum==2){spriteNum=3;}
                else if (spriteNum==3){spriteNum=4;}
                else if (spriteNum==4){spriteNum=5;}
                else if (spriteNum==5){spriteNum=6;}
                else if (spriteNum==6){spriteNum=7;}
                else if (spriteNum==7){spriteNum=1;}
                spriteCounter=0;
            }
        }
    }

    public void render(Graphics2D g2d) {
        //g2d.setColor(Color.GREEN); // Set the color to red for character
        //g2d.fillRect((int) this.posX, (int) this.posY, this.width, this.height); // Draw the character
        BufferedImage image=null;
        switch (direction) {
            case "up":
            if (spriteNum==1) {image=up1;}
            if (spriteNum==2) {image=up2;}
            if (spriteNum==3) {image=up3;}
            if (spriteNum==4) {image=up4;}
            if (spriteNum==5) {image=up5;}
            if (spriteNum==6) {image=up6;}
            if (spriteNum==7) {image=up7;}
            break;
            case "down":
            if (spriteNum==1) {image=down1;}
            if (spriteNum==2) {image=down2;}
            if (spriteNum==3) {image=down3;}
            if (spriteNum==4) {image=down4;}
            if (spriteNum==5) {image=down5;}
            if (spriteNum==6) {image=down6;}
            if (spriteNum==7) {image=down7;}
            break;
            case "right":
            if (spriteNum==1) {image=right1;}
            if (spriteNum==2) {image=right2;}
            if (spriteNum==3) {image=right3;}
            if (spriteNum==4) {image=right4;}
            if (spriteNum==5) {image=right5;}
            if (spriteNum==6) {image=right6;}
            if (spriteNum==7) {image=right7;}
            break;
            case "left":
            if (spriteNum==1) {image=left1;}
            if (spriteNum==2) {image=left2;}
            if (spriteNum==3) {image=left3;}
            if (spriteNum==4) {image=left4;}
            if (spriteNum==5) {image=left5;}
            if (spriteNum==6) {image=left6;}
            if (spriteNum==7) {image=left7;}
            break;
        }
        g2d.drawImage(image, (int) posX, (int) posY, (width/3)*5, (height/3)*5, null);
    }

    public void playerSprite(){
        try {
            up1=ImageIO.read(getClass().getResourceAsStream("/spritesheet/up1.png"));
            up2=ImageIO.read(getClass().getResourceAsStream("/spritesheet/up2.png"));
            up3=ImageIO.read(getClass().getResourceAsStream("/spritesheet/up3.png"));
            up4=ImageIO.read(getClass().getResourceAsStream("/spritesheet/up4.png"));
            up5=ImageIO.read(getClass().getResourceAsStream("/spritesheet/up5.png"));
            up6=ImageIO.read(getClass().getResourceAsStream("/spritesheet/up6.png"));
            up7=ImageIO.read(getClass().getResourceAsStream("/spritesheet/up7.png"));
            down1=ImageIO.read(getClass().getResourceAsStream("/spritesheet/down1.png"));
            down2=ImageIO.read(getClass().getResourceAsStream("/spritesheet/down2.png"));
            down3=ImageIO.read(getClass().getResourceAsStream("/spritesheet/down3.png"));
            down4=ImageIO.read(getClass().getResourceAsStream("/spritesheet/down4.png"));
            down5=ImageIO.read(getClass().getResourceAsStream("/spritesheet/down5.png"));
            down6=ImageIO.read(getClass().getResourceAsStream("/spritesheet/down6.png"));
            down7=ImageIO.read(getClass().getResourceAsStream("/spritesheet/down7.png"));
            left1=ImageIO.read(getClass().getResourceAsStream("/spritesheet/left1.png"));
            left2=ImageIO.read(getClass().getResourceAsStream("/spritesheet/left2.png"));
            left3=ImageIO.read(getClass().getResourceAsStream("/spritesheet/left3.png"));
            left4=ImageIO.read(getClass().getResourceAsStream("/spritesheet/left4.png"));
            left5=ImageIO.read(getClass().getResourceAsStream("/spritesheet/left5.png"));
            left6=ImageIO.read(getClass().getResourceAsStream("/spritesheet/left6.png"));
            left7=ImageIO.read(getClass().getResourceAsStream("/spritesheet/left7.png"));
            right1=ImageIO.read(getClass().getResourceAsStream("/spritesheet/right1.png"));
            right2=ImageIO.read(getClass().getResourceAsStream("/spritesheet/right2.png"));
            right3=ImageIO.read(getClass().getResourceAsStream("/spritesheet/right3.png"));
            right4=ImageIO.read(getClass().getResourceAsStream("/spritesheet/right4.png"));
            right5=ImageIO.read(getClass().getResourceAsStream("/spritesheet/right5.png"));
            right6=ImageIO.read(getClass().getResourceAsStream("/spritesheet/right6.png"));
            right7=ImageIO.read(getClass().getResourceAsStream("/spritesheet/right7.png"));
        } catch (IOException e) {e.printStackTrace();}
    }
}
