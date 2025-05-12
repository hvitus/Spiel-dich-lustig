import java.awt.*;
import javax.swing.*;

public class Startbildschirm extends Spiellogik { // Startbildschirm zum Auswählen ob Binär oder ohne Binär

    public static void zeigeStartbildschirm() {
        JFrame startFrame = new JFrame("Binary Merge");
        startFrame.setSize(300, 250);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setLayout(new GridLayout(4, 1)); // Jetzt 4 Zeilen: Titel + 3 Buttons

        JLabel titel = new JLabel("Wähle dein Level", SwingConstants.CENTER);
        titel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton binärButton = new JButton("Level mit Binärzahlen");
        JButton dezimalButton = new JButton("Nur Dezimalzahlen");
        JButton anleitungButton = new JButton("Anleitung");

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

        anleitungButton.addActionListener(e -> {
            zeigeAnleitungFenster(startFrame);
        });

        startFrame.add(titel);
        startFrame.add(binärButton);
        startFrame.add(dezimalButton);
        startFrame.add(anleitungButton);
        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }

    public static void zeigeAnleitungFenster(JFrame parentFrame) {
        JFrame bildFrame = new JFrame("Anleitung");
        bildFrame.setSize(600, 800);
        bildFrame.setLayout(new BorderLayout());

        // Bilder als Icons laden
        ImageIcon bild1 = new ImageIcon("anleitung1.png"); // erstes A4-Bild
        ImageIcon bild2 = new ImageIcon("anleitung2.png"); // zweites A4-Bild

        // Funktion zum Skalieren der Bilder
        ImageIcon skaliertesBild1 = skalierenBild(bild1, bildFrame.getWidth(), bildFrame.getHeight());
        ImageIcon skaliertesBild2 = skalierenBild(bild2, bildFrame.getWidth(), bildFrame.getHeight());

        // Label für Bild1 und Bild2
        JLabel label = new JLabel(skaliertesBild1);
        JScrollPane scrollPane = new JScrollPane(label); // Zum Scrollen bei großen Bildern

        // Navigationsbuttons
        JButton nächsteButton = new JButton("Nächste");
        JButton zurückButton = new JButton("Zurück");

        // Zurück zum Startbildschirm
        JButton zurückZumStartButton = new JButton("Zurück zum Startbildschirm");
        zurückZumStartButton.addActionListener(e -> {
            bildFrame.dispose();
            parentFrame.setVisible(true); // Zurück zum Startbildschirm
        });

        // Aktuelle Bildseite
        int[] aktuelleSeite = {1}; // Die Seite, die momentan angezeigt wird (1 oder 2)

        // Aktion für „Nächste“: Wechseln zu Bild 2
        nächsteButton.addActionListener(e -> {
            if (aktuelleSeite[0] == 1) {
                label.setIcon(skaliertesBild2);
                aktuelleSeite[0] = 2;
            }
        });

        // Aktion für „Zurück“: Wechseln zu Bild 1
        zurückButton.addActionListener(e -> {
            if (aktuelleSeite[0] == 2) {
                label.setIcon(skaliertesBild1);
                aktuelleSeite[0] = 1;
            }
        });

        // Buttons hinzufügen
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(zurückZumStartButton, BorderLayout.NORTH);
        buttonPanel.add(zurückButton, BorderLayout.WEST);
        buttonPanel.add(nächsteButton, BorderLayout.EAST);

        // Layout hinzufügen
        bildFrame.add(scrollPane, BorderLayout.CENTER);
        bildFrame.add(buttonPanel, BorderLayout.SOUTH);
        parentFrame.setVisible(false);
        bildFrame.setLocationRelativeTo(null);
        bildFrame.setVisible(true);
    }

    // Hilfsmethode zum Skalieren eines Bildes
    private static ImageIcon skalierenBild(ImageIcon original, int breite, int höhe) {
        Image originalImage = original.getImage();
        Image skaliertesImage = originalImage.getScaledInstance(breite, höhe, Image.SCALE_SMOOTH);
        return new ImageIcon(skaliertesImage);
    }

    public static void starteSpiel() {
        ladeHighscores();
        initialisiereSpielfeld();
        zeichneSpielfeld();
        spieleHintergrundmusik("background.wav"); // falls du Musik verwendest
    }
}
