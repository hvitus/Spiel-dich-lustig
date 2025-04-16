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

        boolean verschoben = false;

        if (i == 0) {
            List<String> werte = new ArrayList<>();
            for (int k = 1; k < SIZE - 1; k++) {
                String t = board[k][j].getText().strip();
                if (!t.isEmpty()) werte.add(t);
            }

            if (werte.size() >= SIZE - 2) {
                board[i][j].setBackground(Color.RED);
                return;
            }

            werte.add(0, wert);

            for (int k = 1; k < SIZE - 1; k++) {
                if (k - 1 < werte.size()) {
                    board[k][j].setText(werte.get(k - 1));
                } else {
                    board[k][j].setText("");
                }
            }

            verschoben = true;
        } else if (i == SIZE - 1) {
            List<String> werte = new ArrayList<>();
            for (int k = SIZE - 2; k > 0; k--) {
                String t = board[k][j].getText().strip();
                if (!t.isEmpty()) werte.add(t);
            }

            if (werte.size() >= SIZE - 2) {
                board[i][j].setBackground(Color.RED);
                return;
            }

            werte.add(0, wert);

            for (int k = SIZE - 2, idx = 0; k > 0; k--, idx++) {
                if (idx < werte.size()) {
                    board[k][j].setText(werte.get(idx));
                } else {
                    board[k][j].setText("");
                }
            }

            verschoben = true;
        } else if (j == 0) {
            List<String> werte = new ArrayList<>();
            for (int k = 1; k < SIZE - 1; k++) {
                String t = board[i][k].getText().strip();
                if (!t.isEmpty()) werte.add(t);
            }

            if (werte.size() >= SIZE - 2) {
                board[i][j].setBackground(Color.RED);
                return;
            }

            werte.add(0, wert);

            for (int k = 1; k < SIZE - 1; k++) {
                if (k - 1 < werte.size()) {
                    board[i][k].setText(werte.get(k - 1));
                } else {
                    board[i][k].setText("");
                }
            }

            verschoben = true;
        } else if (j == SIZE - 1) {
            List<String> werte = new ArrayList<>();
            for (int k = SIZE - 2; k > 0; k--) {
                String t = board[i][k].getText().strip();
                if (!t.isEmpty()) werte.add(t);
            }

            if (werte.size() >= SIZE - 2) {
                board[i][j].setBackground(Color.RED);
                return;
            }

            werte.add(0, wert);

            for (int k = SIZE - 2, idx = 0; k > 0; k--, idx++) {
                if (idx < werte.size()) {
                    board[i][k].setText(werte.get(idx));
                } else {
                    board[i][k].setText("");
                }
            }

            verschoben = true;
        }

        if (verschoben) {
            board[i][j].setText(String.valueOf((int)(Math.random() * 3 + 1)));
            board[i][j].setBackground(Color.LIGHT_GRAY);
            findeUndMergeGruppen();
        }
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
