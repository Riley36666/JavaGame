package Java;

public class Gamemain {
    public static void main(String[] args) {
        GameSettings.load();
        StartScreen.startscreen();
        if(LevelMakerSelector.getNextLevelNumber() < 3){
            LevelSaver.createDefault();
        }
    }
}