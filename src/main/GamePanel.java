// ===== Final GamePanel.java =====
package main;

import entity.*;
import object.SuperObject;
import run.RunManager;
import tile.TileManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;


public class GamePanel extends JPanel implements Runnable {
    // === Screen ===
    public final int tileSize = 16 * 3;
    public final int maxScreenCol = 33;
    public final int maxScreenRow = 20;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // === World ===
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public int currentWorld = 1;
    public boolean DEBUG_MODE = true;

    // === Game State ===
    public static final int PLAY_STATE = 0;
    public static final int DEATH_STATE = 1;
    public static final int WIN_STATE = 2;
    public int gameState = PLAY_STATE;

    // === Managers ===
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public WorldManager worldManager = new WorldManager(this);
    public ObjectManager objectManager = new ObjectManager(this);



    // === Game Entities ===
    public Player player = new Player(this, keyH);
    public Enemy[] enemies = new Enemy[10];
    public SuperObject[] obj = new SuperObject[20];

    // === UI ===
    public UI ui = new UI(this);
    public boolean hasKey = false;
    public EnemySpawner enemySpawner;

    Thread gameThread;
    public Clip worldMusic;
    public long startTime = System.currentTimeMillis(); // dri mag sugod ang clock
    public long runStartTime;



    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startTime = System.currentTimeMillis();

        this.enemySpawner = new EnemySpawner(this);
        this.worldManager = new WorldManager(this);
        this.objectManager = new ObjectManager(this);


        worldManager.setupWorldContent();


    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == PLAY_STATE) {
            player.update();
            for (SuperObject o : obj) if (o != null) o.update();
            for (int i = 0; i < enemies.length; i++) {
                if (enemies[i] != null) {
                    enemies[i].update();
                    if (!enemies[i].alive) enemies[i] = null;
                }
            }
            pickUpObject();
        }
    }

    public void pickUpObject() {
        Rectangle playerArea = new Rectangle(
                player.worldX + player.solidArea.x,
                player.worldY + player.solidArea.y,
                player.solidArea.width,
                player.solidArea.height
        );

        for (int i = 0; i < obj.length; i++) {
            SuperObject object = obj[i];
            if (object == null) continue;

            Rectangle objArea = new Rectangle(object.worldX, object.worldY, tileSize, tileSize);

            if (playerArea.intersects(objArea)) {
                switch (object.name) {
                    case "Key" -> {
                        hasKey = true;
                        obj[i] = null;
                    }
                    case "Portal" -> {
                        if (hasKey && areAllEnemiesDead()) {
                            currentWorld++;
                            if (currentWorld > 3) {
                                double totalTimeSec = (System.currentTimeMillis() - startTime) / 1000.0;
                                int hpLeft = player.currentHP;
                                RunManager.getInstance().trySaveRun(totalTimeSec, hpLeft);
                                gameState = WIN_STATE;
                            } else {
                                worldManager.loadWorld(currentWorld);
                            }
                        }
                    }
                    case "Heart" -> obj[i] = null;
                }
            }
        }
    }



    public boolean areAllEnemiesDead() {
        for (Enemy e : enemies) if (e != null && e.alive) return false;
        return true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);
        for (SuperObject o : obj) if (o != null) o.draw(g2, o.worldX - player.worldX + player.screenX, o.worldY - player.worldY + player.screenY, tileSize);
        for (Enemy e : enemies) if (e != null) e.draw(g2);
        player.draw(g2);
        if (gameState == DEATH_STATE) ui.drawDeathScreen(g2);
        else ui.draw(g2);
        if (gameState == WIN_STATE) {
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            g2.setColor(Color.YELLOW);
            String msg = "You Win!";
            int msgWidth = g2.getFontMetrics().stringWidth(msg);
            g2.drawString(msg, screenWidth / 2 - msgWidth / 2, screenHeight / 2);
        }
        g2.dispose();
    }

    public void playWorldMusic(int world) {
        stopWorldMusic();
        String path = switch (world) {
            case 1 -> "/audio/f1_music.wav";
            case 2 -> "/audio/f2_music.wav";
            case 3 -> "/audio/f3_music.wav";
            default -> null;
        };

        if (path == null) return;

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(path));
            worldMusic = AudioSystem.getClip();
            worldMusic.open(audioIn);


            FloatControl gainControl = (FloatControl) worldMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) (Math.log(0.4) / Math.log(10) * 40);
            gainControl.setValue(volume);

            worldMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopWorldMusic() {
        if (worldMusic != null && worldMusic.isRunning()) {
            worldMusic.stop();
            worldMusic.close();
        }
    }

    public void saveAttempt() {
        long endTime = System.currentTimeMillis();
        long totalTime = (endTime - startTime) / 1000; // in seconds
        int remainingHP = player.currentHP;

        String log = "Time: " + totalTime + "s | HP Left: " + remainingHP + "\n";

        try {
            FileWriter writer = new FileWriter("game_stats.txt", true); // Append mode
            writer.write(log);
            writer.close();
            System.out.println("✅ Game stats saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}