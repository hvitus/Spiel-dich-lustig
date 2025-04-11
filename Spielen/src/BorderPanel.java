import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class BorderPanel extends JPanel {
    public BorderPanel(String text, String direction, int row, int col) {
        setBackground(new Color(180, 180, 180)); // grau
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int nummer = -1;
                switch (direction) {
                    case "oben":
                    case "unten":
                        nummer = col - 1; break; 
                    case "links":
                    case "rechts":
                        nummer = row - 1; break; 
                }
                System.out.println("Randfeld geklickt: " + direction + " " + (nummer + 1));
            }
        });
    }
}