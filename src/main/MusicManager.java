package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicManager {
    private static MusicManager instance;
    private Clip clip;
    private String currentlyPlayingPath = "";

    private MusicManager() {}

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void playMusic(String path) {
        if (currentlyPlayingPath.equals(path) && clip != null && clip.isRunning()) {
            return; // Same music already playing, skip
        }

        stop(); // Stop any previous music

        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("⚠Couldnt find music file: " + path);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Adjust volume (set to 40% here)
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.4) / Math.log(10) * 40);  // ~ -12 dB
            gainControl.setValue(volume);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
            currentlyPlayingPath = path;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            clip = null;
            currentlyPlayingPath = "";
        }
    }

    // Aliases for clarity
    public void stopMusic() {
        stop();
    }

    public void playMenuMusic() {
        playMusic("/audio/menu_music.wav");
    }

    public void playVictoryMusic() {
        playMusic("/audio/victory_theme.wav");
    }

    public void playWorldMusic(int world) {
        String path = switch (world) {
            case 1 -> "/audio/f1_music.wav";
            case 2 -> "/audio/f2_music.wav";
            case 3 -> "/audio/f3_music.wav";
            default -> null;
        };

        if (path != null) {
            playMusic(path);
        }
    }
}
