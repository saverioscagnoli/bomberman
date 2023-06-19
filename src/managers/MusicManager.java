package managers;

import javax.sound.sampled.Clip;
import util.Consts;
import util.Utils;

public class MusicManager {
    private static MusicManager instance = null;
    private Clip clip;

    private MusicManager() {
        this.clip = Utils.playSound("assets/title.wav");
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void ost(int gameState) {
        this.clip.stop();
        switch (gameState) {
            case Consts.MENU: {
                this.clip = Utils.playSound("assets/title.wav");
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                break;
            }
            case Consts.IN_GAME: {
                this.clip = Utils.playSound("assets/level-1.wav");
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                break;
            }
        }
    }
}
