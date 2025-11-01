import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // ---------- Player settings ----------
    private int playerX = 100;
    private int playerY = 630;
    private final int playerWidth = 50;
    private final int playerHeight = 50;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Color playerColor = Color.RED;
    private Color floorColor = Color.PINK;
    private Color cactusColor = Color.GREEN;
    private int time = 16;

    // ---------- Physics ----------
    private double velocityY = 0;
    private final double gravity = 0.6;
    private final double jumpStrength = -12;
    private boolean onGround = false;
    private int cameraX = 0;

    // ---------- Level data ----------
    private final int[][] platforms;
    private final int[][] wincon;
    private final int[][] cactus;
    private Timer timer;
    private JFrame frame;
    private boolean gameOver = false;
    private boolean lost = false;
    private int currentlevel;

    // ---------- Background ----------
    private Image backgroundImage;
    private int bgWidth = 1920;
    private int bgHeight = 1080;

    // ---------- Constructors ----------
    public GamePanel(int[][] platforms, int[][] wincon, int[][] cactus) {
        this.platforms = platforms;
        this.wincon = wincon;
        this.cactus = cactus;
        init();
    }

    public GamePanel(int level) {
        this.platforms = LevelData.getLevel(level);
        this.wincon = LevelData.wincon(level);
        this.cactus = LevelData.cactus(level);
        this.currentlevel = level;
        init();
    }

    // ---------- Shared setup ----------
    private void init() {
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Load background image
        backgroundImage = Toolkit.getDefaultToolkit().getImage(
                GamePanel.class.getResource("/background.png")
        );
        if (backgroundImage == null) {
            // Fallback for testing in IntelliJ
            backgroundImage = Toolkit.getDefaultToolkit().getImage("images/background.png");
        }

        // Load colors
        String colorName = GameSettings.getPlayerColor();
        switch (colorName.toUpperCase()) {
            case "GREEN": playerColor = Color.GREEN; break;
            case "YELLOW": playerColor = Color.YELLOW; break;
            case "BLUE": playerColor = Color.BLUE; break;
            case "PINK": playerColor = Color.PINK; break;
            default: playerColor = Color.RED; break;
        }
        // load floor color
        String floorColorName = GameSettings.getFloorColor();
        switch (floorColorName.toUpperCase()) {
            case "RED": floorColor = Color.RED; break;
            case "YELLOW": floorColor = Color.YELLOW; break;
            case "BLUE": floorColor = Color.BLUE; break;
            case "PINK": floorColor = Color.PINK; break;
            default: floorColor = Color.PINK; break;
        }
        // load cactus color
        String cactusColorName = GameSettings.getCactusColor();
        switch (cactusColorName.toUpperCase()) {
            case "RED": cactusColor = Color.RED; break;
            case "YELLOW": cactusColor = Color.YELLOW; break;
            case "BLUE": cactusColor = Color.BLUE; break;
            case "PINK": cactusColor = Color.PINK; break;
            default: cactusColor = Color.GREEN; break;
        }

        // FPS timing
        int FPS = GameSettings.getFPSValue();
        switch (FPS) {
            case 30: time = 33; break;
            case 60: time = 16; break;
            case 120: time = 8; break;
            case 240: time = 4; break;
        }

        timer = new Timer(time, this);
        timer.start();
    }

    // ---------- Entry point ----------
    public static void start(int level) {
        GamePanel panel = new GamePanel(level);
        JFrame frame = new JFrame("Level " + level);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setResizable(false);
        frame.add(panel);
        panel.setFrame(frame);
        frame.setVisible(true);
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(StartScreen.class.getResource("/icon.png"))
        );
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    // ---------- Rendering ----------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Repeat the background seamlessly as the camera scrolls
        if (backgroundImage != null) {
            int bgX = (int) (-cameraX * 0.5) % bgWidth; // parallax + looping
            if (bgX > 0) bgX -= bgWidth; // ensure smooth wrap

            for (int x = bgX; x < getWidth(); x += bgWidth) {
                // draw image, aligning ground to floor (y=680)
                g.drawImage(backgroundImage, x, 680 - (bgHeight - 400), bgWidth, bgHeight, this);
            }
        } else {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Platforms
        g.setColor(floorColor);
        for (int[] p : platforms) {
            g.fillRect(p[0] - cameraX, p[1], p[2], p[3]);
        }
        // cactus items
        g.setColor(cactusColor);
        for (int[] p : cactus) {
            g.fillRect(p[0] - cameraX, p[1], p[2], p[3]);
        }

        // Win blocks
        g.setColor(Color.YELLOW);
        for (int[] p : wincon) {
            g.fillRect(p[0] - cameraX, p[1], p[2], p[3]);
        }

        // Player
        g.setColor(playerColor);
        g.fillRect(playerX - cameraX, playerY, playerWidth, playerHeight);

        // Messages
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("YOU WIN!", getWidth() / 2 - 150, getHeight() / 2);

            timer.stop();
            gameOver = false;

            new javax.swing.Timer(1000, e2 -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                LevelSelector.open(new JFrame());
            }) {{
                setRepeats(false);
                start();
            }};
            return;
        }

        if (lost) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("LOST!", getWidth() / 2 - 150, getHeight() / 2);

            timer.stop();
            lost = false;

            new javax.swing.Timer(1000, e2 -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                GamePanel.start(currentlevel);
            }) {{
                setRepeats(false);
                start();
            }};
            return;
        }
    }

        // ---------- Game Loop ----------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

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

        // Collision detection
        onGround = false;
        Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
        for (int[] p : platforms) {
            Rectangle platRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (playerRect.intersects(platRect)) {
                if (playerY + playerHeight - velocityY <= p[1]) {
                    playerY = p[1] - playerHeight;
                    velocityY = 0;
                    onGround = true;
                }
            }
        }
        Rectangle platRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);
        for (int[] p : cactus) {
            Rectangle cactusRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (cactusRect.intersects(platRect)) {
                lost = true;
            }
        }
        // Lost condition
        if (playerY > 680) {
            lost = true;
        }

        // Win condition
        for (int[] p : wincon) {
            Rectangle winRect = new Rectangle(p[0], p[1], p[2], p[3]);
            if (playerRect.intersects(winRect)) {
                gameOver = true;
            }
        }

        // Camera follows player
        if (playerX > 1080) {
            cameraX = playerX - 1080;
        }

        repaint();
    }

    // ---------- Input ----------
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            if (frame != null) frame.dispose();
            LevelSelector.open(frame);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
