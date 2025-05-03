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
    static JLabel highscoreLabel = new JLabel("Highscore: 1", SwingConstants.CENTER);
    static int highscore = 1;
    static JPanel overlay = new JPanel();

    public static void main(String[] args) {
        frame.setLayout(new BorderLayout());
        highscoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(highscoreLabel, BorderLayout.NORTH);

        JPanel spielfeldPanel = new JPanel(new GridLayout(SIZE, SIZE));
        frame.add(spielfeldPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton restartBtn = new JButton("Restart");
        restartBtn.addActionListener(e -> neustarten());
        buttonPanel.add(restartBtn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        initialisiereSpielfeld(spielfeldPanel);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void initialisiereSpielfeld(JPanel panel) {
        panel.removeAll();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i == 0 || i == SIZE - 1) && (j == 0 || j == SIZE - 1)) {
                    board[i][j] = null; // Ecken leer
                } else if (i > 0 && i < SIZE - 1 && j > 0 && j < SIZE - 1) {
                    board[i][j] = new Panel("grid", " ", null);
                } else {
                    board[i][j] = new Panel("border", "", null);
                }
            }
        }

        // Initialisiere Border-Zellen
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null && board[i][j].typ.equals("border")) {
                    if (i == 0) board[i][j].richtung = "oben";
                    else if (i == SIZE - 1) board[i][j].richtung = "unten";
                    else if (j == 0) board[i][j].richtung = "links";
                    else if (j == SIZE - 1) board[i][j].richtung = "rechts";

                    board[i][j].setText(generiereRandZahl());

                    int finalI = i;
                    int finalJ = j;
                    board[i][j].addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            verschiebeZahl(finalI, finalJ);
                            updateRotStatus();
                            checkGameOver();
                            frame.repaint();
                        }
                    });
                }
            }
        }

        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                panel.add(board[i][j] != null ? board[i][j] : leeresFeld());

        panel.revalidate();
        panel.repaint();
        updateHighscore();
        updateRotStatus();
    }

    public static JPanel leeresFeld() {
        JPanel leer = new JPanel();
        leer.setBackground(Color.GRAY);
        leer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return leer;
    }

    public static void verschiebeZahl(int i, int j) {
        if (board[i][j] == null) return;
        String wert = board[i][j].getText().strip();
        if (wert.isEmpty()) return;

        int ri = 0, rj = 0;
        if (i == 0) ri = 1;
        else if (i == SIZE - 1) ri = -1;
        else if (j == 0) rj = 1;
        else if (j == SIZE - 1) rj = -1;
        else return;

        int zi = i + ri;
        int zj = j + rj;

        if (kannSchieben(zi, zj, ri, rj)) {
            schiebeKette(zi, zj, ri, rj);
            board[zi][zj].setText(wert);
            board[i][j].setText(generiereRandZahl());
            board[i][j].setBackground(Color.LIGHT_GRAY);
            findeUndMergeGruppen();
            updateHighscore();
        } else {
            board[i][j].setBackground(Color.RED);
        }
    }

    public static boolean kannSchieben(int i, int j, int ri, int rj) {
        if (i <= 0 || i >= SIZE - 1 || j <= 0 || j >= SIZE - 1) return false;
        if (board[i][j].getText().strip().isEmpty()) return true;
        return kannSchieben(i + ri, j + rj, ri, rj);
    }

    public static void schiebeKette(int i, int j, int ri, int rj) {
        if (board[i][j].getText().strip().isEmpty()) return;

        int ni = i + ri;
        int nj = j + rj;

        schiebeKette(ni, nj, ri, rj);
        board[ni][nj].setText(board[i][j].getText());
        board[i][j].setText("");
    }

    public static void findeUndMergeGruppen() {
        boolean etwasGemerged;
        do {
            etwasGemerged = false;
            boolean[][] besucht = new boolean[SIZE][SIZE];

            for (int i = 1; i < SIZE - 1; i++) {
                for (int j = 1; j < SIZE - 1; j++) {
                    if (!besucht[i][j]) {
                        String wert = board[i][j].getText().strip();
                        if (!wert.isEmpty()) {
                            List<Point> gruppe = findeGruppe(i, j, wert, besucht);
                            if (gruppe.size() >= 3) {
                                int neueZahl = Integer.parseInt(wert) + 1;
                                Point p0 = gruppe.get(0);
                                board[p0.x][p0.y].setText(String.valueOf(neueZahl));
                                for (int k = 1; k < gruppe.size(); k++) {
                                    Point p = gruppe.get(k);
                                    board[p.x][p.y].setText("");
                                }
                                etwasGemerged = true;
                            }
                        }
                    }
                }
            }
        } while (etwasGemerged);
    }

    public static List<Point> findeGruppe(int i, int j, String wert, boolean[][] besucht) {
        List<Point> gruppe = new ArrayList<>();
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(i, j));
        besucht[i][j] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            gruppe.add(p);
            int[][] richtungen = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
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

    public static int ermittleMaximalwertImRaster() {
        int max = 1;
        for (int i = 1; i < SIZE - 1; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
                String wert = board[i][j].getText().strip();
                if (!wert.isEmpty()) {
                    try {
                        int zahl = Integer.parseInt(wert);
                        if (zahl > max) max = zahl;
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return max;
    }

    public static String generiereRandZahl() {
        int max = ermittleMaximalwertImRaster();
        int min = Math.max(1, max - 3);
        int zahl = (int)(Math.random() * (max - min + 1)) + min;
        return String.valueOf(zahl);
    }

    public static void updateHighscore() {
        int max = ermittleMaximalwertImRaster();
        if (max > highscore) highscore = max;
        highscoreLabel.setText("Highscore: " + highscore);
    }

    public static void updateRotStatus() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null && board[i][j].typ.equals("border")) {
                    int ri = 0, rj = 0;
                    if (i == 0) ri = 1;
                    else if (i == SIZE - 1) ri = -1;
                    else if (j == 0) rj = 1;
                    else if (j == SIZE - 1) rj = -1;

                    if (kannSchieben(i + ri, j + rj, ri, rj)) {
                        board[i][j].setBackground(Color.LIGHT_GRAY);
                    } else {
                        board[i][j].setBackground(Color.RED);
                    }
                }
            }
        }
    }

    public static void checkGameOver() {
        boolean alleRot = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null && board[i][j].typ.equals("border") &&
                    board[i][j].getBackground() != Color.RED) {
                    alleRot = false;
                    break;
                }
            }
        }
        if (alleRot) zeigeGameOverOverlay();
    }

    public static void zeigeGameOverOverlay() {
        overlay = new JPanel();
        overlay.setLayout(new BorderLayout());
        overlay.setBackground(new Color(0, 0, 0, 150));
        JLabel text = new JLabel("Du bist gestorben", SwingConstants.CENTER);
        text.setFont(new Font("Arial", Font.BOLD, 48));
        text.setForeground(Color.WHITE);
        overlay.add(text, BorderLayout.CENTER);
        frame.setGlassPane(overlay);
        overlay.setVisible(true);
    }

    public static void neustarten() {
        highscore = 1;
        highscoreLabel.setText("Highscore: 1");
        initialisiereSpielfeld((JPanel) frame.getContentPane().getComponent(1));
        overlay.setVisible(false);
    }

    // Panel-Klasse (einfacher Button-Ersatz)
    static class Panel extends JLabel {
        String typ;
        String richtung;

        public Panel(String typ, String text, String richtung) {
            super(text, SwingConstants.CENTER);
            this.typ = typ;
            this.richtung = richtung;
            setOpaque(true);
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setFont(new Font("Arial", Font.BOLD, 24));
        }
    }
}
