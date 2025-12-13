package Java;

import java.io.FileWriter;
import java.io.IOException;

public class LevelSaver {

    private static final String LEVEL_FOLDER = "levels/";

    public static void saveEmpty(int level) {
        String path = LEVEL_FOLDER + "level" + level + ".txt";

        try (FileWriter fw = new FileWriter(path)) {
            fw.write("# Level " + level + "\n");
            fw.write("# PLATFORM x y width height\n");
            fw.write("# CACTUS x y width height\n");
            fw.write("# TRAMP x y width height\n");
            fw.write("# WIN x y width height\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
