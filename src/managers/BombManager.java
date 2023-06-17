package managers;

import java.util.ArrayList;
import entities.Bomb;
import entities.Bomberman;
import entities.Explosion;
import ui.SpriteAnimation;
import util.Consts;
import util.Utils;
import java.awt.Graphics2D;

public class BombManager {
    private static BombManager instance = null;
    public ArrayList<Bomb> bombs;
    public ArrayList<Explosion> explosions;

    private BombManager() {
        this.bombs = new ArrayList<>();
        this.explosions = new ArrayList<>();
    }

    public static synchronized BombManager getInstance() {
        if (instance == null) {
            instance = new BombManager();
        }
        return instance;
    }

    public void addBomb(Bomberman character) {
        int x = (int) (character.posX + character.width * 0.5);
        int y = (int) (character.posY + character.height * 0.5);
        int[] pos = Utils.normalizePos(x, y);
        Bomb b = new Bomb(pos[0], pos[1], Consts.tileDims, Consts.tileDims, 0, character.bombRadius);
        b.setScale(2.5f);
        b.addAnimation("bomb", new SpriteAnimation(b.spritesheet, 1, 4, b.scale, 0, 4, 10));
        this.bombs.add(b);
    }

    public void addExplosion(Explosion ex) {
        this.explosions.add(ex);
    }

    private void updateExplosions() {
        ArrayList<Explosion> toRemove = new ArrayList<>();
        int l = this.explosions.size();
        for (int i = 0; i < l; i++) {
            Explosion ex = this.explosions.get(i);
            if (ex.dead) {
                toRemove.add(ex);
            } else {
                ex.update();
            }
        }
        toRemove.forEach((ex) -> this.explosions.remove(ex));
    }

    public void updateBombs() {
        ArrayList<Bomb> toRemove = new ArrayList<>();
        int l = this.bombs.size();
        for (int i = 0; i < l; i++) {
            Bomb b = bombs.get(i);
            if (b.dead) {
                toRemove.add(b);
            } else {
                b.update();
            }
        }
        toRemove.forEach((b) -> this.bombs.remove(b));
        this.updateExplosions();
    }

    public void drawBombs(Graphics2D g2d) {
        int l = this.bombs.size();
        for (int i = 0; i < l; i++) {
            Bomb b = bombs.get(i);
            b.render(g2d);
        }

        int l2 = this.explosions.size();
        for (int i = 0; i < l2; i++) {
            Explosion ex = this.explosions.get(i);
            ex.render(g2d);
        }
    }
}
