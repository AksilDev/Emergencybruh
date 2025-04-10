package util;

import java.io.*;
import java.util.*;

public class GameStatsManager {
    private static final String FILE_PATH = "game_stats.txt";

    public static void saveAttempt(int timeSec, int hpLeft) {
        List<String> attempts = loadAttempts();
        attempts.add(String.format("Time: %ds | HP Left: %d", timeSec, hpLeft));

        // Sort by time (ascending)
        attempts.sort(Comparator.comparingInt(GameStatsManager::extractTime));

        // Keep only top 5
        while (attempts.size() > 5)
            attempts.remove(attempts.size() - 1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String entry : attempts)
                writer.write(entry + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadAttempts() {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null)
                list.add(line);
        } catch (IOException ignored) {}
        return list;
    }

    private static int extractTime(String entry) {
        try {
            return Integer.parseInt(entry.split(" ")[1].replace("s", ""));
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }
}
