package Java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Editor {

    // --- Tile canvas inside the Editor ---
    static class LevelCanvas extends JPanel {
        private int rows = 15;
        private int cols = 20;
        private int tileSize = 32;
        private int[][] tiles;
        private int currentTileType = 1; // default tile type

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
                        tiles[row][col] = currentTileType;
                        repaint();
                    }
                }
            });
        }

        public void setCurrentTileType(int type) {
            this.currentTileType = type;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    switch (tiles[r][c]) {
                        case 1 -> g.setColor(Color.BLUE);   // walls
                        case 2 -> g.setColor(Color.RED);    // enemies
                        case 3 -> g.setColor(Color.GREEN);  // player start
                        default -> g.setColor(Color.BLACK); // empty
                    }
                    g.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(c * tileSize, r * tileSize, tileSize, tileSize);
                }
            }
        }

        public int[][] getTiles() {
            return tiles;
        }
    }

    // --- Main editor window ---
    public static void openEditor(int level) {
        JFrame editor = new JFrame("Editing Level " + level);
        editor.setSize(1000, 700);
        editor.setLocationRelativeTo(null);
        editor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editor.setIconImage(
                Toolkit.getDefaultToolkit().getImage(StartScreen.class.getResource("/icon.png"))
        );
        LevelCanvas canvas = new LevelCanvas();

        // Toolbar for tile selection
        JPanel toolbar = new JPanel();
        JButton wallButton = new JButton("Wall");
        JButton enemyButton = new JButton("Enemy");
        JButton playerButton = new JButton("Player");

        wallButton.addActionListener(e -> canvas.setCurrentTileType(1));
        enemyButton.addActionListener(e -> canvas.setCurrentTileType(2));
        playerButton.addActionListener(e -> canvas.setCurrentTileType(3));

        toolbar.add(wallButton);
        toolbar.add(enemyButton);
        toolbar.add(playerButton);

        // Save button
        JButton saveButton = new JButton("Save Level " + level);
        saveButton.addActionListener(e -> {
            int[][] tiles = canvas.getTiles();
            // For now, just print to console. Replace with LevelSaver.save(level, tiles)
            System.out.println("Saving Level " + level);
            for (int r = 0; r < tiles.length; r++) {
                for (int c = 0; c < tiles[r].length; c++) {
                    System.out.print(tiles[r][c] + " ");
                }
                System.out.println();
            }
            JOptionPane.showMessageDialog(editor, "Level " + level + " saved.");
            editor.dispose();
            StartScreen.startscreen();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(saveButton);

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(canvas, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        editor.add(mainPanel);
        editor.setVisible(true);
    }

    // For testing
    public static void main(String[] args) {
        openEditor(1);
    }
}
