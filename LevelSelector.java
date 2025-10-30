import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelSelector {

    public static void open(JFrame startFrame) {
        // Hide the start screen
        startFrame.setVisible(false);

        // --- Frame setup ---
        JFrame frame = new JFrame("Level Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        // --- Background panel with gradient ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(10, 10, 30),
                        0, getHeight(), new Color(30, 30, 80));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout(0, 40));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        // --- Title label ---
        JLabel title = new JLabel("Select a Level", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        backgroundPanel.add(title, BorderLayout.NORTH);

        // --- Center panel with grid of buttons ---
        JPanel gridPanel = new JPanel(new GridLayout(2, 5, 20, 20));
        gridPanel.setOpaque(false);

        for (int i = 1; i <= 10; i++) {
            JButton button = createLevelButton("Level " + i);
            int level = i;
            button.addActionListener(e -> {
                frame.dispose();
                load(level);
            });
            gridPanel.add(button);
        }

        backgroundPanel.add(gridPanel, BorderLayout.CENTER);

        // --- Bottom-right: Back button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        styleSecondaryButton(backButton);
        backButton.addActionListener(e -> {
            frame.dispose();
            StartScreen.startscreen();
        });

        bottomPanel.add(backButton);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // --- Show frame ---
        frame.add(backgroundPanel);
        frame.setVisible(true);
    }

    private static JButton createLevelButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(60, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 160, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(60, 120, 220));
            }
        });

        return button;
    }

    private static void styleSecondaryButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 100, 100));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 70, 70));
            }
        });
    }

    private static void load(int level) {
        System.out.println("Loading level " + level);
        GamePanel.start(level);
    }
}
