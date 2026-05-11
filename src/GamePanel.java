import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class GamePanel extends JPanel {
    // 1. Grid Settings (Optimized for 1920x1760 image at 32px tiles)
    public final int COLS = 60;
    public final int ROWS = 55;
    public final int TILE_SIZE = 32;

    public int[][] grid = new int[ROWS][COLS];
    public String selectedTool = "Wall";
    public Entity player = new Entity(2, 2, Color.WHITE);
    public Entity npc = new Entity(3, 2, Color.CYAN);

    public boolean isRunning = false;
    public boolean npcActive = true;
    public boolean showDebugGrid = true;

    private ImageIcon background;
    private ImageIcon playerGif;
    private ImageIcon npcGif;

    private long lastMoveTime = 0;
    private final long MOVE_COOLDOWN = 150; // Adjusted for 32px movement speed

    Pathfinder pathfinder;
    ArrayList<Node> path = new ArrayList<>();
    Timer movementTimer;

    public GamePanel() {
        // Set dimensions to match high-res image
        setPreferredSize(new Dimension(1920, 1760));
        setDoubleBuffered(true);

        pathfinder = new Pathfinder(this);
        setupKeyBindings();

        // Load Sprites and Background
        try {
            background = new ImageIcon(getClass().getResource("/res/Harvest BG.png"));
            playerGif = new ImageIcon(getClass().getResource("/res/1.1run.gif"));
            npcGif = new ImageIcon(getClass().getResource("/res/2.1run.gif"));
        } catch (Exception e) {
            System.out.println("Resource Error: Check /res/ folder!");
        }

        // Initialize Level from File or Default
        loadPreBuiltLevel();

        // Mouse Listener for Editor Painting
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int c = e.getX() / TILE_SIZE;
                int r = e.getY() / TILE_SIZE;
                if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
                    applyTool(r, c);
                }
            }
        });

        // NPC Movement logic (Runs at fixed interval)
        movementTimer = new Timer(500, e -> moveNPC());
        movementTimer.start();

        // Global Repaint Timer (60 FPS)
        new Timer(16, e -> repaint()).start();
    }

    private void applyTool(int r, int c) {
        switch (selectedTool) {
            case "Wall" -> grid[r][c] = (grid[r][c] == 1) ? 0 : 1;
            case "Player" -> { player.row = r; player.col = c; }
            case "NPC" -> { npc.row = r; npc.col = c; npcActive = true; }
        }
        updatePath();
    }

    public void updatePath() {
        pathfinder.setNodes(npc.row, npc.col, player.row, player.col);
        path = pathfinder.getPath();
    }

    private void moveNPC() {
        if (!isRunning || !npcActive) return;
        updatePath();

        if (path != null && !path.isEmpty()) {
            Node nextStep = path.get(0);

            // Update direction for sprite flipping
            if (nextStep.col < npc.col) npc.dir = "LEFT";
            else if (nextStep.col > npc.col) npc.dir = "RIGHT";

            npc.row = nextStep.row;
            npc.col = nextStep.col;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // 1. Draw Environment
        if (background != null) {
            g2.drawImage(background.getImage(), 0, 0, 1920, 1760, null);
        }

        // 2. Draw Editor/Collision Boxes (Transparent Black)
        if (showDebugGrid) {
            g2.setColor(new Color(0, 0, 0, 150));
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (grid[r][c] == 1) {
                        g2.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        // 3. Draw Pathfinding "Ghost" Path
        if (path != null) {
            g2.setColor(new Color(255, 255, 0, 80));
            for (Node n : path) {
                g2.fillRect(n.col * TILE_SIZE, n.row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // 4. Draw Characters (Handling Sprite size and flipping)
        int spriteSize = (int)(TILE_SIZE * 2.0); // 64px character on 32px grid
        int offset = (spriteSize - TILE_SIZE) / 2;

        if (playerGif != null) {
            drawEntity(g2, player, playerGif, spriteSize, offset);
        }
        if (npcActive && npcGif != null) {
            drawEntity(g2, npc, npcGif, spriteSize, offset);
        }
    }

    private void drawEntity(Graphics2D g2, Entity e, ImageIcon icon, int size, int offset) {
        int x = e.col * TILE_SIZE - offset;
        int y = e.row * TILE_SIZE - offset;

        if (e.dir.equals("LEFT")) {
            // Flip image horizontally
            g2.drawImage(icon.getImage(), x + size, y, -size, size, null);
        } else {
            g2.drawImage(icon.getImage(), x, y, size, size, null);
        }
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Cardinal Directions
        im.put(KeyStroke.getKeyStroke("W"), "moveUp");
        im.put(KeyStroke.getKeyStroke("S"), "moveDown");
        im.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        im.put(KeyStroke.getKeyStroke("D"), "moveRight");

        // Diagonal Directions
        im.put(KeyStroke.getKeyStroke("Q"), "moveNW");
        im.put(KeyStroke.getKeyStroke("E"), "moveNE");
        im.put(KeyStroke.getKeyStroke("Z"), "moveSW");
        im.put(KeyStroke.getKeyStroke("C"), "moveSE");

        am.put("moveUp", new MoveAction(0, -1, "UP"));
        am.put("moveDown", new MoveAction(0, 1, "DOWN"));
        am.put("moveLeft", new MoveAction(-1, 0, "LEFT"));
        am.put("moveRight", new MoveAction(1, 0, "RIGHT"));
        am.put("moveNW", new MoveAction(-1, -1, "LEFT"));
        am.put("moveNE", new MoveAction(1, -1, "RIGHT"));
        am.put("moveSW", new MoveAction(-1, 1, "LEFT"));
        am.put("moveSE", new MoveAction(1, 1, "RIGHT"));
    }

    private class MoveAction extends AbstractAction {
        int dx, dy;
        String dir;
        MoveAction(int dx, int dy, String dir) { this.dx = dx; this.dy = dy; this.dir = dir; }

        @Override
        public void actionPerformed(ActionEvent e) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMoveTime < MOVE_COOLDOWN) return;

            int nextR = player.row + dy;
            int nextC = player.col + dx;

            if (nextR >= 0 && nextR < ROWS && nextC >= 0 && nextC < COLS) {
                if (grid[nextR][nextC] != 1) {
                    // Prevent corner cutting
                    if (dx != 0 && dy != 0) {
                        if (grid[player.row][nextC] == 1 && grid[nextR][player.col] == 1) return;
                    }
                    player.row = nextR;
                    player.col = nextC;
                    player.dir = dir;
                    lastMoveTime = currentTime;
                    updatePath();
                }
            }
        }
    }

    public void saveMap() {
        try (PrintWriter out = new PrintWriter(new FileWriter("mapdata.txt"))) {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) out.print(grid[r][c] + " ");
                out.println();
            }
            JOptionPane.showMessageDialog(this, "Map Saved!");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void loadPreBuiltLevel() {
        File file = new File("mapdata.txt");
        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                for (int r = 0; r < ROWS; r++) {
                    for (int c = 0; c < COLS; c++) {
                        if (sc.hasNextInt()) grid[r][c] = sc.nextInt();
                    }
                }
            } catch (FileNotFoundException e) { e.printStackTrace(); }
        }
        updatePath();
    }
}