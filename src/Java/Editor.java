package Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Editor {

    static class LevelCanvas extends JPanel {

        private final int tileSize = 32;
        private int[][] tiles = new int[0][0];

        private int currentTileType = 1;
        private int playerX = 100;
        private int playerY = 630;
        private boolean placingPlayer = false;

        public LevelCanvas() {
            setBackground(Color.BLACK);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int col = e.getX() / tileSize;
                    int row = e.getY() / tileSize;

                    if (row < 0 || col < 0) return;
                    if (row >= tiles.length || col >= tiles[0].length) return;

                    if (placingPlayer) {
                        playerX = col * tileSize;
                        playerY = row * tileSize;
                        placingPlayer = false;
                        repaint();
                        return;
                    }

                    tiles[row][col] = SwingUtilities.isRightMouseButton(e)
                            ? 0
                            : currentTileType;

                    repaint();
                }
            });
        }

        private void ensureGridSize() {
            int rows = getHeight() / tileSize;
            int cols = getWidth() / tileSize;

            if (rows <= 0 || cols <= 0) return;

            if (tiles.length == rows && tiles[0].length == cols) return;

            int[][] newTiles = new int[rows][cols];

            for (int r = 0; r < Math.min(rows, tiles.length); r++) {
                for (int c = 0; c < Math.min(cols, tiles[0].length); c++) {
                    newTiles[r][c] = tiles[r][c];
                }
            }

            tiles = newTiles;
        }

        public int[][] getTiles() {
            return tiles;
        }

        public int getPlayerX() {
            return playerX;
        }

        public int getPlayerY() {
            return playerY;
        }

        public void setCurrentTileType(int type) {
            currentTileType = type;
            placingPlayer = false;
        }

        public void setPlacingPlayer(boolean placing) {
            placingPlayer = placing;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ensureGridSize();

            for (int r = 0; r < tiles.length; r++) {
                for (int c = 0; c < tiles[0].length; c++) {

                    switch (tiles[r][c]) {
                        case 1 -> g.setColor(GamePanel.floorColor());
                        case 2 -> g.setColor(GamePanel.catcusColor());
                        case 4 -> g.setColor(Color.BLACK);
                        case 5 -> g.setColor(Color.YELLOW);
                        default -> g.setColor(Color.DARK_GRAY);
                    }

                    g.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(c * tileSize, r * tileSize, tileSize, tileSize);
                }
            }

            g.setColor(Color.BLUE);
            g.fillOval(playerX, playerY, tileSize, tileSize);
        }
    }

    public static void openEditor(int level) {

        JFrame frame = new JFrame("Editor - Level " + level);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        LevelCanvas canvas = new LevelCanvas();

        JPanel toolbar = new JPanel();

        JButton player = new JButton("Player");
        JButton platform = new JButton("Platform");
        JButton cactus = new JButton("Cactus");
        JButton tramp = new JButton("Trampoline");
        JButton win = new JButton("Win");
        JButton erase = new JButton("Erase");

        player.addActionListener(e -> canvas.setPlacingPlayer(true));
        platform.addActionListener(e -> canvas.setCurrentTileType(1));
        cactus.addActionListener(e -> canvas.setCurrentTileType(2));
        tramp.addActionListener(e -> canvas.setCurrentTileType(4));
        win.addActionListener(e -> canvas.setCurrentTileType(5));
        erase.addActionListener(e -> canvas.setCurrentTileType(0));

        toolbar.add(player);
        toolbar.add(platform);
        toolbar.add(cactus);
        toolbar.add(tramp);
        toolbar.add(win);
        toolbar.add(erase);

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            boolean success = LevelSaver.saveFromTiles(
                    level,
                    canvas.getTiles(),
                    32,
                    canvas.getPlayerX(),
                    canvas.getPlayerY()
            );

            if (!success) {
                JOptionPane.showMessageDialog(
                        frame,
                        "You must place at least one WIN tile.",
                        "Invalid Level",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    frame,
                    "Level Saved!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            frame.dispose();
            StartScreen.startscreen();
        });

        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(save, BorderLayout.SOUTH);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
