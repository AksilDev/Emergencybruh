package main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicManager {
    private static MusicManager instance;
    private Clip clip;

    private MusicManager() {}

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void playMusic(String path) {
        stop(); // Stop any currently playing music

        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("❌ Could not find audio file: " + path);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Set volume (adjust as needed)
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.4) / Math.log(10) * 40);
            gainControl.setValue(volume);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    // ✅ Universal method name to avoid confusion
    public void stopMusic() {
        stop(); // internally uses the same logic
    }

    // ✅ Menu convenience methods
    public void playMenuMusic() {
        playMusic("/audio/menu_music.wav");
    }

    public void stopMenuMusic() {
        stop();
    }

    public void playVictoryMusic() {
        playMusic("/audio/victory_theme.wav");
    }
}
