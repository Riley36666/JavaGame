package Java;

public class Gamemain {
    static void main() {
        GameSettings.load();
        StartScreen.startscreen();
        if(LevelMakerSelector.getNextLevelNumber() < 3){
            LevelSaver.createDefault();
        }
    }
}