package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;

public class General extends Enemy {

    public General(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;


        maxHP = 6;
        currentHP = 6;
        attackDamage = 4;
        speed = 1;


        direction = "down";
        directionNum = 0;

        solidArea = new Rectangle(8, 16, 60, 60);
        loadSprites("/enemies/general_full.png", "/enemies/general_attack.png");
        loadHurtFrames("/enemies/general_hurt.png");
        loadDeathFrames("/enemies/general_death.png");
    }

    private void loadDeathFrames(String s) {
    }

    protected void loadHurtFrames(String path) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream(path));
            UtilityTool u = new UtilityTool();

            int rows = 4;
            int cols = 4; // ✅ match your general_hurt.png
            int frameW = sheet.getWidth() / cols;
            int frameH = sheet.getHeight() / rows;

            hurtFrames = new BufferedImage[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    BufferedImage sub = sheet.getSubimage(c * frameW, r * frameH, frameW, frameH);
                    hurtFrames[r][c] = u.scaleImage(sub, gp.tileSize * 2, gp.tileSize * 2);
                }
            }

        } catch (Exception e) {
            System.err.println("⚠️ Failed to load hurtFrames: " + path);
            e.printStackTrace();
        }
    }


    @Override protected void loadSprites(String walkPath, String attackPath) {
        walkFrames = loadSpriteSheet(walkPath, gp.tileSize * 2 + 12);
        attackFrames = loadSpriteSheet(attackPath, gp.tileSize * 2 + 12);
    }

    public BufferedImage[][] loadSpriteSheet(String walkPath, int i) {
        return new BufferedImage[0][];
    }
    @Override
    public int getDamage() {
        return attackDamage;
    }


    @Override protected int getWalkFrameLength() { return 8; }
    @Override protected int getAttackFrameLength() { return 9; }
    @Override protected int getAttackFrameToHit() { return 4; }
    @Override protected int getAttackSpeed() { return 15; }
}
