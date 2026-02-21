package TodoApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Notes {

    private static JFrame subnote;
    private static JPanel contentPanel;

    public static void open(int page, JFrame frame) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            try (BufferedReader br =
                         new BufferedReader(new FileReader("tasks/task" + page + ".txt"))) {

                // ---------- READ TITLE ----------
                String title = br.readLine();
                if (title == null || title.trim().isEmpty()) {
                    title = "Untitled Note";
                }

                // ---------- CREATE WINDOW ----------
                createNote(title);
                frame.dispose();

                // ---------- READ CONTENT ----------
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    addText(line);
                }

                subnote.setVisible(true);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to load note file:\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private static void createNote(String title) {
        subnote = new JFrame(title);
        subnote.setSize(900, 700);
        subnote.setLocationRelativeTo(null);
        subnote.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        subnote.setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(245, 246, 248));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        subnote.add(scrollPane, BorderLayout.CENTER);
    }

    private static void addText(String text) {

        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 16, 12, 16));
        card.setBackground(Color.WHITE);

        JLabel label = new JLabel("<html>" + text + "</html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(new Color(40, 40, 40));

        card.add(label, BorderLayout.CENTER);

        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

        contentPanel.add(card);
        contentPanel.add(Box.createVerticalStrut(12));
    }
}
