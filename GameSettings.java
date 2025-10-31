import java.io.*;
import java.util.Properties;

public class GameSettings {
    private static final String SETTINGS_FILE = "settings.properties";
    private static final Properties props = new Properties();

    // Load or create defaults on startup
    static {
        load();
    }

    public static void load() {
        try (FileInputStream in = new FileInputStream(SETTINGS_FILE)) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("Settings file not found. Creating default settings...");
            setDefaults();
            save();
        }
    }

    public static void save() {
        try (FileOutputStream out = new FileOutputStream(SETTINGS_FILE)) {
            props.store(out, "Game Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setDefaults() {
        props.setProperty("player.color", "RED");
        props.setProperty("floor.color", "GREEN");
        props.setProperty("fullscreen", "false");
        props.setProperty("FPS", "60");
    }

    // --- Player color ---
    public static String getPlayerColor() {
        return props.getProperty("player.color", "RED");
    }

    public static void setPlayerColor(String color) {
        props.setProperty("player.color", color);
    }

    // --- Floor color ---
    public static String getFloorColor() {
        return props.getProperty("floor.color", "GREEN");
    }

    public static void setFloorColor(String color) {
        props.setProperty("floor.color", color);
    }
    public static void setFPS(String fps) {
        props.setProperty("FPS", fps);
    }
    // --- Fullscreen ---
    public static boolean isFullscreen() {
        return Boolean.parseBoolean(props.getProperty("fullscreen", "false"));
    }

    public static void setFullscreen(boolean fullscreen) {
        props.setProperty("fullscreen", String.valueOf(fullscreen));
    }
    // --- FPS ---
    public static int getFPS() {
        return Integer.parseInt(props.getProperty("FPS", "60"));
    }
}
