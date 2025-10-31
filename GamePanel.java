import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // ---------- Player settings ----------
    private int playerX = 100;
    private int playerY = 630;
    private final int playerWidth = 50;
    private final int playerHeight = 50;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Color playerColor = Color.RED; // default
    private Color floorColor = Color.GREEN;
    private int time = 16;
    // ---------- Physics ----------
    private double velocityY = 0;
    private final double gravity = 0.6;
    private final double jumpStrength = -12;
    private boolean onGround = false;

    // ---------- Level data ----------
    private final int[][] platforms;
    private final int[][] wincon;

    private Timer timer;
    private JFrame frame;
    private boolean gameOver = false;
    private boolean lost = false;
    private int currentlevel;
    // ---------- Constructors ----------
    public GamePanel(int[][] platforms, int[][] wincon) {
        this.platforms = platforms;
        this.wincon = wincon;
        init();
    }

    public GamePanel(int level) {
        this.platforms = LevelData.getLevel(level);
        this.wincon = LevelData.wincon(level);
        this.currentlevel = level;
        init();
    }

    // ---------- Shared setup ----------
    private void init() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Load player color from settings
        String colorName = GameSettings.getPlayerColor();
        switch (colorName.toUpperCase()) {
            case "RED": playerColor = Color.RED; break;
            case "GREEN": playerColor = Color.GREEN; break;
            case "YELLOW": playerColor = Color.YELLOW; break;
            case "BLUE": playerColor = Color.BLUE; break;
            case "PINK": playerColor = Color.PINK; break;
            default: playerColor = Color.RED; break;
        }
        String floorColorName = GameSettings.getFloorColor();
        switch (floorColorName.toUpperCase()) {
            case "RED": floorColor = Color.RED; break;
            case "GREEN": floorColor = Color.GREEN; break;
            case "YELLOW": floorColor = Color.YELLOW; break;
            case "BLUE": floorColor = Color.BLUE; break;
            case "PINK": floorColor = Color.PINK; break;
            default: floorColor = Color.GREEN; break;
        }
        //FPS Amounnts
        int FPS = GameSettings.getFPS();
        switch (FPS){
            case 30: time = 33; break;
            case 60: time = 16; break;
            case 120: time = 8; break;
            case 240: time = 4; break;
        }
        timer = new Timer(time, this);
        timer.start();
    }

    // ---------- Entry Point ----------
    public static void start(int level) {
        GamePanel panel = new GamePanel(level);
        JFrame frame = new JFrame("Level " + level);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setResizable(false);
        frame.add(panel);
        panel.setFrame(frame);
        frame.setVisible(true);
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    // ---------- Rendering ----------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Player
        g.setColor(playerColor);
        g.fillRect(playerX, playerY, playerWidth, playerHeight);

        // Platforms
        g.setColor(floorColor);
        for (int[] p : platforms) {
            g.fillRect(p[0], p[1], p[2], p[3]);
        }

        // Win blocks
        g.setColor(Color.YELLOW);
        for (int[] p : wincon) {
            g.fillRect(p[0], p[1], p[2], p[3]);
        }

        // Win message
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("YOU WIN!", getWidth() / 2 - 150, getHeight() / 2);
        }
        if(lost){
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("LOST!", getWidth() / 2 - 150, getHeight() / 2);
        }
    }

    // ---------- Game Loop ----------
    @Override
    public void actionPerformed(ActionEvent e) {

        if (gameOver) return; // stop updating after win

        // Movement
        if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A))
            playerX -= 8;
        if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D))
            playerX += 8;
        if ((pressedKeys.contains(KeyEvent.VK_SPACE) ||
                pressedKeys.contains(KeyEvent.VK_W) ||
                pressedKeys.contains(KeyEvent.VK_UP)) && onGround) {
            velocityY = jumpStrength;
            onGround = false;
        }

        // Apply gravity
        velocityY += gravity;
        playerY += velocityY;

        // Collision detection (platforms)
        onGround = false;
        Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);

        for (int[] p : platforms) {
            Rectangle platRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (playerRect.intersects(platRect)) {
                // landed on top
                if (playerY + playerHeight - velocityY <= p[1]) {
                    playerY = p[1] - playerHeight;
                    velocityY = 0;
                    onGround = true;
                }
            }
        }
        if(playerY > 680) {
            lost = true;
            timer.stop();
            repaint();
            javax.swing.Timer restartTimer = new javax.swing.Timer(500, e2 -> {
                start(currentlevel);
            });
            restartTimer.setRepeats(false);
            restartTimer.start();
            return;
        }
        // Win condition
        for (int[] p : wincon) {
            Rectangle winRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (playerRect.intersects(winRect)) {
                gameOver = true;
                timer.stop();
                repaint();

                // Wait 5 seconds before restarting
                javax.swing.Timer restartTimer = new javax.swing.Timer(500, e2 -> {
                    frame.dispose();
                    LevelSelector.open(frame);
                });
                restartTimer.setRepeats(false);
                restartTimer.start();
                return;
            }
        }

        repaint();
    }

    // ---------- Input ----------
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            if (frame != null) frame.dispose();
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
