import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Startbildschirm  extends Spiellogik{
  
	public static void Startbildschirm() {//beschreibt den Starbildschirm
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setBackground(Color.BLACK);

        JLabel titelLabel = new JLabel("Binary Fusion₂", SwingConstants.CENTER);
        titelLabel.setFont(new Font("Arial", Font.BOLD, 41));
        titelLabel.setForeground(Color.BLUE);
        titelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startPanel.add(Box.createVerticalStrut(80));
        startPanel.add(titelLabel);

        // Animation für die Schriftgröße
        Timer timer = new Timer(50, new ActionListener() {
            private int fontSize = 45;
            private boolean growing = true;

            
            public void actionPerformed(ActionEvent e) {
                if (growing) {
                    fontSize += 2;
                    if (fontSize >= 72) {
                        growing = false;
                    }
                } else {
                    fontSize -= 2;
                    if (fontSize <= 48) {
                        growing = true;
                    }
                }
                titelLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
            }
        });
        timer.start();

        JButton startButton = new JButton("Start");//Start Knopf
        startButton.setFont(new Font("Arial", Font.BOLD, 32));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(50, 200, 50));
        startButton.setForeground(Color.BLACK);

        // Button Hover Effekt: Vergrößern beim Hover
        startButton.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
                startButton.setFont(new Font("Arial", Font.BOLD, 36));  // Vergrößern
            }

            
            public void mouseExited(MouseEvent e) {
                startButton.setFont(new Font("Arial", Font.BOLD, 32));  // Zurücksetzen
            }
        });

        startButton.addActionListener(e -> {// startet Spiel wenn man auf Startknopf drückt
            initialisiereSpielfeld();
            zeichneSpielfeld();
            spieleHintergrundmusik("background.wav");// spielt Hintergrundmusik ab
        });

        startPanel.add(Box.createVerticalStrut(50));
        startPanel.add(startButton);


        frame.getContentPane().add(startPanel, BorderLayout.CENTER);
        frame.setSize(700, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
