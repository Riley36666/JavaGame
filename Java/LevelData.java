package Java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelData {

    private static final String LEVEL_FOLDER = "levels/";

    // GamePanel EXPECTS these names:
    public static int[][] getLevel(int levelNumber) {
        return loadSection(levelNumber, "PLATFORM");
    }

    public static int[][] wincon(int levelNumber) {
        return loadSection(levelNumber, "WIN");
    }

    public static int[][] cactus(int levelNumber) {
        return loadSection(levelNumber, "CACTUS");
    }

    public static int[][] trampoline(int levelNumber) {
        return loadSection(levelNumber, "TRAMP");
    }

    // Also keep your naming if you want:
    public static int[][] getWinCon(int levelNumber) {
        return wincon(levelNumber);
    }

    public static int[][] getCactus(int levelNumber) {
        return cactus(levelNumber);
    }

    public static int[][] getTrampoline(int levelNumber) {
        return trampoline(levelNumber);
    }

    // --------------------------------------------------------
    // Shared loader
    // --------------------------------------------------------
    private static int[][] loadSection(int levelNumber, String keyword) {
        String path = LEVEL_FOLDER + "level" + levelNumber + ".txt";
        List<int[]> objects = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                if (line.startsWith(keyword)) {
                    String[] p = line.split(" ");

                    if (p.length < 5) continue;

                    int x = Integer.parseInt(p[1]);
                    int y = Integer.parseInt(p[2]);
                    int w = Integer.parseInt(p[3]);
                    int h = Integer.parseInt(p[4]);

                    objects.add(new int[]{x, y, w, h});
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to load level file: " + path);
            e.printStackTrace();
        }

        return objects.toArray(new int[0][]);
    }
}
