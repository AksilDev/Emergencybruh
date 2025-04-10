package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeveloperPanel extends JPanel {

    private final MainWindow window;
    private final ImageIcon backgroundGif;

    private final JLabel returnLabel;

    public DeveloperPanel(MainWindow window) {
        this.window = window;
        setLayout(null);
        setPreferredSize(new Dimension(1584, 960));

        backgroundGif = new ImageIcon(getClass().getResource("/ui/dev_team.gif"));

        // Return Label
        returnLabel = new JLabel("RETURN");
        returnLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        returnLabel.setForeground(Color.BLACK);
        returnLabel.setBounds(80, 865, 200, 40);
        returnLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(returnLabel);

        returnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                window.showStartMenu();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                returnLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                returnLabel.setForeground(Color.BLACK);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this); // scaled GIF
    }
}
