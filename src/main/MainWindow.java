package main;

import javax.swing.*;

public class MainWindow extends JFrame {

    private StartMenuPanel startMenuPanel;
    private DeveloperPanel developerPanel;
    private GamePanel gamePanel;

    public MainWindow() {
        setTitle("Monarch's Descent");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        startMenuPanel = new StartMenuPanel(this);
        setContentPane(startMenuPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void startGame() {
        if (startMenuPanel != null) {
            startMenuPanel.stopMenuMusic();
        }

        gamePanel = new GamePanel();
        setContentPane(gamePanel);
        revalidate();
        repaint();
        requestFocusInWindow();


        SwingUtilities.invokeLater(() -> {
            gamePanel.requestFocusInWindow();
            gamePanel.startGameThread();
        });
    }


    public void showDeveloperPanel() {
        if (developerPanel == null) {
            developerPanel = new DeveloperPanel(this);
        }

        setContentPane(developerPanel);
        revalidate();
        repaint();
        requestFocusInWindow();
    }

    public void showStartMenu() {
        if (startMenuPanel == null) {
            startMenuPanel = new StartMenuPanel(this);
        }

        setContentPane(startMenuPanel);
        revalidate();
        repaint();
        requestFocusInWindow();
    }
}
