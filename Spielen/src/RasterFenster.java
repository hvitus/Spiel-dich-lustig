import javax.swing.*;
import java.awt.*;

public class RasterFenster {
    public static void main(String[] args) {
        JFrame frame = new JFrame("5x5 Raster");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(5, 5));

        for (int i = 1; i <= 25; i++) {
            frame.add(new JButton(String.valueOf(i)));
        }

        frame.setVisible(true);
    }
} 