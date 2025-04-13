package main;


public class ObjectManager {

    GamePanel gp;
    public ObjectManager(GamePanel gp) {
        this.gp = gp;
    }
    public void clearObjects() {
        for (int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }
    }

}
