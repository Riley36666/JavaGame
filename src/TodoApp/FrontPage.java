package TodoApp;

import javax.swing.*;

public class FrontPage {
    static int filecount = filechecks.filecount();
    public static void startPage() {
        JFrame Frame = new JFrame("Todo App");
        Frame.setSize(600, 800);
        Frame.setAlwaysOnTop(true);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setVisible(true);

        for(int i = 0; i < filecount; i++){
            System.out.println(filechecks.getTask(i));
        }
    }
}
