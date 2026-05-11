import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Pathfinding Lab");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        GamePanel gamePanel = new GamePanel();

        JScrollPane scrollPane = new JScrollPane(gamePanel);
        scrollPane.setPreferredSize(new Dimension(800, 600)); // Standard window size

        // Hide scrollbars for the "Game" feel
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        window.add(scrollPane, BorderLayout.CENTER);
        window.add(new ControlPanel(gamePanel), BorderLayout.EAST);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // --- NEW: THE AUTO-SCROLL TIMER ---
        // This keeps the scrollpane viewport centered on the player
        Timer scrollTimer = new Timer(16, e -> {
            int playerX = gamePanel.player.col * gamePanel.TILE_SIZE;
            int playerY = gamePanel.player.row * gamePanel.TILE_SIZE;

            // Calculate center
            int centerX = playerX - (scrollPane.getViewport().getWidth() / 2);
            int centerY = playerY - (scrollPane.getViewport().getHeight() / 2);

            // Set the scroll position
            scrollPane.getViewport().setViewPosition(new Point(
                    Math.max(0, Math.min(centerX, gamePanel.getWidth() - scrollPane.getViewport().getWidth())),
                    Math.max(0, Math.min(centerY, gamePanel.getHeight() - scrollPane.getViewport().getHeight()))
            ));
        });
        scrollTimer.start();
    }
}