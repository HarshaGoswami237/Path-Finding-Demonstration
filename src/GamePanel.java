import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class GamePanel extends JPanel {
    public final int ROWS = 25, COLS = 25, TILE_SIZE = 64;
    public int[][] grid = new int[ROWS][COLS];

    public String selectedTool = "Wall";
    public Entity player = new Entity(0, 0, Color.WHITE);
    public Entity npc = new Entity(9, 9, Color.CYAN);
    public boolean isRunning = false;
    public boolean npcActive = true;

    // Zoom and Throttling
    private double zoom = 1.0;
    private long lastMoveTime = 0;
    private final long MOVE_COOLDOWN = 150;

    Pathfinder pathfinder;
    ArrayList<Node> path = new ArrayList<>();
    Timer movementTimer;

    public GamePanel() {
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setBackground(new Color(34, 139, 34));
        pathfinder = new Pathfinder(this);
        setupKeyBindings();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Adjust coordinates based on zoom
                int r = (int) (e.getY() / (TILE_SIZE * zoom));
                int c = (int) (e.getX() / (TILE_SIZE * zoom));
                if (r >= 0 && r < ROWS && c >= 0 && c < COLS) {
                    applyTool(r, c);
                }
            }
        });

        // Add MouseWheelListener for Zooming (Ctrl + Wheel)
        addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                if (e.getPreciseWheelRotation() < 0) zoom += 0.1;
                else zoom = Math.max(0.1, zoom - 0.1);
                repaint();
                e.consume();
            }
        });

        movementTimer = new Timer(500, e -> moveNPC());
        movementTimer.start();
    }

    private void applyTool(int r, int c) {
        switch (selectedTool) {
            case "Wall" -> grid[r][c] = (grid[r][c] == 1) ? 0 : 1;
            case "Player" -> { player.row = r; player.col = c; }
            case "NPC" -> { npc.row = r; npc.col = c; npcActive = true; }
        }
        updatePath();
        repaint();
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
            if (nextStep.row != player.row || nextStep.col != player.col) {
                npc.row = nextStep.row;
                npc.col = nextStep.col;
            }
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Apply Zoom Transform
        g2.scale(zoom, zoom);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == 1) g2.setColor(new Color(50, 50, 50));
                else g2.setColor(new Color(60, 179, 113));

                g2.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.drawRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        if (path != null) {
            g2.setColor(new Color(255, 255, 0, 100));
            for (Node n : path) {
                g2.fillRect(n.col * TILE_SIZE + 20, n.row * TILE_SIZE + 20, 24, 24);
            }
        }

        player.draw(g2, TILE_SIZE);
        if (npcActive) npc.draw(g2, TILE_SIZE);
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke("W"), "moveUp");
        im.put(KeyStroke.getKeyStroke("S"), "moveDown");
        im.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        im.put(KeyStroke.getKeyStroke("D"), "moveRight");
        am.put("moveUp", new MoveAction(0, -1));
        am.put("moveDown", new MoveAction(0, 1));
        am.put("moveLeft", new MoveAction(-1, 0));
        am.put("moveRight", new MoveAction(1, 0));
    }

    public void scrollToPlayer() {
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
        if (scrollPane != null) {
            int playerX = (int)(player.col * TILE_SIZE * zoom);
            int playerY = (int)(player.row * TILE_SIZE * zoom);
            int viewW = scrollPane.getViewport().getWidth();
            int viewH = scrollPane.getViewport().getHeight();
            int scrollX = playerX - (viewW / 2) + (int)(TILE_SIZE * zoom / 2);
            int scrollY = playerY - (viewH / 2) + (int)(TILE_SIZE * zoom / 2);
            scrollX = Math.max(0, Math.min(scrollX, (int)(getPreferredSize().width * zoom) - viewW));
            scrollY = Math.max(0, Math.min(scrollY, (int)(getPreferredSize().height * zoom) - viewH));
            scrollPane.getViewport().setViewPosition(new Point(scrollX, scrollY));
        }
    }

    private class MoveAction extends AbstractAction {
        int dx, dy;
        MoveAction(int dx, int dy) { this.dx = dx; this.dy = dy; }
        @Override
        public void actionPerformed(ActionEvent e) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastMoveTime < MOVE_COOLDOWN) return;
            lastMoveTime = currentTime;
            int nextR = player.row + dy;
            int nextC = player.col + dx;
            if (nextR >= 0 && nextR < ROWS && nextC >= 0 && nextC < COLS && grid[nextR][nextC] != 1) {
                player.row = nextR;
                player.col = nextC;
                updatePath();
                scrollToPlayer();
                repaint();
            }
        }
    }
}