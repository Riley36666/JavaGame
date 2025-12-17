package Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class LevelMakerSelector {

    private static final String LEVEL_FOLDER = "levels/";
    public static void open() {
        JFrame frame = new JFrame("Level Maker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(StartScreen.class.getResource("/icon.png"))
        );
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(10, 10, 30),
                        0, getHeight(), new Color(30, 30, 80)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout(0, 40));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        int nextLevel = getNextLevelNumber();

        // --- Top panel ---
        JLabel title = new JLabel("Level Maker", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        backgroundPanel.add(title, BorderLayout.NORTH);

        // --- Center content ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        centerPanel.setOpaque(false);

        JLabel infoLabel = new JLabel(
                "Next available level: Level " + nextLevel,
                SwingConstants.CENTER
        );
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JButton createButton = new JButton("Create Level " + nextLevel);
        JButton backButton = new JButton("Back");
        stylePrimaryButton(createButton);
        stylePrimaryButton(backButton);
        createButton.addActionListener(e -> {
            frame.dispose();
            Editor.openEditor(nextLevel);
        });
        backButton.addActionListener(e -> {
            frame.dispose();
            Java.StartScreen.startscreen();
        });
        centerPanel.add(infoLabel);
        centerPanel.add(createButton);
        centerPanel.add(backButton);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        frame.add(backgroundPanel);
        frame.setVisible(true);
    }

    // --- Finds next unused level number ---
    public static int getNextLevelNumber() {
        File folder = new File(LEVEL_FOLDER);
        if (!folder.exists()) folder.mkdir();

        int highest = 0;

        for (File f : folder.listFiles()) {
            String name = f.getName().toLowerCase();
            if (name.startsWith("level") && name.endsWith(".txt")) {
                try {
                    int n = Integer.parseInt(name.substring(5, name.length() - 4));
                    if (n > highest) highest = n;
                } catch (Exception ignored) {}
            }
        }
        return highest + 1;
    }


    // --- Button styling (matches LevelSelector) ---
    public static void stylePrimaryButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(60, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setBorder(BorderFactory.createEmptyBorder(14, 30, 14, 30));
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
    }
}
