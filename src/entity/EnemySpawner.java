package entity;

import main.GamePanel;

public class EnemySpawner {
    private final GamePanel gp;

    public EnemySpawner(GamePanel gp) {
        this.gp = gp;
    }

    public void spawnWorld(int world) {
        clearEnemies();

        switch (world) {
            case 1 -> spawnWorld1();
            case 2 -> spawnWorld2();
            case 3 -> spawnWorld3();
        }
    }

    private void clearEnemies() {
        for (int i = 0; i < gp.enemies.length; i++) {
            gp.enemies[i] = null;
        }
    }

    private void spawnWorld1() {
        gp.enemies[0] = new Goblin(gp, 17 * gp.tileSize, 10 * gp.tileSize);
        gp.enemies[1] = new Goblin(gp, 16 * gp.tileSize, 11 * gp.tileSize);
        gp.enemies[2] = new Goblin(gp, 45 * gp.tileSize, 24 * gp.tileSize);
        gp.enemies[3] = new Goblin(gp, 44 * gp.tileSize, 25 * gp.tileSize);
    }
    private void spawnWorld2() {
        gp.enemies[0] = new Goblin(gp, 11 * gp.tileSize, 22 * gp.tileSize);
        gp.enemies[1] = new Goblin(gp, 20 * gp.tileSize, 20 * gp.tileSize);
        gp.enemies[2] = new General(gp, 20 * gp.tileSize, 24 * gp.tileSize);
    }

    private void spawnWorld3() {
        gp.enemies[0] = new Goblin(gp, 15 * gp.tileSize, 15 * gp.tileSize);
        gp.enemies[1] = new Goblin(gp, 17 * gp.tileSize, 16 * gp.tileSize);
        gp.enemies[4] = new General(gp, 23 * gp.tileSize, 22 * gp.tileSize);
        gp.enemies[5] = new Boss(gp, 25 * gp.tileSize, 24 * gp.tileSize);
    }
}
