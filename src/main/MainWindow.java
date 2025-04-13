package main;

import javax.swing.*;

public class MainWindow extends JFrame {

    private StartMenuPanel startMenuPanel;
    private NamePromptPanel namePromptPanel;
    private GamePanel gamePanel;

    public MainWindow() {
        namePromptPanel = new NamePromptPanel(this);

        setTitle("Monarch's Descent");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        showStartMenu();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showStartMenu() {
        MusicManager.getInstance().stopMenuMusic();  // 🔄 updated
        startMenuPanel = new StartMenuPanel(this);
        setContentPane(startMenuPanel);
        revalidate();
        repaint();
    }

    public void showDeveloperPanel() {
        MusicManager.getInstance().stopMenuMusic();  // 🔄 updated
        setContentPane(new DeveloperPanel(this));
        revalidate();
        repaint();
    }

    public void showNamePromptPanel() {
        setContentPane(namePromptPanel);
        revalidate();
        repaint();
    }

    public void showStatsScreenPanel() {
        MusicManager.getInstance().stopMenuMusic();  // 🔄 updated
        setContentPane(new StatsScreenPanel(this));
        revalidate();
        repaint();
    }

    public void startGame() {
        MusicManager.getInstance().stopMenuMusic();  // 🔄 updated

        gamePanel = new GamePanel();
        gamePanel.setWindow(this);

        setContentPane(gamePanel);
        revalidate();
        repaint();

        gamePanel.requestFocusInWindow();
        gamePanel.startGameThread();
    }

    public void showVictoryPanel() {
        stopGameMusic(); // just to be safe
        setContentPane(new VictoryPanel(this));
        revalidate();
        repaint();
    }

    public void stopGameMusic() {
        if (gamePanel != null) {
            gamePanel.stopWorldMusic();
        }
    }
}
