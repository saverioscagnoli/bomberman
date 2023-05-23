package loop;

import entities.GameCharacter;
import entities.PowerUp;

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
        }
    }
}