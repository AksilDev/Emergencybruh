package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {
    GamePanel gp;

    private BufferedImage heart_full;
    private BufferedImage heart_half;
    private BufferedImage heart_blank;

    public UI(GamePanel gp) {
        this.gp = gp;
        loadHUDGraphics();
    }

    public void loadHUDGraphics() {
        try {
            heart_full = ImageIO.read(getClass().getResourceAsStream("/objects/full.png"));
            heart_half = ImageIO.read(getClass().getResourceAsStream("/objects/half.png"));
            heart_blank = ImageIO.read(getClass().getResourceAsStream("/objects/blank.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        drawPlayerLife(g2);

        if (gp.gameState == GamePanel.PLAY_STATE) {
            drawSkillCooldowns(g2);
        }

        if (gp.gameState == GamePanel.DEATH_STATE) {
            drawDeathScreen(g2);
        }
        if (gp.gameState == GamePanel.DIALOGUE_STATE) {
            drawDialogueBox(g2, gp.dialogueManager.getCurrentLine());
        }

    }

    private void drawPlayerLife(Graphics2D g2) {
        int x = 20;
        int y = 20;

        int hp = gp.player.currentHP;
        int max = gp.player.maxHP;

        int totalHearts = max / 2;

        for (int i = 0; i < totalHearts; i++) {
            if (hp >= 2) {
                g2.drawImage(heart_full, x, y, null);
                hp -= 2;
            } else if (hp == 1) {
                g2.drawImage(heart_half, x, y, null);
                hp--;
            } else {
                g2.drawImage(heart_blank, x, y, null);
            }
            x += 40;
        }
    }

    public void drawDeathScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        g2.setColor(Color.RED);
        String text = "YOU DIED";

        int textWidth = g2.getFontMetrics().stringWidth(text);
        int x = gp.screenWidth / 2 - textWidth / 2;
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public void drawSkillCooldowns(Graphics2D g2) {
        Font font = new Font("Arial", Font.BOLD, 16);
        g2.setFont(font);

        int startX = 20;
        int startY = gp.screenHeight - 40;

        // Basic Attack
        String basicStatus = gp.player.attackHandler.isBasicReady()
                ? "Ready"
                : String.format("%.1fs", gp.player.attackHandler.getBasicCooldownRemaining());
        String basic = "[J] Basic: " + basicStatus;

        // Special
        float specialCD = gp.player.attackHandler.cooldownRemaining(
                gp.player.attackHandler.specialCooldownStart, 4000);
        String special = "[K] Special: " + (specialCD <= 0 ? "Ready" : String.format("%.1fs", specialCD));

        // Ultimate
        float ultCD = gp.player.attackHandler.cooldownRemaining(
                gp.player.attackHandler.ultimateCooldownStart, 8000);
        String ult = "[L] Ult: " + (ultCD <= 0 ? "Ready" : String.format("%.1fs", ultCD));

        drawOutlinedText(g2, basic, startX, startY, Color.WHITE, Color.BLACK);
        drawOutlinedText(g2, special, startX + 200, startY, Color.WHITE, Color.BLACK);
        drawOutlinedText(g2, ult, startX + 400, startY, Color.WHITE, Color.BLACK);
    }

    public void drawOutlinedText(Graphics2D g2, String text, int x, int y, Color textColor, Color outlineColor) {
        g2.setColor(outlineColor);
        g2.drawString(text, x - 1, y);
        g2.drawString(text, x + 1, y);
        g2.drawString(text, x, y - 1);
        g2.drawString(text, x, y + 1);

        g2.setColor(textColor);
        g2.drawString(text, x, y);
    }

    public void drawDialogueBox(Graphics2D g2, String text) {
        int boxWidth = gp.screenWidth - 100;
        int boxHeight = 100;
        int x = 50;
        int y = gp.screenHeight - boxHeight - 50;

        // Background
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(x, y, boxWidth, boxHeight, 15, 15);

        // Border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, boxWidth, boxHeight, 15, 15);

        // Text
        g2.setFont(new Font("Monospaced", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("* " + text, x + 20, y + 40);
    }

}
