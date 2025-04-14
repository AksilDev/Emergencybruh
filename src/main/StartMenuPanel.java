package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartMenuPanel extends JPanel {
    private final MainWindow window;
    private final ImageIcon backgroundGif;

    public StartMenuPanel(MainWindow window) {
        this.window = window;
        setPreferredSize(new Dimension(1584, 960));
        setLayout(null);
        setDoubleBuffered(true);
        backgroundGif = new ImageIcon(getClass().getResource("/ui/menu_background.gif"));

        addMenuLabel("PLAY", 690, 605, () -> {
            MusicManager.getInstance().stop();
            window.showNamePromptPanel();
        });
        addMenuLabel("INFO", 690, 840, window::showDeveloperPanel);
        addMenuLabel("STATS", 120, 840, window::showStatsScreenPanel);
        addMenuLabel("EXIT", 690, 740, () -> System.exit(0));

        MusicManager.getInstance().playMenuMusic();
    }

    private void addMenuLabel(String text, int x, int y, Runnable action) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Georgia", Font.BOLD, 35));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(x, y, 200, 40);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.BLACK);
            }
        });

        add(label);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}
