import javax.swing.*;
import java.awt.*;

// Datenklasse für Spielfeldzellen
class Panel {
    String type;       // "grid" oder "border"
    String value;      // "x", " ", Zahl etc.
    String direction;  // für border: "top", "left", ...

    public Panel(String type, String value, String direction) {
        this.type = type;
        this.value = value;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return value;
    }
}

// GUI-Panel für innere Felder
class GridPanel extends JPanel {
    public GridPanel(String text) {
        setBackground(new Color(255, 255, 255)); // weiss
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
    }
}

// GUI-Panel für Randfelder
class BorderPanel extends JPanel {
    public BorderPanel(String text) {
        setBackground(new Color(180, 180, 180)); // grau
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());
        add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
    }
}

public class SpielfeldMitRahmen extends JFrame {

    static final int SIZE = 7;
    static Panel[][] board = new Panel[SIZE][SIZE];

    public SpielfeldMitRahmen() {
        setTitle("5x5-Spielfeld mit Rand");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(SIZE, SIZE));
        setSize(600, 600);

        initialisiereSpielfeld();
        zeigeGUI();

        setVisible(true);
    }

    // Spielfeld-Logik initialisieren (ohne Ecken)
    static void initialisiereSpielfeld() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                // Ecken leer
                if ((i == 0 || i == SIZE - 1) && (j == 0 || j == SIZE - 1)) {
                    board[i][j] = null;
                    continue;
                }

                // Rahmenfelder
                if (i == 0) board[i][j] = new Panel("border", " ", "top");
                else if (i == SIZE - 1) board[i][j] = new Panel("border", " ", "bottom");
                else if (j == 0) board[i][j] = new Panel("border", " ", "left");
                else if (j == SIZE - 1) board[i][j] = new Panel("border", " ", "right");

                // Innere Gitterfelder
                else board[i][j] = new Panel("grid", " ", null);
            }
        }
    }

    // GUI aus dem Logik-Array aufbauen
    void zeigeGUI() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Panel feld = board[i][j];

                if (feld == null) {
                    add(new JPanel()); // leere Ecke
                } else if (feld.type.equals("grid")) {
                    add(new GridPanel(feld.value));
                } else if (feld.type.equals("border")) {
                    add(new BorderPanel(feld.value));
                }
            }
        }
    }

    public static void main(String[] args) {
        new SpielfeldMitRahmen();
    }
}
