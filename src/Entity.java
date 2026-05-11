import java.awt.*;

public class Entity {
    public int row, col;
    public Color color;
    public String dir = "DOWN"; // Default direction

    public Entity(int r, int c, Color color) {
        this.row = r;
        this.col = c;
        this.color = color;
    }

    public void draw(Graphics2D g2, int tileSize) {
        g2.setColor(color);
        // Using a filled round rect for a "character" look
        g2.fillRoundRect(col * tileSize + 8, row * tileSize + 8, tileSize - 16, tileSize - 16, 15, 15);
    }
}