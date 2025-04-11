import javax.swing.*;
import java.awt.*;

public class Raster extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int zeilen = 5;
        int spalten = 5;
        int feldBreite = getWidth() / spalten;
        int feldHoehe = getHeight() / zeilen;

        for (int i = 0; i < zeilen; i++) {
            for (int j = 0; j < spalten; j++) {
                int x = j * feldBreite;
                int y = i * feldHoehe;
                g.drawRect(x, y, feldBreite, feldHoehe);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("5x5-Raster");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        Raster raster = new Raster();
        frame.add(raster);

        frame.setVisible(true);
    }
}
