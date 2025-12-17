package Java;

import java.io.*;
import java.util.*;

public class LevelData {

    public static int[][] getLevel(int level) {
        return parseFile(level, "PLATFORM");
    }

    public static int[][] wincon(int level) {
        return parseFile(level, "WIN");
    }

    public static int[][] cactus(int level) {
        return parseFile(level, "CACTUS");
    }

    public static int[][] trampoline(int level) {
        return parseFile(level, "TRAMP");
    }

    public static int[] playerStart(int level) {
        try (BufferedReader br = new BufferedReader(new FileReader("levels/level" + level + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) continue;

                String[] parts = line.split(" ");
                if (parts[0].equals("PLAYER")) {
                    return new int[]{
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2])
                    };
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Default start position
        return new int[]{100, 630};
    }

    public static int getLevelBottom(int level) {
        int bottom = 0;
        int[][] platforms = getLevel(level);
        for (int[] p : platforms) {
            bottom = Math.max(bottom, p[1] + p[3]);
        }
        // Add some buffer below the lowest platform
        return bottom + 200;
    }

    private static int[][] parseFile(int level, String type) {
        List<int[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("levels/level" + level + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) continue;

                String[] parts = line.split(" ");
                if (parts[0].equals(type)) {
                    list.add(new int[]{
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            Integer.parseInt(parts[3]),
                            Integer.parseInt(parts[4])
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list.toArray(new int[0][]);
    }
}