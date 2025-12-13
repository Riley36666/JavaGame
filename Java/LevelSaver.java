package Java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LevelSaver {

    private static final String LEVEL_FOLDER = "levels/";

    public static void saveFromTiles(int level, int[][] tiles, int tileSize) {
        String path = LEVEL_FOLDER + "level" + level + ".txt";

        try (FileWriter fw = new FileWriter(path)) {
            fw.write("# Level " + level + "\n");
            fw.write("# PLATFORM x y w h\n");
            fw.write("# CACTUS x y w h\n");
            fw.write("# TRAMP x y w h\n");
            fw.write("# WIN x y w h\n");

            for (int r = 0; r < tiles.length; r++) {
                for (int c = 0; c < tiles[r].length; c++) {
                    int x = c * tileSize;
                    int y = r * tileSize;

                    switch (tiles[r][c]) {
                        case 1 -> fw.write("PLATFORM " + x + " " + y + " " + tileSize + " " + tileSize + "\n");
                        case 2 -> fw.write("CACTUS " + x + " " + y + " " + tileSize + " " + tileSize + "\n");
                        case 4 -> fw.write("TRAMP " + x + " " + y + " " + tileSize + " " + tileSize + "\n");
                        case 5 -> fw.write("WIN " + x + " " + y + " " + tileSize + " " + tileSize + "\n");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void createDefualt() {
        System.out.println("Creating default levels...");

        for (int i = 0; i < 3; i++) {
            int level = LevelMakerSelector.getNextLevelNumber();
            saveEmpty(level);
        }
    }
    public static void saveEmpty(int level) {
        ensureFolderExists();

        String path = LEVEL_FOLDER + "level" + level + ".txt";

        try (FileWriter fw = new FileWriter(path)) {

            fw.write("# Level " + level + "\n");
            fw.write("# PLATFORM x y w h\n");
            fw.write("# CACTUS x y w h\n");
            fw.write("# TRAMP x y w h\n");
            fw.write("# WIN x y w h\n\n");
            fw.write("PLATFORM 0 680 3000 50\n");
            fw.write("WIN 2800 630 50 50\n");
            System.out.println("Created level " + level);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void ensureFolderExists() {
        File folder = new File(LEVEL_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();}
}}