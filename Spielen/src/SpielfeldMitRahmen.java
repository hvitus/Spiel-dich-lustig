import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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
                if (i == 0) board[i][j] = new Panel("border", " ", "oben");
                else if (i == SIZE - 1) board[i][j] = new Panel("border", " ", "unten");
                else if (j == 0) board[i][j] = new Panel("border", " ", "links");
                else if (j == SIZE - 1) board[i][j] = new Panel("border", " ", "rechts");

                // Innere Gitterfelder
                else board[i][j] = new Panel("grid", " ", null);
            }
        }
    }

    void zeigeGUI() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Panel feld = board[i][j];

                if (feld == null) {
                    add(new JPanel()); 
                } else if (feld.type.equals("grid")) {
                    add(new GridPanel(feld.value));
                } else if (feld.type.equals("border")) {
                    add(new BorderPanel(feld.value, feld.direction, i, j)); 
                }
            }
        }
    }

   
}
