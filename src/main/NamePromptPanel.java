package main;

import run.RunManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NamePromptPanel extends JPanel {
    private final MainWindow window;

    public NamePromptPanel(MainWindow window) {
        this.window = window;
        setLayout(null);
        setPreferredSize(new Dimension(1584, 960));
        setBackground(Color.BLACK);

        JLabel label = new JLabel("Type thy name, Brave Adventurer!");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Georgia", Font.BOLD, 36));
        label.setBounds(500, 300, 1000, 60);
        add(label);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Georgia", Font.PLAIN, 28));
        nameField.setBounds(550, 400, 450, 50);
        add(nameField);

        JButton startButton = new JButton("BEGIN");
        startButton.setFont(new Font("Georgia", Font.BOLD, 28));
        startButton.setBounds(600, 480, 300, 60);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    RunManager.getInstance().setPlayerName(name);
                    window.startGame();  // this transitions to GamePanel
                } else {
                    JOptionPane.showMessageDialog(NamePromptPanel.this, "E enter imong ngan palihug", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add(startButton);
    }
}
