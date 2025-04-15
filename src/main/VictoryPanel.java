package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VictoryPanel extends JPanel {
    private final MainWindow window;
    private final ImageIcon victoryGif;

    public VictoryPanel(MainWindow window) {
        this.window = window;
        setLayout(null);
        setPreferredSize(new Dimension(1584, 960));
        setBackground(Color.BLACK);

        MusicManager.getInstance().stop(); // Clear lingering music
        MusicManager.getInstance().playVictoryMusic();

        JLabel redoBtn = new JLabel("REDO");
        redoBtn.setFont(new Font("Georgia", Font.BOLD, 32));
        redoBtn.setForeground(Color.WHITE);
        redoBtn.setBounds(740, 690, 150, 40);
        redoBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        redoBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MusicManager.getInstance().stop();
                window.showStartMenu();
            }
        });
        add(redoBtn);

        JLabel exitBtn = new JLabel("EXIT");
        exitBtn.setFont(new Font("Georgia", Font.BOLD, 32));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBounds(745, 805, 150, 40);
        exitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MusicManager.getInstance().stop();
                System.exit(0);
            }
        });
        add(exitBtn);

        victoryGif = new ImageIcon(getClass().getResource("/ui/victory.gif"));
        JLabel gifLabel = new JLabel(victoryGif);
        gifLabel.setBounds(0, 0, 1584, 960);
        add(gifLabel);
        setComponentZOrder(gifLabel, getComponentCount() - 1); // send to back
    }
}
