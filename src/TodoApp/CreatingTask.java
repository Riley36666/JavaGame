package TodoApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreatingTask {

    public static void savingTextframe(JFrame previousFrame) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            if (previousFrame != null) {
                previousFrame.setVisible(false);
            }

            JFrame frame = new JFrame("Create a New Task");
            frame.setSize(600, 500);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // ---------- MAIN PANEL ----------
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBorder(new EmptyBorder(20, 20, 20, 20));
            content.setBackground(new Color(245, 246, 248));

            // ---------- TITLE ----------
            JLabel header = new JLabel("Create a New Task");
            header.setFont(new Font("Segoe UI", Font.BOLD, 22));
            header.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(header);
            content.add(Box.createVerticalStrut(20));

            // ---------- TITLE FIELD ----------
            JLabel titleLabel = new JLabel("Task Title");
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            content.add(titleLabel);

            JTextField titleField = new JTextField();
            titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            content.add(titleField);
            content.add(Box.createVerticalStrut(15));

            // ---------- DESCRIPTION ----------
            JLabel descLabel = new JLabel("Description");
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            content.add(descLabel);

            JTextArea descriptionArea = new JTextArea();
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JScrollPane descScroll = new JScrollPane(descriptionArea);
            descScroll.setPreferredSize(new Dimension(100, 200));
            descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(descScroll);

            content.add(Box.createVerticalStrut(20));

            // ---------- BUTTONS ----------
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setOpaque(false);

            JButton cancel = new JButton("Cancel");
            JButton save = new JButton("Save Task");

            cancel.addActionListener(e -> {
                frame.dispose();
                if (previousFrame != null) {
                    previousFrame.setVisible(true);
                }
            });

            save.addActionListener(e -> {
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Task title cannot be empty.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // TODO: save task logic here
                JOptionPane.showMessageDialog(frame, "Task saved successfully!");
                saveTask(title, description);
                frame.dispose();
                if (previousFrame != null) {
                    previousFrame.setVisible(true);
                }
            });

            buttonPanel.add(cancel);
            buttonPanel.add(save);

            frame.add(content, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }
    private static void saveTask(String title, String description) {

    }
}
