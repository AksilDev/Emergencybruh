package main;

import javax.swing.*;

public class MainWindow extends JFrame {
    private StartMenuPanel startMenuPanel;

    public MainWindow() {
        setTitle("Monarch's Descent");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        showStartMenu();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showStartMenu() {
        if (startMenuPanel != null) {
            startMenuPanel.stopMenuMusic();
        }
        startMenuPanel = new StartMenuPanel(this);
        setContentPane(startMenuPanel);
        revalidate();
        repaint();
    }

    public void showDeveloperPanel() {
        if (startMenuPanel != null) {
            startMenuPanel.stopMenuMusic();
        }
        setContentPane(new DeveloperPanel(this));
        revalidate();
        repaint();
    }

    public void showStatsScreenPanel() {
        if (startMenuPanel != null) {
            startMenuPanel.stopMenuMusic();
        }
        setContentPane(new StatsScreenPanel(this));
        revalidate();
        repaint();
    }
    public void startGame() {
        if (startMenuPanel != null) {
            startMenuPanel.stopMenuMusic();
        }
        GamePanel gamePanel = new GamePanel();
        setContentPane(gamePanel);
        revalidate();
        repaint();

        gamePanel.requestFocusInWindow();
        gamePanel.startGameThread();
    }
}
