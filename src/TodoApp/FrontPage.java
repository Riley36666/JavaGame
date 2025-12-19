package TodoApp;



import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FrontPage {

    static int filecount = FileChecks.filecount();
    static JFrame Frame = new JFrame("Todo app");
    static JPanel Panel = new JPanel();

    public static void startPage() {
        Frame.setSize(600, 800);
        Frame.setAlwaysOnTop(true);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setLayout(new GridLayout(0, 1));

        for (int i = 1; i <= filecount; i++) {
            titles(i);
        }
        createTasksbutton();
        Frame.add(Panel);
        Frame.revalidate();
        Frame.repaint();
        Panel.setVisible(true);
        Frame.setVisible(true);
    }

    private static void createList(String task, int page) {
        JButton tasks = new JButton(task);

        tasks.addActionListener(e -> {
            try {
                Notes.open(page, Frame);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        Panel.add(tasks);

    }

    private static void titles(int i) {
        try (BufferedReader br =
                     new BufferedReader(new FileReader("tasks/task" + i + ".txt"))) {

            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // First valid line is the task title
                createList(line, i);
                break;
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    private static void createTasksbutton() {
        JButton createTaskbutton = new JButton("Create a Task");
        createTaskbutton.addActionListener(e -> {
            CreatingTask.savingTextframe(Frame);
        });

        Panel.add(createTaskbutton);
    }
}
