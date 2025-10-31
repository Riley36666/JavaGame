import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelSelector {

    private static int currentPage = 1;

    public static void open(JFrame startFrame) {
        startFrame.setVisible(false);
        showPage(1); // start with page 1
    }

    private static void showPage(int page) {
        JFrame frame = new JFrame("Level Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

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

        JLabel title = new JLabel("Select a Level", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        backgroundPanel.add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 5, 20, 20));
        gridPanel.setOpaque(false);

        // --- determine which levels to show on this page ---
        int startLevel = (page - 1) * 10 + 1;
        int endLevel = startLevel + 9;

        for (int i = startLevel; i <= endLevel; i++) {
            JButton button = createLevelButton("Level " + i);
            int level = i;
            button.addActionListener(e -> {
                frame.dispose();
                load(level);
            });
            gridPanel.add(button);
        }

        backgroundPanel.add(gridPanel, BorderLayout.CENTER);

        // --- Bottom navigation panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Left: Back to Start button
        JButton backButton = new JButton("Back");
        styleSecondaryButton(backButton);
        backButton.addActionListener(e -> {
            frame.dispose();
            StartScreen.startscreen();
        });
        bottomPanel.add(backButton, BorderLayout.WEST);

        // Right: Page navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setOpaque(false);

        if (page > 1) {
            JButton prevPage = new JButton("<");
            styleSecondaryButton(prevPage);
            prevPage.addActionListener(e -> {
                frame.dispose();
                showPage(page - 1);
            });
            navPanel.add(prevPage);
        }
        //JLabel WIP = new  JLabel("WIP");

        if (page < 2) { // we only have 2 pages
            JButton nextPage = new JButton(">");
            styleSecondaryButton(nextPage);
            nextPage.addActionListener(e -> {
                frame.dispose();
                showPage(page + 1);
            });
            navPanel.add(nextPage);
            //.add(WIP);
        }

        bottomPanel.add(navPanel, BorderLayout.EAST);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

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
