package main;

import entity.*;
import object.SuperObject;
import run.RunManager;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    public final int tileSize = 16 * 3;
    public final int maxScreenCol = 33;
    public final int maxScreenRow = 20;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    public int currentWorld = 1;
    public static final int PLAY_STATE = 0;
    public static final int DEATH_STATE = 1;
    public static final int WIN_STATE = 2;
    public static final int DIALOGUE_STATE = 3;
    public int gameState = PLAY_STATE;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public WorldManager worldManager = new WorldManager(this);
    public ObjectManager objectManager = new ObjectManager(this);
    public EnemySpawner enemySpawner = new EnemySpawner(this);

    public Player player = new Player(this, keyH);
    public Enemy[] enemies = new Enemy[10];
    public SuperObject[] obj = new SuperObject[20];
    public UI ui = new UI(this);
    public DialogueManager dialogueManager = new DialogueManager(this);
    public java.util.List<String> currentDialogue;

    public boolean hasKey = false;
    public long startTime = System.currentTimeMillis();
    private Thread gameThread;
    private MainWindow window;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startTime = System.currentTimeMillis();

        worldManager.loadWorld(currentWorld);
        MusicManager.getInstance().playWorldMusic(currentWorld);
    }

    public void setWindow(MainWindow window) {
        this.window = window;
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
                                if (window != null) {
                                    SwingUtilities.invokeLater(() -> window.showVictoryPanel());
                                }
                            } else {
                                MusicManager.getInstance().stop(); // stop any old music
                                worldManager.loadWorld(currentWorld);
                                MusicManager.getInstance().playWorldMusic(currentWorld);
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
        for (SuperObject o : obj) if (o != null)
            o.draw(g2, o.worldX - player.worldX + player.screenX, o.worldY - player.worldY + player.screenY, tileSize);
        for (Enemy e : enemies) if (e != null) e.draw(g2);
        player.draw(g2);
        ui.draw(g2);
        g2.dispose();
    }

    public void handleDeathRestart() {
        currentWorld = 1;
        hasKey = false;
        player.setDefaultValues();
        MusicManager.getInstance().stop();
        worldManager.loadWorld(currentWorld);
        MusicManager.getInstance().playWorldMusic(currentWorld);
        gameState = PLAY_STATE;
    }
}
