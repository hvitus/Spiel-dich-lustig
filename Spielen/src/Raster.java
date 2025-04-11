import javax.swing.*;
import java.awt.*;

public class Raster extends JPanel {
    private final int cellSize;

    public Raster(int cellSize) {
        this.cellSize = cellSize;
        setPreferredSize(new Dimension(cellSize * 5, cellSize * 5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int rows = 5;
        int cols = 5;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    public static void main(String[] args) {
        int buttonSize = 60;
        int rasterSize = buttonSize * 5;

        JFrame frame = new JFrame("Raster");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

       
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setPreferredSize(new Dimension(rasterSize + buttonSize * 2, buttonSize));
        for (int i = 0; i < 7; i++) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = i;
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(buttonSize, buttonSize));
            if (i == 0 || i == 6) {
                b.setVisible(false); 
                topPanel.add(Box.createRigidArea(new Dimension(buttonSize, buttonSize)), gbc);
            } else {
                topPanel.add(b, gbc);
            }
        }

       
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setPreferredSize(new Dimension(rasterSize + buttonSize * 2, buttonSize));
        for (int i = 0; i < 7; i++) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = i;
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(buttonSize, buttonSize));
            if (i == 0 || i == 6) {
                bottomPanel.add(Box.createRigidArea(new Dimension(buttonSize, buttonSize)), gbc);
            } else {
                bottomPanel.add(b, gbc);
            }
        }

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(buttonSize, rasterSize + buttonSize * 2));
        for (int i = 0; i < 7; i++) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = i;
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(buttonSize, buttonSize));
            if (i == 0 || i == 6) {
                leftPanel.add(Box.createRigidArea(new Dimension(buttonSize, buttonSize)), gbc);
            } else {
                leftPanel.add(b, gbc);
            }
        }

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setPreferredSize(new Dimension(buttonSize, rasterSize + buttonSize * 2));
        for (int i = 0; i < 7; i++) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = i;
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(buttonSize, buttonSize));
            if (i == 0 || i == 6) {
                rightPanel.add(Box.createRigidArea(new Dimension(buttonSize, buttonSize)), gbc);
            } else {
                rightPanel.add(b, gbc);
            }
        }

        // Zentrale Rasterfläche
        JPanel centerPanel = new Raster(buttonSize);

        // Zusammenfügen
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(centerPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}
