package TodoApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FrontPage {

    static int filecount = FileChecks.filecount();
    static JFrame frame = new JFrame("Todo App");

    // Theme
    static final Color BG = new Color(18, 18, 18);
    static final Color CARD = new Color(28, 28, 28);
    static final Color ACCENT = new Color(0, 153, 255);
    static final Color TEXT = new Color(230, 230, 230);
    static final Color MUTED = new Color(160, 160, 160);

    static JPanel listPanel = new JPanel();

    public static void startPage() {
        SwingUtilities.invokeLater(() -> {
            setLookAndFeel();

            frame.setSize(600, 800);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.getContentPane().setBackground(BG);

            frame.add(header(), BorderLayout.NORTH);
            frame.add(content(), BorderLayout.CENTER);
            frame.add(bottomBar(), BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }

    /* ---------------- HEADER ---------------- */

    private static JPanel header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Your Tasks");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel(filecount + " active tasks");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(BG);
        text.add(title);
        text.add(Box.createVerticalStrut(4));
        text.add(subtitle);

        header.add(text, BorderLayout.WEST);
        return header;
    }

    /* ---------------- CONTENT ---------------- */

    private static JScrollPane content() {
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG);
        listPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        for (int i = 1; i <= filecount; i++) {
            readTitle(i);
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);

        return scroll;
    }

    /* ---------------- TASK CARD ---------------- */

    private static void createTaskCard(String title, int page) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel text = new JLabel(title);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        text.setForeground(TEXT);

        JButton open = new JButton("OPEN");
        open.setFont(new Font("Segoe UI", Font.BOLD, 12));
        open.setForeground(ACCENT);
        open.setBackground(CARD);
        open.setBorderPainted(false);
        open.setFocusPainted(false);
        open.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        open.addActionListener(e -> {
            try {
                Notes.open(page, frame);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        card.add(text, BorderLayout.WEST);
        card.add(open, BorderLayout.EAST);

        listPanel.add(card);
        listPanel.add(Box.createVerticalStrut(12));
    }

    /* ---------------- FILE READ ---------------- */

    private static void readTitle(int i) {
        try (BufferedReader br = new BufferedReader(
                new FileReader("tasks/task" + i + ".txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    createTaskCard(line, i);
                    break;
                }
            }
        } catch (IOException e) {
            // Ignore missing files silently
        }
    }

    /* ---------------- BOTTOM BAR ---------------- */

    private static JPanel bottomBar() {
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
        bar.setBackground(BG);
        bar.setBorder(new EmptyBorder(16, 20, 20, 20));

        JButton add = new JButton("+  New Task");
        JButton remove = new JButton("Remove Task");

        // ---------- ADD BUTTON (PRIMARY) ----------
        add.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add.setForeground(Color.BLACK);
        add.setBackground(ACCENT);
        add.setFocusPainted(false);
        add.setBorder(new EmptyBorder(12, 22, 12, 22));
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        add.addActionListener(e -> CreatingTask.savingTextframe(frame));

        // ---------- REMOVE BUTTON (DESTRUCTIVE) ----------
        remove.setFont(new Font("Segoe UI", Font.BOLD, 16));
        remove.setForeground(new Color(220, 80, 80));
        remove.setBackground(BG);
        remove.setFocusPainted(false);
        remove.setBorder(BorderFactory.createLineBorder(new Color(220, 80, 80), 2));
        remove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        remove.addActionListener(e -> {

        });

        // ---------- LAYOUT ----------
        bar.add(remove);
        bar.add(Box.createHorizontalGlue());
        bar.add(add);

        return bar;
    }


    /* ---------------- LAF ---------------- */

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }
}
