import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    public ControlPanel(GamePanel gamePanel) {
        setPreferredSize(new Dimension(180, 0));
        // 7 Rows: Header, Wall, Player, NPC, Save, Toggle, Start
        setLayout(new GridLayout(7, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("EDITOR MODE");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        add(label);

        String[] options = {"Wall", "Player", "NPC"};
        for (String opt : options) {
            JButton btn = new JButton(opt);
            btn.addActionListener(e -> gamePanel.selectedTool = opt);
            add(btn);
        }

        JButton saveBtn = new JButton("SAVE DATA");
        saveBtn.addActionListener(e -> gamePanel.saveMap());
        add(saveBtn);

        JButton toggleBtn = new JButton("HIDE/SHOW GRID");
        toggleBtn.addActionListener(e -> {
            gamePanel.showDebugGrid = !gamePanel.showDebugGrid;
            gamePanel.repaint();
        });
        add(toggleBtn);

        JButton startBtn = new JButton("START");
        startBtn.setBackground(Color.GREEN);
        startBtn.setOpaque(true);
        startBtn.setBorderPainted(false);
        startBtn.addActionListener(e -> {
            gamePanel.isRunning = !gamePanel.isRunning;
            startBtn.setText(gamePanel.isRunning ? "STOP SIM" : "START SIM");
            startBtn.setBackground(gamePanel.isRunning ? Color.RED : Color.GREEN);
        });
        add(startBtn);

        // RESET BUTTON REMOVED FOR SAFETY
    }
}