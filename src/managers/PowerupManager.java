package managers;

import entities.Bomberman;
import entities.PowerUp;
import loop.GameLoop;

import java.awt.Color;
import java.awt.Graphics2D;
import entities.*;

public class PowerupManager {

    public static void HandlePowerup(PowerUp p, Bomberman c) {
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

            case "bomb": {
                Runnable onPickup = () -> {
                    if (c.bombRadius < 5) {
                        c.bombRadius += 1;
                    } // avoids having the bomb radius going above 5.
                };
                Runnable onExpire = () -> {
                };
                p.onPickup(5000, onPickup, onExpire);
            }
                break;

            case "rain": {
                Runnable onPickup = () -> {
                    for (Entity e : GameLoop.entities) {
                        if (e instanceof Enemy) {
                            Enemy enemy = (Enemy) e;
                            enemy.dealDamage((int) enemy.health / 2);
                            System.out.println(enemy + " health: " + enemy.health);
                        }
                    }
                };
                Runnable onExpire = () -> {
                };
                p.onPickup(5000, onPickup, onExpire);
            }
        }
    }

    public static void RenderPowerup(PowerUp p, Graphics2D g2d) {
        // draw an orange rectangle for the powerup
        g2d.setColor(Color.ORANGE);
        g2d.fillRect((int) p.posX, (int) p.posY, p.width, p.height);
    }

}