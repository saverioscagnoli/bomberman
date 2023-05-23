package loop;

import entities.GameCharacter;
import entities.PowerUp;

import loop.GameLoop;

import java.awt.Color;
import java.awt.Graphics2D;

import util.Consts;
import util.Utils;
/**
 * PowerupManager
 */
public class PowerupManager {

    public static void HandlePowerup(PowerUp p, GameCharacter c){
        switch (p.name) {
            case "speed": {
                Runnable onPickup = () -> {
                    c.speed += 5;
                };
                Runnable onExpire = () -> {
                    c.speed -= 5;
                };
                p.onPickup(5000, onPickup, onExpire);
            }
            break;
        }
    }

    public static void RenderPowerup(PowerUp p, Graphics2D g2d){
        // draw an orange rectangle for the powerup
        g2d.setColor(Color.ORANGE);
        g2d.fillRect((int) p.posX, (int) p.posY, p.width, p.height);
    }

}