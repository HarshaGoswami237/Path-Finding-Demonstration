import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Pathfinding Lab");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        // 1. Create the Panel
        GamePanel gamePanel = new GamePanel();

        // 2. Wrap it in a ScrollPane
        JScrollPane scrollPane = new JScrollPane(gamePanel);
        scrollPane.setPreferredSize(new Dimension(640, 640)); // Fixed Viewport size

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        // OPTIONAL: Make scrolling feel faster
        scrollPane.getVerticalScrollBar().setUnitIncrement(64);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(64);

        // 3. Add ONLY the scrollPane to the center
        window.add(scrollPane, BorderLayout.CENTER);

        // 4. Add the control panel
        ControlPanel controlPanel = new ControlPanel(gamePanel);
        window.add(controlPanel, BorderLayout.EAST);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}