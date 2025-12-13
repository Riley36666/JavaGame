package Java;

public class main{
    public static void main(String[] args) {
        GameSettings.load();
        StartScreen.startscreen();
        if(LevelMakerSelector.getNextLevelNumber() < 3){
            LevelSaver.createDefualt();
        }
    }
}