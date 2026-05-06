import java.awt.*;
import java.awt.image.BufferedImage;

public class Map {
    public final int ROWS = 15, COLS = 15, TILE_SIZE = 48;
    public int[][] grid = new int[ROWS][COLS]; // 0: Grass, 1: Obstacle

    public void draw(Graphics2D g2) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == 1) g2.setColor(new Color(60, 60, 60)); // Stone/Wall
                else g2.setColor(new Color(34, 139, 34)); // Grass Green

                g2.fillRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g2.setColor(new Color(0, 0, 0, 30)); // Subtle grid lines
                g2.drawRect(c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}