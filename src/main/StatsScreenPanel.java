package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class StatsScreenPanel extends JPanel {
    private final MainWindow window;
    private final Image background;
    private final java.util.List<String> statsList = new ArrayList<>();

    public StatsScreenPanel(MainWindow window) {
        this.window = window;
        setLayout(null);
        setPreferredSize(new Dimension(1584, 960));
        background = new ImageIcon(getClass().getResource("/ui/Stats.gif")).getImage();

        loadStats();
        setupReturnLabel();
    }

    private void loadStats() {
        try (BufferedReader reader = new BufferedReader(new FileReader("game_stats.txt"))) {
            String line;
            while ((line = reader.readLine()) != null && statsList.size() < 5) {
                statsList.add(line);
            }
        } catch (Exception e) {
            statsList.add("No stats available.");
        }
    }
    private void setupReturnLabel() {
        JLabel returnLabel = new JLabel("RETURN");
        returnLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        returnLabel.setForeground(Color.WHITE);
        returnLabel.setBounds(100, 830, 200, 40);
        returnLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                window.showStartMenu();
            }
        });
        this.add(returnLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g.setFont(new Font("Georgia", Font.BOLD, 28));
        g.setColor(Color.WHITE);

        int x = 690;
        int y = 330;
        g.drawString("Top 5 Runs:", x, y);
        y += 40;
        for (String stat : statsList) {
            g.drawString(stat, x, y);
            y += 35;
        }
    }
}
