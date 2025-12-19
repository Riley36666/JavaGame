package TodoApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FrontPage {

    static int filecount = FileChecks.filecount();
    static JFrame Frame = new JFrame("Todo App");
    static JPanel Panel = new JPanel();

    public static void startPage() {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // ---------- FRAME ----------
            Frame.setSize(600, 800);
            Frame.setLocationRelativeTo(null);
            Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Frame.setLayout(new BorderLayout());
            Frame.setBackground(Color.BLACK);
            // ---------- CONTENT PANEL ----------
            Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));
            Panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            Panel.setBackground(new Color(0, 0, 0));

            // ---------- ADD TASK TITLES ----------
            for (int i = 1; i <= filecount; i++) {
                titles(i);
            }

            // ---------- ADD CREATE TASK BUTTON ----------
            createTasksbutton();

            // ---------- SCROLL SUPPORT ----------
            JScrollPane scrollPane = new JScrollPane(Panel);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            Frame.add(scrollPane, BorderLayout.CENTER);
            Frame.setVisible(true);
        });
    }

    // ---------- CREATE TASK BUTTON CARD ----------
    private static void createList(String task, int page) {
        JButton tasks = new JButton(task);
        tasks.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tasks.setBackground(Color.WHITE);
        tasks.setForeground(new Color(40, 40, 40));
        tasks.setFocusPainted(false);
        tasks.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        tasks.setAlignmentX(Component.LEFT_ALIGNMENT);

        tasks.addActionListener(e -> {
            try {
                Notes.open(page, Frame);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.setBackground(new Color(245, 246, 248));
        wrapper.add(tasks, BorderLayout.CENTER);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, tasks.getPreferredSize().height + 10));

        Panel.add(wrapper);
        Panel.add(Box.createVerticalStrut(10));
    }

    // ---------- READ TASK TITLE ----------
    private static void titles(int i) {
        try (BufferedReader br = new BufferedReader(new FileReader("tasks/task" + i + ".txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                createList(line, i);
                break;
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // ---------- CREATE TASK BUTTON ----------
    private static void createTasksbutton() {
        JButton createTaskbutton = new JButton("Create a Task");
        createTaskbutton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        createTaskbutton.setBackground(new Color(33, 33, 33));
        createTaskbutton.setForeground(Color.WHITE);
        createTaskbutton.setFocusPainted(false);
        createTaskbutton.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        createTaskbutton.setAlignmentX(Component.LEFT_ALIGNMENT);

        createTaskbutton.addActionListener(e -> {
            CreatingTask.savingTextframe(Frame);
        });

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.setBackground(new Color(245, 246, 248));
        wrapper.add(createTaskbutton, BorderLayout.CENTER);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, createTaskbutton.getPreferredSize().height + 10));

        Panel.add(Box.createVerticalStrut(20));
        Panel.add(wrapper);
    }
}
