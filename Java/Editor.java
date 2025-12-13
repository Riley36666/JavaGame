package Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Editor {

    static class LevelCanvas extends JPanel {
        private final int rows = 15;
        private final int cols = 30;
        private final int tileSize = 32;
        private final int[][] tiles;
        private int currentTileType = 1;

        public LevelCanvas() {
            tiles = new int[rows][cols];
            setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
            setBackground(Color.BLACK);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int col = e.getX() / tileSize;
                    int row = e.getY() / tileSize;

                    if (row >= 0 && row < rows && col >= 0 && col < cols) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            tiles[row][col] = 0; // erase
                        } else {
                            tiles[row][col] = currentTileType;
                        }
                        repaint();
                    }
                }
            });
        }

        public int[][] getTiles() {
            return tiles;
        }

        public void setCurrentTileType(int type) {
            currentTileType = type;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    switch (tiles[r][c]) {
                        case 1 -> g.setColor(Color.PINK);   // platform
                        case 2 -> g.setColor(Color.GREEN);  // cactus
                        case 3 -> g.setColor(Color.RED);    // player (unused)
                        case 4 -> g.setColor(Color.BLACK);  // trampoline
                        case 5 -> g.setColor(Color.YELLOW); // win
                        default -> g.setColor(Color.DARK_GRAY);
                    }
                    g.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(c * tileSize, r * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public static void openEditor(int level) {
        JFrame frame = new JFrame("Editor - Level " + level);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        LevelCanvas canvas = new LevelCanvas();

        JPanel toolbar = new JPanel();
        JButton platform = new JButton("Platform");
        JButton cactus = new JButton("Cactus");
        JButton tramp = new JButton("Trampoline");
        JButton win = new JButton("Win");

        platform.addActionListener(e -> canvas.setCurrentTileType(1));
        cactus.addActionListener(e -> canvas.setCurrentTileType(2));
        tramp.addActionListener(e -> canvas.setCurrentTileType(4));
        win.addActionListener(e -> canvas.setCurrentTileType(5));

        toolbar.add(platform);
        toolbar.add(cactus);
        toolbar.add(tramp);
        toolbar.add(win);

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            LevelSaver.saveFromTiles(level, canvas.getTiles(), 32);
            JOptionPane.showMessageDialog(frame, "Level Saved!");
            frame.dispose();
            StartScreen.startscreen();
        });

        JPanel bottom = new JPanel();
        bottom.add(save);

        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(new JScrollPane(canvas), BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        openEditor(1);
    }
}
