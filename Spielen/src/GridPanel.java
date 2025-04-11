// GUI-Panel für innere Felder

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class GridPanel extends JPanel {
    public GridPanel(String text) {
        setBackground(new Color(255, 255, 255)); // weiss
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
