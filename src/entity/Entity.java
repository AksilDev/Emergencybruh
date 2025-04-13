package entity;

import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public int worldX, worldY;
    public int speed;
    public String direction = "down";
    public Rectangle solidArea;
    public boolean collisionOn = false;

    public boolean isHurt = false;
    public boolean isDying = false;
    protected int hurtTimer = 0;
    protected int deathTimer = 0;

    protected BufferedImage[][] walkFrames, attackFrames, hurtFrames, deathFrames;
    protected int directionNum = 0;
    protected int walkFrameIndex = 0;
    protected int attackFrameIndex = 0;

    public abstract void update();
    protected abstract GamePanel getGamePanel();

    public void triggerHurt() {
        isHurt = true;
        hurtTimer = 0;
    }

    public void triggerDeath() {
        isDying = true;
        deathTimer = 0;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        GamePanel gp = getGamePanel();
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        try {
            if (isDying && deathFrames != null && deathFrames[directionNum].length > 0) {
                int idx = Math.min(deathTimer / 10, deathFrames[directionNum].length - 1);
                image = deathFrames[directionNum][idx];
            } else if (isHurt && hurtFrames != null && hurtFrames[directionNum].length > 0) {
                int idx = Math.min(hurtTimer / 5, hurtFrames[directionNum].length - 1);
                image = hurtFrames[directionNum][idx];
            } else if (this instanceof Enemy && ((Enemy) this).isAttacking && attackFrames != null) {
                int idx = attackFrameIndex % attackFrames[directionNum].length;
                image = attackFrames[directionNum][idx];
            } else if (walkFrames != null) {
                int idx = walkFrameIndex % walkFrames[directionNum].length;
                image = walkFrames[directionNum][idx];
            }
        } catch (Exception e) {
            System.err.println("⚠️ Image retrieval error at directionNum=" + directionNum);
            e.printStackTrace();
        }

        if (image != null) {
            g2.drawImage(image, screenX, screenY, null);
            if (this instanceof Enemy enemy && enemy.name != null && !enemy.name.isEmpty()) {

                int barX = screenX;
                int barY = screenY - 12;
                int barWidth = 60;
                int barHeight = 6;

                //  HPbackground
                g2.setColor(Color.BLACK);
                g2.fillRect(barX, barY, barWidth, barHeight);

                // current HP
                int hpWidth = (int)(((double) enemy.currentHP / enemy.maxHP) * barWidth);
                g2.setColor(Color.RED);
                g2.fillRect(barX, barY, hpWidth, barHeight);

                // HP bar
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                int textWidth = g2.getFontMetrics().stringWidth(enemy.name);
                int textX = screenX + (image.getWidth() - textWidth) / 2;
                int textY = barY - 6;
                g2.drawString(enemy.name, textX, textY);
            }

        } else {
            System.err.println("⚠️ Image null for entity at worldX=" + worldX + ", worldY=" + worldY);
        }
    }


}
