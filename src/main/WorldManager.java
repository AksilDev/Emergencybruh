package main;

import entity.Boss;
import entity.General;
import entity.Goblin;
import object.OBJ_Key;
import object.OBJ_Portal;

public class WorldManager {

    private final GamePanel gp;

    public WorldManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setupWorldContent() {
        System.out.println("🗺 Loading World: " + gp.currentWorld);

        gp.tileM.loadMap("/maps/world0" + gp.currentWorld + ".txt");
        gp.enemySpawner.spawnWorld(gp.currentWorld);
        gp.objectManager.clearObjects();
        gp.player.setDefaultValues();
        gp.hasKey = false;

        switch (gp.currentWorld) {
            case 1 -> setupWorld1();
            case 2 -> setupWorld2();
            case 3 -> setupWorld3();
        }
    }

    private void setupWorld1() {
        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 46 * gp.tileSize;
        gp.obj[1].worldY = 24 * gp.tileSize;

        gp.obj[2] = new OBJ_Portal(gp);
        gp.obj[2].worldX = 47 * gp.tileSize;
        gp.obj[2].worldY = 24 * gp.tileSize;
    }

    private void setupWorld2() {
        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 47 * gp.tileSize;
        gp.obj[1].worldY = 24 * gp.tileSize;

        gp.obj[2] = new OBJ_Portal(gp);
        gp.obj[2].worldX = 49 * gp.tileSize;
        gp.obj[2].worldY = 24 * gp.tileSize;
    }

    private void setupWorld3() {
        gp.obj[1] = new OBJ_Key(gp);
        gp.obj[1].worldX = 40 * gp.tileSize;
        gp.obj[1].worldY = 26 * gp.tileSize;

        gp.obj[2] = new OBJ_Portal(gp);
        gp.obj[2].worldX = 41 * gp.tileSize;
        gp.obj[2].worldY = 26 * gp.tileSize;
    }

    public void loadWorld(int worldNum) {
        gp.currentWorld = worldNum;
        setupWorldContent(); // Reuse your current method to set up tiles, enemies, objects, etc.
        gp.playWorldMusic(gp.currentWorld); // Add this after setting currentWorld


    }

}
