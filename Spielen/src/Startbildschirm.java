import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Startbildschirm extends Spiellogik{
	 public static void zeigeStartbildschirm() {
	        JFrame startFrame = new JFrame("Binary Merge");
	        startFrame.setSize(300, 200);
	        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        startFrame.setLayout(new GridLayout(3, 1));

	        JLabel titel = new JLabel("Wähle dein Level", SwingConstants.CENTER);
	        titel.setFont(new Font("Arial", Font.BOLD, 20));

	        JButton binärButton = new JButton("Level mit Binärzahlen");
	        JButton dezimalButton = new JButton("Nur Dezimalzahlen");

	        binärButton.addActionListener(e -> {
	            binärModus = true;
	            startFrame.dispose();
	            starteSpiel();
	        });

	        dezimalButton.addActionListener(e -> {
	            binärModus = false;
	            startFrame.dispose();
	            starteSpiel();
	        });

	        startFrame.add(titel);
	        startFrame.add(binärButton);
	        startFrame.add(dezimalButton);
	        startFrame.setLocationRelativeTo(null);
	        startFrame.setVisible(true);
	    }
	    public static void starteSpiel() {
	        ladeHighscores();
	        initialisiereSpielfeld();
	        zeichneSpielfeld();
	        spieleHintergrundmusik("background.wav"); // falls du Musik verwendest
	    }
}
