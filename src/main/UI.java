package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
public class UI{
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
        int startX = 20;
        int startY = gp.screenHeight - 40;

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Georgia", Font.BOLD, 20));


        String basicStatus = gp.player.attackHandler.isBasicReady()
                ? "Ready"
                : String.format("%.1fs", gp.player.attackHandler.getBasicCooldownRemaining());

        g2.drawString("[J] Basic: " + basicStatus, startX, startY);

//
        String specialStatus = gp.player.attackHandler.isSpecialReady()
                ? "Ready"
                : String.format("%.1fs", gp.player.attackHandler.cooldownRemaining(
                gp.player.attackHandler.specialCooldownStart, 4000));

        g2.drawString("[K] Special: " + specialStatus, startX + 200, startY);

//
        String ultStatus = gp.player.attackHandler.isUltimateReady()
                ? "Ready"
                : String.format("%.1fs", gp.player.attackHandler.cooldownRemaining(
                gp.player.attackHandler.ultimateCooldownStart, 8000));

        g2.drawString("[L] Ult: " + ultStatus, startX + 450, startY);

        drawOutlinedText(g2, basicStatus, startX, startY, Color.WHITE, Color.BLACK);
        drawOutlinedText(g2, specialStatus, startX + 200, startY, Color.WHITE, Color.BLACK);
        drawOutlinedText(g2, ultStatus, startX + 400, startY, Color.WHITE, Color.BLACK);
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

//    public void drawSkillCooldowns(Graphics2D g2) {
//        g2.setFont(new Font("Arial", Font.PLAIN, 20));
//        g2.setColor(Color.WHITE);
//        int x = 20;
//        int y = gp.screenHeight - 40;
//
//        float specialCD = gp.player.attackHandler.cooldownRemaining(gp.player.attackHandler.specialCooldownStart, 4000);
//        float ultCD = gp.player.attackHandler.cooldownRemaining(gp.player.attackHandler.ultimateCooldownStart, 8000);
//
//        String j = "[J] Basic";
//        String k = "[K] Special: " + (specialCD <= 0 ? "Ready" : String.format("%.1fs", specialCD));
//        String l = "[L] Ult: " + (ultCD <= 0 ? "Ready" : String.format("%.1fs", ultCD));
//
//        g2.drawString(j, x, y);
//        g2.drawString(k, x + 140, y);
//        g2.drawString(l, x + 360, y);
//    }

}