package run;

import java.io.*;
import java.util.*;

public class RunManager {
    private static RunManager instance;
    private final List<RunRecord> topRuns = new ArrayList<>();
    private final String FILE_PATH = "game_stats.txt";
    private String playerName;

    private RunManager() {
        loadRuns();
    }

    public static RunManager getInstance() {
        if (instance == null) instance = new RunManager();
        return instance;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public List<RunRecord> getTopRuns() {
        return topRuns;
    }

    public void trySaveRun(double time, int hp) {
        RunRecord newRun = new RunRecord(playerName, time, hp);
        topRuns.add(newRun);
        topRuns.sort(Comparator.comparingDouble(RunRecord::getTime));
        if (topRuns.size() > 5) topRuns.remove(5);
        saveRunsToFile();
    }

    private void loadRuns() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    double time = Double.parseDouble(parts[1].trim());
                    int hp = Integer.parseInt(parts[2].trim());
                    topRuns.add(new RunRecord(name, time, hp));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRunsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (RunRecord run : topRuns) {
                bw.write(run.getName() + "," + run.getTime() + "," + run.getHp());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
