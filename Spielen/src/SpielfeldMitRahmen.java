import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpielfeldMitRahmen {
    static final int SIZE = 7;
    static Panel[][] board = new Panel[SIZE][SIZE];
    static JFrame frame = new JFrame("Spielfeld mit Rahmen");

    public static void main(String[] args) {
        initialisiereSpielfeld();
        zeichneSpielfeld();
    }

    public static void initialisiereSpielfeld() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i == 0 || i == SIZE - 1) && (j == 0 || j == SIZE - 1)) {
                    board[i][j] = null; // Ecken leer
                } else if (i == 0) {
                    board[i][j] = new Panel("border", String.valueOf((int)(Math.random() * 3 + 1)), "oben");
                } else if (i == SIZE - 1) {
                    board[i][j] = new Panel("border", String.valueOf((int)(Math.random() * 3 + 1)), "unten");
                } else if (j == 0) {
                    board[i][j] = new Panel("border", String.valueOf((int)(Math.random() * 3 + 1)), "links");
                } else if (j == SIZE - 1) {
                    board[i][j] = new Panel("border", String.valueOf((int)(Math.random() * 3 + 1)), "rechts");
                } else {
                    board[i][j] = new Panel("grid", " ", null);
                }

                // Nur wenn Panel vorhanden und vom Typ "border"
                if (board[i][j] != null && board[i][j].typ.equals("border")) {
                    int finalI = i;
                    int finalJ = j;
                    board[i][j].addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            verschiebeZahl(finalI, finalJ);
                            frame.repaint();
                        }
                    });
                }
            }
        }
    }

    public static void zeichneSpielfeld() {
        frame.setLayout(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null) {
                    frame.add(board[i][j]);
                } else {
                    // Leere Ecken auch als Panels anzeigen, damit Gitter vollständig ist
                    JPanel leerPanel = new JPanel();
                    leerPanel.setBackground(Color.GRAY);
                    leerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // <— Gitterlinie auch hier
                    frame.add(leerPanel);
                }
            }
        }
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    

    public static void verschiebeZahl(int i, int j) {
        if (board[i][j] == null) return;

        String wert = board[i][j].getText();

        try {
            if (i == 0 && isGrid(i + 1, j)) {
                board[i + 1][j].setText(wert);
            } else if (i == SIZE - 1 && isGrid(i - 1, j)) {
                board[i - 1][j].setText(wert);
            } else if (j == 0 && isGrid(i, j + 1)) {
                board[i][j + 1].setText(wert);
            } else if (j == SIZE - 1 && isGrid(i, j - 1)) {
                board[i][j - 1].setText(wert);
            }

            // Neue Zufallszahl ins Randfeld
            board[i][j].setText(String.valueOf((int)(Math.random() * 3 + 1)));

        } catch (Exception e) {
            System.out.println("Fehler beim Verschieben: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isGrid(int i, int j) {
        return i > 0 && i < SIZE - 1 && j > 0 && j < SIZE - 1;
    }
}
