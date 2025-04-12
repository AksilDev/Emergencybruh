package run;

public class RunRecord {
    private final String name;
    private final double time;  // in seconds
    private final int hp;       // HP left

    public RunRecord(String name, double time, int hp) {
        this.name = name;
        this.time = time;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public double getTime() {
        return time;
    }

    public int getHp() {
        return hp;
    }
}
