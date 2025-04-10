package main;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartMenuPanel extends JPanel {

    private final MainWindow window;
    private final ImageIcon backgroundGif;
    private Clip menuMusic;

    private final JLabel playLabel = new JLabel("PLAY");
    private final JLabel exitLabel = new JLabel("EXIT");
    private final JLabel devLabel = new JLabel("INFO");

    public StartMenuPanel(MainWindow window) {
        this.window = window;

        setLayout(null);
        setPreferredSize(new Dimension(1584, 960));

        backgroundGif = new ImageIcon(getClass().getResource("/ui/menu_background.gif"));

        setupLabel(playLabel, 690, 605);
        setupLabel(exitLabel, 690, 735);
        setupLabel(devLabel, 690, 830);

        addListeners();

        add(playLabel);
        add(exitLabel);
        add(devLabel);

        playMenuMusic();
    }

    private void setupLabel(JLabel label, int x, int y) {
        label.setFont(new Font("Georgia", Font.BOLD, 36));
        label.setForeground(Color.BLACK);
        label.setBounds(x, y, 200, 50);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addListeners() {
        playLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stopMenuMusic();
                window.startGame();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                playLabel.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playLabel.setForeground(Color.BLACK);
            }
        });

        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stopMenuMusic();
                System.exit(0);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                exitLabel.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitLabel.setForeground(Color.BLACK);
            }
        });

        devLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                window.showDeveloperPanel();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                devLabel.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                devLabel.setForeground(Color.BLACK);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), null);
    }

    private void playMenuMusic() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                    getClass().getResource("/audio/menu_music.wav"));
            menuMusic = AudioSystem.getClip();
            menuMusic.open(audioIn);

            FloatControl gainControl = (FloatControl) menuMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.4) / Math.log(10) * 40); // About 20 == -8dB 40 == lower abmbot kapoy math
            gainControl.setValue(volume);

            menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMenuMusic() {
        if (menuMusic != null && menuMusic.isRunning()) {
            menuMusic.stop();
            menuMusic.close();
        }
    }
}
