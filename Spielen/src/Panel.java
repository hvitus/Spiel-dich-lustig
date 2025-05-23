import javax.swing.*;
import java.awt.*;
import javax.swing.Timer; // Für den Timer
import java.awt.event.ActionListener; // Für ActionListener
import java.awt.event.ActionEvent; // Für ActionEvent
import java.awt.Font; // Für Font


class Panel extends JLabel {//Klasse Panel als Graphische Komponente, welche von initialisiere Spielfeld benutzt wird
    String typ;
    String richtung;
    static Font normalFont = new Font("Arial", Font.PLAIN, 24);
    static Font bigFont = new Font("Arial", Font.BOLD, 36);
    Timer animationTimer;

    public Panel(String typ, String text, String richtung) {
        super(text, SwingConstants.CENTER);
        this.typ = typ;
        this.richtung = richtung;
        setOpaque(true);
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFont(normalFont);
    }

    public void animateMerge() {//Merge Animation
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        final int steps = 6;
        final int delay = 20;
        final int[] step = {0}; // Array für veränderbare Variable

        animationTimer = new Timer(delay, null);
        animationTimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                float scale = 1.0f + 0.1f * (step[0] < steps / 2 ? step[0] : steps - step[0]);
                setFont(getScaledFont(scale));
                step[0]++;
                if (step[0] > steps) {
                    setFont(normalFont);
                    animationTimer.stop();
                }
            }
        });
        animationTimer.start();
    }


    private Font getScaledFont(float scale) {
        return getFont().deriveFont(getFont().getSize2D() * scale);
    }
    
    public int getValue() {//ordnet ein ob Binär oder Dezimal
        String text = getText().strip();
        if (text.isEmpty()) return -1;

        try {
            if (text.matches("[01]+")) { // Binär
                return Integer.parseInt(text, 2);
            } else {
                return Integer.parseInt(text);
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}


