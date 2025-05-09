import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.Point;
import java.util.List;
import javax.sound.sampled.*;

public class Spiellogik{
    static final int SIZE = 7;
    static Panel[][] board = new Panel[SIZE][SIZE];
    static JFrame frame = new JFrame("Spielfeld mit Rahmen");
    static int highscore = 1;
    static JLabel highscoreLabel = new JLabel("Highscore: 1", SwingConstants.CENTER);
    private static Timer delayTimer;
    static List<HighscoreEintrag> highscoreListe = new ArrayList<>();
    static final String DATEINAME = "highscores.txt";
    Color dunkelgrün = new Color(45, 115, 20); 


    public static void initialisiereSpielfeld() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if ((i == 0 || i == SIZE - 1) && (j == 0 || j == SIZE - 1)) {
                    board[i][j] = null;
                } else if (i > 0 && i < SIZE - 1 && j > 0 && j < SIZE - 1) {
                    board[i][j] = new Panel("grid", " ", null);
                    board[i][j].setBackground(Color.WHITE);
                } else {
                    board[i][j] = new Panel("border", "", null);
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null && board[i][j].typ.equals("border")) {
                    if (i == 0) board[i][j].richtung = "oben";
                    else if (i == SIZE - 1) board[i][j].richtung = "unten";
                    else if (j == 0) board[i][j].richtung = "links";
                    else if (j == SIZE - 1) board[i][j].richtung = "rechts";

                    board[i][j].setText(erstelleZahlAlsText(generiereRandWert()));

                    // Farbanpassung nach Format
                    setzeTextfarbe(board[i][j]);

                    int finalI = i;
                    int finalJ = j;
                    board[i][j].addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            verschiebeZahl(finalI, finalJ);
                            setzeRandfarbenZurück();
                            prüfeObSpielVorbei();
                            frame.repaint();
                        }
                    });
                }
            }
        }
    }

    public static void zeichneSpielfeld() {
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel spielfeldPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null) {
                    spielfeldPanel.add(board[i][j]);
                } else {
                    JPanel leerPanel = new JPanel();
                    leerPanel.setBackground(Color.GRAY);
                    leerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    spielfeldPanel.add(leerPanel);
                }
            }
        }

        highscoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        highscoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(highscoreLabel, BorderLayout.NORTH);
        frame.getContentPane().add(spielfeldPanel, BorderLayout.CENTER);

        frame.setSize(700, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
            setzeTextfarbe(board[zi][zj]); // <- hinzugefügt
            board[i][j].setText(erstelleZahlAlsText(generiereRandWert()));
            setzeTextfarbe(board[i][j]); // <- hinzugefügt
            board[i][j].setBackground(Color.LIGHT_GRAY);
            findeUndMergeGruppen();
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
        setzeTextfarbe(board[ni][nj]); // <- hinzugefügt
        board[i][j].setText("");
    }

    public static void findeUndMergeGruppen() {
        final boolean[] etwasGemerged = { false };

        boolean gefundenUndTimerGestartet;

        do {
            gefundenUndTimerGestartet = false;
            boolean[][] besucht = new boolean[SIZE][SIZE];

            for (int i = 1; i < SIZE - 1 && !gefundenUndTimerGestartet; i++) {
                for (int j = 1; j < SIZE - 1 && !gefundenUndTimerGestartet; j++) {
                    if (!besucht[i][j]) {
                        String wert = board[i][j].getText().strip();
                        if (!wert.isEmpty()) {
                            List<Point> gruppe = findeGruppe(i, j, wert, besucht);
                            if (gruppe.size() >= 3) {
                                gefundenUndTimerGestartet = true;

                                int neueZahl = board[i][j].getValue() + 1;

                                delayTimer = new Timer(200, new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        Point p0 = gruppe.get(0);
                                        board[p0.x][p0.y].setText(erstelleZahlAlsText(neueZahl));
                                        setzeTextfarbe(board[p0.x][p0.y]); // <- hinzugefügt
                                        board[p0.x][p0.y].animateMerge();

                                        for (int k = 1; k < gruppe.size(); k++) {
                                            Point p = gruppe.get(k);
                                            board[p.x][p.y].setText("");
                                        }

                                        if (neueZahl > highscore) {
                                            highscore = neueZahl;
                                            highscoreLabel.setText("Highscore: " + highscore);
                                            spieleHighscorePopAnimation();
                                        }

                                        etwasGemerged[0] = true;
                                        delayTimer.stop();

                                        frame.repaint();
                                        SwingUtilities.invokeLater(() -> {
                                            findeUndMergeGruppen();
                                            setzeRandfarbenZurück();
                                            prüfeObSpielVorbei();
                                        });
                                    }
                                });
                                delayTimer.setRepeats(false);
                                delayTimer.start();
                            }
                        }
                    }
                }
            }
        } while (gefundenUndTimerGestartet && etwasGemerged[0]);
    }

    public static String erstelleZahlAlsText(int wert) {
        return Math.random() < 0.5 ? Integer.toBinaryString(wert) : String.valueOf(wert);
    }

    public static int generiereRandWert() {
    	if (highscore >= 5) {
            int min = Math.max(1, highscore - 4);
            int max = highscore - 1;
            return (int) (Math.random() * (max - min + 1)) + min;
        } else {
            return (int) (Math.random() * 2) + 1;
        }
    }

    // Neue Methode zur Farbsetzung je nach Format
    public static void setzeTextfarbe(Panel p) {
        String text = p.getText().strip();
        if (text.equals("1")) {
            p.setForeground(Color.BLUE); // <- explizit für intrinsischen Wert 1
        } else if (text.matches("[01]+")) { 
        	p.setForeground(new Color(45, 115, 20));} else {
            p.setForeground(Color.BLUE); // Dezimal
        }
    }

    public static List<Point> findeGruppe(int i, int j, String wert, boolean[][] besucht) {
        List<Point> gruppe = new ArrayList<>();
        int zielWert = board[i][j].getValue(); // Wandelt Binär oder Dezimal nach int

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(i, j));
        besucht[i][j] = true;

        while (!queue.isEmpty()) {
            Point p = queue.remove();
            gruppe.add(p);

            for (int[] dir : new int[][]{{1,0}, {-1,0}, {0,1}, {0,-1}}) {
                int ni = p.x + dir[0];
                int nj = p.y + dir[1];

                if (ni > 0 && ni < SIZE - 1 && nj > 0 && nj < SIZE - 1 && !besucht[ni][nj]) {
                    if (!board[ni][nj].getText().strip().isEmpty() &&
                        board[ni][nj].getValue() == zielWert) {

                        besucht[ni][nj] = true;
                        queue.add(new Point(ni, nj));
                    }
                }
            }
        }

        return gruppe;
    }



    public static void prüfeObSpielVorbei() {
        boolean allesRot = true;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null && board[i][j].typ.equals("border")) {
                    if (!board[i][j].getBackground().equals(Color.RED)) {
                        allesRot = false;
                        break;
                    }
                }
            }
        }

        if (allesRot) zeigeGameOverOverlay();
    }

    public static void setzeRandfarbenZurück() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null && board[i][j].typ.equals("border")) {
                    int ri = 0, rj = 0;
                    if (i == 0) ri = 1;
                    else if (i == SIZE - 1) ri = -1;
                    else if (j == 0) rj = 1;
                    else if (j == SIZE - 1) rj = -1;

                    int zi = i + ri;
                    int zj = j + rj;

                    if (kannSchieben(zi, zj, ri, rj)) {
                        board[i][j].setBackground(Color.LIGHT_GRAY);
                    } else {
                        board[i][j].setBackground(Color.RED);
                    }
                }
            }
        }
    }

    public static void zeigeGameOverOverlay() {
        String name = JOptionPane.showInputDialog(frame, "Spiel vorbei! Gib deinen Namen ein:");
        if (name != null && !name.strip().isEmpty()) {
            highscoreListe.add(new HighscoreEintrag(name.strip(), highscore));
            highscoreListe.sort((a, b) -> b.punkte - a.punkte);
            speichereHighscores();
        }

        JLayeredPane layeredPane = frame.getLayeredPane();
        JPanel overlay = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));

        JLabel message = new JLabel("Du bist gestorben", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 40));
        message.setForeground(Color.WHITE);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        overlay.add(Box.createVerticalStrut(30));
        overlay.add(message);

        for (HighscoreEintrag eintrag : highscoreListe) {
            JLabel scoreLabel = new JLabel(eintrag.name + " - " + eintrag.punkte);
            scoreLabel.setForeground(Color.WHITE);
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            overlay.add(scoreLabel);
        }

        JButton restartButton = new JButton("Neustart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 24));
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> {
            frame.getLayeredPane().remove(overlay);
            frame.getLayeredPane().repaint();
            frame.getContentPane().removeAll();
            highscore = 1;
            highscoreLabel.setText("Highscore: 1");
            initialisiereSpielfeld();
            zeichneSpielfeld();
            spieleHintergrundmusik("background.wav");
        });

        overlay.add(Box.createVerticalStrut(20));
        overlay.add(restartButton);
        layeredPane.add(overlay, JLayeredPane.POPUP_LAYER);
        spieleGameOverMusik("gameover.wav");
    }

    static class HighscoreEintrag {
        String name;
        int punkte;
        HighscoreEintrag(String name, int punkte) {
            this.name = name;
            this.punkte = punkte;
        }
    }
    public static void spieleHighscorePopAnimation() {
        Timer timer = new Timer(50, null);
        final int[] groesse = {20};
        final boolean[] wachsend = {true};

        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (wachsend[0]) {
                    groesse[0] += 2;
                    if (groesse[0] >= 32) wachsend[0] = false;
                } else {
                    groesse[0] -= 2;
                    if (groesse[0] <= 20) {
                        timer.stop();
                        groesse[0] = 20;
                    }
                }
                highscoreLabel.setFont(new Font("Arial", Font.BOLD, groesse[0]));
            }
        });

        timer.start();
    }

    public static void speichereHighscores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATEINAME))) {
            for (HighscoreEintrag e : highscoreListe) {
                writer.println(e.name + ";" + e.punkte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ladeHighscores() {
        File file = new File(DATEINAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] parts = zeile.split(";");
                if (parts.length == 2) {
                    String name = parts[0];
                    int punkte = Integer.parseInt(parts[1]);
                    highscoreListe.add(new HighscoreEintrag(name, punkte));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Clip hintergrundClip;
    private static Clip gameOverClip;

    public static void spieleHintergrundmusik(String dateipfad) {
        stopMusik(); // Vorherige Musik stoppen
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(dateipfad));
            hintergrundClip = AudioSystem.getClip();
            hintergrundClip.open(audioInput);
            hintergrundClip.loop(Clip.LOOP_CONTINUOUSLY); // Endlosschleife
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void spieleGameOverMusik(String dateipfad) {
    try {
        if (hintergrundClip != null && hintergrundClip.isRunning()) {
            hintergrundClip.stop();
        }

        AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(dateipfad));
        gameOverClip = AudioSystem.getClip();
        gameOverClip.open(audioInput);
        gameOverClip.start(); // einmalig abspielen
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static void stopMusik() {
    if (hintergrundClip != null && hintergrundClip.isRunning()) {
        hintergrundClip.stop();
        hintergrundClip.close();
    }
    if (gameOverClip != null && gameOverClip.isRunning()) {
        gameOverClip.stop();
        gameOverClip.close();
    }
}


}
