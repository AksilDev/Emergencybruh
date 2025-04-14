package entity;

import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity {
    protected GamePanel gp;
    public int maxHP, currentHP, attackDamage, speed;
    public boolean alive = true;

    protected int walkCounter = 0, attackTimer = 0;
    protected boolean isAttacking = false, alreadyHitPlayer = false;
    protected int actionLockCounter = 0;

    public String name = "";
    private final int AGGRO_RANGE = 200;
    private final int ATTACK_RANGE = 40;

    public Enemy(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(8, 16, 60, 60);
    }

    @Override
    protected GamePanel getGamePanel() {
        return gp;
    }

    public void update() {
        if (!alive) return;

        if (isDying) {
            deathTimer++;
            if (deathTimer > 60) alive = false;
            return;
        }

        if (isHurt) {
            hurtTimer++;
            if (hurtTimer > 30) isHurt = false;
            return;
        }

        if (isAttacking) {
            handleAttack();
            return;
        }

        if (isPlayerNearby()) {
            followPlayer();
            if (isPlayerInAttackRange()) startAttack();
        } else {
            handleRandomMovement();
        }

        handleWalkingAnimation();
    }

    private boolean isPlayerNearby() {
        int dx = Math.abs(worldX - gp.player.worldX);
        int dy = Math.abs(worldY - gp.player.worldY);
        return dx < AGGRO_RANGE && dy < AGGRO_RANGE;
    }

    private boolean isPlayerInAttackRange() {
        int dx = Math.abs(worldX - gp.player.worldX);
        int dy = Math.abs(worldY - gp.player.worldY);
        return dx < ATTACK_RANGE && dy < ATTACK_RANGE;
    }

    private void followPlayer() {
        int dx = gp.player.worldX - worldX;
        int dy = gp.player.worldY - worldY;


        if (Math.abs(dx) > Math.abs(dy)) {
            direction = dx > 0 ? "right" : "left";
            directionNum = dx > 0 ? 3 : 2;
        } else {
            direction = dy > 0 ? "down" : "up";
            directionNum = dy > 0 ? 0 : 1;
        }


        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (!collisionOn) {
            moveInDirection();
        } else {

            String[] fallbackDirections = {"up", "down", "left", "right"};

            for (String fallback : fallbackDirections) {
                if (fallback.equals(direction)) continue; // skip current dir

                direction = fallback;
                directionNum = switch (fallback) {
                    case "down" -> 0;
                    case "up" -> 1;
                    case "left" -> 2;
                    case "right" -> 3;
                    default -> directionNum;
                };

                collisionOn = false;
                gp.cChecker.checkTile(this);

                if (!collisionOn) {
                    moveInDirection();
                    break;
                }
            }
        }
    }

    private void moveInDirection() {
        switch (direction) {
            case "up" -> worldY -= speed;
            case "down" -> worldY += speed;
            case "left" -> worldX -= speed;
            case "right" -> worldX += speed;
        }
    }


    protected void handleAttack() {
        attackTimer++;
        if (attackFrameIndex == getAttackFrameToHit() && !alreadyHitPlayer) {
            checkHitPlayer();
            alreadyHitPlayer = true;
        }

        if (attackTimer > getAttackSpeed()) {
            attackFrameIndex = (attackFrameIndex + 1) % getAttackFrameLength();
            attackTimer = 0;

            if (attackFrameIndex == 0) {
                isAttacking = false;
                alreadyHitPlayer = false;
            }
        }
    }

    protected void checkHitPlayer() {
        Rectangle slashArea = new Rectangle(worldX, worldY, 50, 50);
        if (direction.equals("down")) slashArea.y += solidArea.height;
        if (direction.equals("up")) slashArea.y -= 50;
        if (direction.equals("left")) slashArea.x -= 50;
        if (direction.equals("right")) slashArea.x += solidArea.width;

        Rectangle playerArea = new Rectangle(
                gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );

        if (slashArea.intersects(playerArea)) gp.player.takeDamage(attackDamage);
    }

    public void takeDamage(int damage) {
        currentHP -= damage;
        triggerHurt();
        if (currentHP <= 0) {
            currentHP = 0;
            alive = false;
            triggerDeath();
        }
    }

    protected void startAttack() {
        isAttacking = true;
        attackFrameIndex = 0;
        attackTimer = 0;
    }

    protected void handleRandomMovement() {
        actionLockCounter++;
        if (actionLockCounter > 120) {
            actionLockCounter = 0;
            int r = (int) (Math.random() * 4);
            directionNum = r;
            direction = switch (r) {
                case 0 -> "down";
                case 1 -> "up";
                case 2 -> "left";
                case 3 -> "right";
                default -> direction;
            };
        }

        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (!collisionOn) {
            switch (direction) {
                case "down" -> worldY += speed;
                case "up" -> worldY -= speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }
    }

    protected void handleWalkingAnimation() {
        walkCounter++;
        if (walkCounter > 15) {
            walkFrameIndex = (walkFrameIndex + 1) % getWalkFrameLength();
            walkCounter = 0;
        }
    }

    protected abstract void loadSprites(String walkPath, String attackPath);
    protected abstract int getWalkFrameLength();
    protected abstract int getAttackFrameLength();
    protected abstract int getAttackFrameToHit();
    protected abstract int getAttackSpeed();
}
