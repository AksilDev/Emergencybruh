package main;

import run.RunManager;
import run.RunRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StatsScreenPanel extends JPanel {
    private final MainWindow window;
    private final Image background;
    private final Image medal1, medal2, medal3;
    private final Image heartFull, heartHalf;

    public StatsScreenPanel(MainWindow window) {
        this.window = window;
        setLayout(null);
        setPreferredSize(new Dimension(1584, 960));

        // Play background music
        MusicManager.getInstance().playMenuMusic();

        background = new ImageIcon(getClass().getResource("/ui/Stats.gif")).getImage();
        medal1 = new ImageIcon(getClass().getResource("/ui/medal_1st.png")).getImage();
        medal2 = new ImageIcon(getClass().getResource("/ui/medal_2nd.png")).getImage();
        medal3 = new ImageIcon(getClass().getResource("/ui/medal_3rd.png")).getImage();
        heartFull = new ImageIcon(getClass().getResource("/objects/full.png")).getImage();
        heartHalf = new ImageIcon(getClass().getResource("/objects/half.png")).getImage();

        setupReturnLabel();
    }

    private void setupReturnLabel() {
        JLabel returnLabel = new JLabel("RETURN");
        returnLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        returnLabel.setForeground(Color.WHITE);
        returnLabel.setBounds(140, 840, 200, 40);
        returnLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MusicManager.getInstance().stopMusic();
                window.showStartMenu();
            }
        });
        this.add(returnLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g2.setFont(new Font("Georgia", Font.BOLD, 32));
        g2.setColor(Color.WHITE);

        int y = 370;
        List<RunRecord> topRuns = RunManager.getInstance().getTopRuns();
        for (int i = 0; i < topRuns.size(); i++) {
            RunRecord record = topRuns.get(i);
            int x = 600;

            if (i == 0) g2.drawImage(medal1, x - 60, y - 30, 40, 40, this);
            else if (i == 1) g2.drawImage(medal2, x - 60, y - 30, 40, 40, this);
            else if (i == 2) g2.drawImage(medal3, x - 60, y - 30, 40, 40, this);

            String line = String.format("#%d. %s - %.2f sec", i + 1, record.getName(), record.getTime());
            g2.drawString(line, x, y);

            int hp = record.getHp();
            int fullHearts = hp / 2;
            boolean hasHalfHeart = hp % 2 == 1;

            int heartX = x + 500;
            for (int j = 0; j < fullHearts; j++) {
                g2.drawImage(heartFull, heartX, y - 25, 25, 25, this);
                heartX += 30;
            }
            if (hasHalfHeart) {
                g2.drawImage(heartHalf, heartX, y - 25, 25, 25, this);
            }

            y += 60;
        }
    }
}
