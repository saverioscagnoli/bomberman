package entities;

import java.awt.Graphics2D;

import loop.GameLoop;
import util.Consts;
import util.Utils;
import javax.imageio.ImageIO;
import java.io.IOException;
import entities.*;

public class Bomb extends Entity {

    public Bomb(float posX, float posY, int width, int height, int speed,int bombRadius) {
        super(posX, posY, width, height, speed);
        this.isSolid = false;
        try {
            bombSprite = ImageIO.read(getClass().getResourceAsStream("/spritesheet/bombs.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.setTimeout(() -> this.explode(bombRadius), 3000);

    }

    @Override
    public void update() {
    }

    private Explosion explosionMatrix[][];

    private void explode(int bombRadius) {

        explosionMatrix = new Explosion[4][5];  // creating an array that can store up to 5 explosions in the 4 directions

        
        for(int rad = 1; rad<bombRadius+1;rad++){ // for the length of the bomb radius
            explosionMatrix[0][rad] = new Explosion((int) this.posX - Consts.tileDims*rad, (int) this.posY);
            explosionMatrix[1][rad] = new Explosion((int) this.posX, (int) this.posY - Consts.tileDims*rad);
            explosionMatrix[2][rad] = new Explosion((int) this.posX + Consts.tileDims*rad, (int) this.posY);
            explosionMatrix[3][rad] = new Explosion((int) this.posX, (int) this.posY + Consts.tileDims*rad);
        }

        // adds explosions in explosion matrix to entities
        for(int i = 0; i<4; i++){
            boolean hitWall = false;
            for(int j = 0; j<bombRadius+1; j++){
                if(explosionMatrix[i][j] != null){
                    if(!this.checkSolid((int) explosionMatrix[i][j].posX, (int) explosionMatrix[i][j].posY) && !hitWall){
                        GameLoop.entities.add(explosionMatrix[i][j]);
                    }else{
                        hitWall = true;
								break;
                }
            }
        }
        this.die();}
    }

    private boolean checkSolid(int posX, int posY) {
        for (int i = 0; i < GameLoop.entities.size(); i++) { //for every entity in the list
            Entity e = GameLoop.entities.get(i);  // get the entity
            if (e.posX == posX && e.posY == posY) { // if the entity is in the same position as the explosion
                if (e.isSolid) { // if the entity is solid
                    Obstacle wall = (Obstacle) e; // cast the entity to an obstacle
                    if (wall.destructable) { // if the obstacle is destructable
                        Utils.setTimeout(() -> wall.die(), 100); // destroy the obstacle
                    }
                }
                return true;
            }
            
            // check if the entity is an enemy. if it is, and the enemy and explosion overlap, kill it.
            if (e instanceof Enemy) {
                Enemy enemy = (Enemy) e;

                // if the enemy and the explosion have aabb collision, damage it.
                if (enemy.posX < posX + Consts.tileDims && enemy.posX + enemy.width > posX && enemy.posY < posY + Consts.tileDims && enemy.posY + enemy.height > posY) {
                    enemy.dealDamage(1);
                }

            }

            if (e instanceof GameCharacter){
                // System.out.println("player");
                GameCharacter player = (GameCharacter) e;
                if (player.posX < posX + Consts.tileDims && player.posX + player.width > posX && player.posY < posY + Consts.tileDims && player.posY + player.height > posY) {
                    player.dealDamage(1);
                }
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
