package managers;

import javax.sound.sampled.Clip;
import util.Consts;
import util.GameState;
import util.Utils;

/* This class manages all the sounds */

public class SoundManager {
    private static SoundManager instance = null;
    private Clip clip;

    private SoundManager() {
        this.clip = Utils.playSound(Consts.soundPath + "title.wav");
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static SoundManager build() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /* Play a sound based on the gamestate */
    public void ost(GameState gameState) {
        this.clip.stop();
        switch (gameState) {
            case Menu: {
                this.clip = Utils.playSound(Consts.soundPath + "title.wav");
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                break;
            }
            case Stats: {
                this.clip = Utils.playSound(Consts.soundPath + "stats.wav");
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                break;
            }
            case InGame: {
                this.clip = Utils.playSound(Consts.soundPath + "level-1.wav");
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                break;
            }
            case Pause: {
                this.clip = Utils.playSound(Consts.soundPath + "pause.wav");
                break;
            }
            default: {
                break;
            }
        }
    }
}
