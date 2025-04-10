package entity;

import main.GamePanel;

public class General extends Enemy {
public int scale = 3;
    public General(GamePanel gp, int x, int y) {
        super(gp);
        this.worldX = x;
        this.worldY = y;
        maxHP = 6;
        currentHP = maxHP;
        attackDamage = 2;
        speed = 1;

        direction = "down";
        directionNum = 0;

        solidArea.setBounds(8, 16, 60, 60);

        loadSprites("/enemies/general_full.png", "/enemies/general_attack.png");
        hurtFrames = loadSpriteSheet("/enemies/general_hurt.png", gp.tileSize * scale, 4, 4);   // 256x256
        deathFrames = loadSpriteSheet("/enemies/general_death.png",gp.tileSize * scale, 6, 4); // 384x256

    }

    @Override
    protected void loadSprites(String walkPath, String attackPath) {
        walkFrames = loadSpriteSheet(walkPath, gp.tileSize * scale, 8, 4);   // 512x256 = 8 cols
        attackFrames = loadSpriteSheet(attackPath, gp.tileSize * scale, 9, 4); // 576x256 = 9 cols
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
