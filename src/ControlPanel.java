import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    public ControlPanel(GamePanel gamePanel) {
        setPreferredSize(new Dimension(150, 0));
        setLayout(new GridLayout(6, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("EDITOR MODE");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        String[] options = {"Wall", "Player", "NPC"};
        for (String opt : options) {
            JButton btn = new JButton(opt);
            btn.addActionListener(e -> gamePanel.selectedTool = opt);
            add(btn);
        }

        JButton reset = new JButton("Reset Map");
        reset.addActionListener(e -> {
            for(int r=0; r<10; r++) for(int c=0; c<10; c++) gamePanel.grid[r][c] = 0;
            gamePanel.repaint();
        });

        JButton startBtn = new JButton("START");
        startBtn.setBackground(Color.GREEN);
        startBtn.addActionListener(e -> {
            gamePanel.isRunning = !gamePanel.isRunning;
            startBtn.setText(gamePanel.isRunning ? "STOP SIM" : "START SIM");
            startBtn.setBackground(gamePanel.isRunning ? Color.RED : Color.GREEN);
        });
        add(startBtn);
        add(reset);
    }
}