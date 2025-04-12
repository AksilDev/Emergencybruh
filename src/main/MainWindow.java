package main;

import javax.swing.*;

public class MainWindow extends JFrame {
    private StartMenuPanel startMenuPanel;
    private NamePromptPanel namePromptPanel;

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


    public void showNamePromptPanel() {
        setContentPane(namePromptPanel);
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
    public void showNamePrompt() {
        setContentPane(namePromptPanel);
        revalidate();
        repaint();
    }


}
