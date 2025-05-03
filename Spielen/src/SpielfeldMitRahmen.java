import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


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
                    JPanel leerPanel = new JPanel();
                    leerPanel.setBackground(Color.GRAY);
                    leerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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

        String wert = board[i][j].getText().strip();
        if (wert.isEmpty()) return;

        int ri = 0, rj = 0;

        if (i == 0) { ri = 1; }                 // nach unten
        else if (i == SIZE - 1) { ri = -1; }    // nach oben
        else if (j == 0) { rj = 1; }            // nach rechts
        else if (j == SIZE - 1) { rj = -1; }    // nach links
        else return;

        int zi = i + ri;
        int zj = j + rj;

        // Prüfe, ob wir Platz schaffen können
        if (kannSchieben(zi, zj, ri, rj)) {
            schiebeKette(zi, zj, ri, rj);
            board[zi][zj].setText(wert);
            board[i][j].setText(String.valueOf((int)(Math.random() * 3 + 1)));
            board[i][j].setBackground(Color.LIGHT_GRAY);
            findeUndMergeGruppen();
        } else {
            board[i][j].setBackground(Color.RED);
        }
    }

 // Prüft rekursiv, ob ein Feld leer ist oder frei gemacht werden kann
    public static boolean kannSchieben(int i, int j, int ri, int rj) {
        if (i <= 0 || i >= SIZE - 1 || j <= 0 || j >= SIZE - 1) return false;
        if (board[i][j].getText().strip().isEmpty()) return true;

        return kannSchieben(i + ri, j + rj, ri, rj);
    }

    // Verschiebt alle Zahlen in Richtung ri/rj um 1 Feld
    public static void schiebeKette(int i, int j, int ri, int rj) {
        if (board[i][j].getText().strip().isEmpty()) return;

        int ni = i + ri;
        int nj = j + rj;

        schiebeKette(ni, nj, ri, rj); // erst weiter hinten Platz schaffen
        board[ni][nj].setText(board[i][j].getText());
        board[i][j].setText("");
    }


    public static void findeUndMergeGruppen() {
        boolean[][] besucht = new boolean[SIZE][SIZE];

        for (int i = 1; i < SIZE - 1; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
                if (!besucht[i][j]) {
                    String wert = board[i][j].getText().strip();
                    if (!wert.isEmpty()) {
                        List<Point> gruppe = findeGruppe(i, j, wert, besucht);
                        if (gruppe.size() >= 3) {
                            int summe = gruppe.size() * Integer.parseInt(wert);
                            Point p0 = gruppe.get(0);
                            board[p0.x][p0.y].setText(String.valueOf(summe));
                            for (int k = 1; k < gruppe.size(); k++) {
                                Point p = gruppe.get(k);
                                board[p.x][p.y].setText("");
                            }
                        }
                    }
                }
            }
        }
    }

    public static List<Point> findeGruppe(int i, int j, String wert, boolean[][] besucht) {
        List<Point> gruppe = new ArrayList<>();
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(i, j));
        besucht[i][j] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            gruppe.add(p);

            int[][] richtungen = {{0,1}, {1,0}, {0,-1}, {-1,0}};

            for (int[] r : richtungen) {
                int ni = p.x + r[0];
                int nj = p.y + r[1];

                if (ni > 0 && ni < SIZE - 1 && nj > 0 && nj < SIZE - 1) {
                    if (!besucht[ni][nj]) {
                        String nachbar = board[ni][nj].getText().strip();
                        if (nachbar.equals(wert)) {
                            besucht[ni][nj] = true;
                            queue.add(new Point(ni, nj));
                        }
                    }
                }
            }
        }

        return gruppe;
    }
}
