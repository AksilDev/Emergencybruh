package entity;

import main.GamePanel;

import java.awt.image.BufferedImage;

public class General extends Enemy {

    public General(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;

        maxHP = 6;
        currentHP = maxHP;
        attackDamage = 4;
        speed = 1;

        direction = "down";
        directionNum = 0;

        solidArea.setBounds(8, 16, 60, 60);

        loadSprites("/enemies/general_full.png", "/enemies/general_attack.png");
        loadHurtFrames("/enemies/general_hurt.png", 4, 4);   // 256x256
        loadDeathFrames("/enemies/general_death.png", 6, 4); // 384x256

    }

    @Override
    protected void loadSprites(String walkPath, String attackPath) {
        walkFrames = loadSpriteSheet(walkPath, gp.tileSize * 2 + 12, 8, 4);   // 512x256 = 8 cols
        attackFrames = loadSpriteSheet(attackPath, gp.tileSize * 2 + 12, 9, 4); // 576x256 = 9 cols
    }

    @Override
    public int getDamage() {
        return attackDamage;
    }

    @Override protected int getWalkFrameLength()     { return 8; }
    @Override protected int getAttackFrameLength()   { return 9; }
    @Override protected int getAttackFrameToHit()    { return 4; }
    @Override protected int getAttackSpeed()         { return 15; }
}
