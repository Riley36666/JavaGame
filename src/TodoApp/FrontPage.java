package TodoApp;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class FrontPage {
    static int filecount = filechecks.filecount();
    static JFrame Frame = new JFrame("Todo app");

    public static void startPage() {
        Frame.setSize(600, 800);
        Frame.setAlwaysOnTop(true);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setLayout(new GridLayout(0, 1));
        for (int i = 1; i <= filecount; i++) {
            titles(i);
        }
        Frame.setVisible(true);
    }
    private static void createtask(String title) {
        JLabel tasks = new JLabel(title);
        Frame.add(tasks);
        Frame.revalidate();
        Frame.repaint();
    }
    private static void titles(int i) {
        try (
                BufferedReader br = new BufferedReader(new FileReader("tasks/task" + i + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty()) continue;
                String[] parts = line.split(" ");
                    String title = String.join("", parts);
                    createtask(title);
                }
            } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
