package sdl;

public class spielen {
    public static void main(String[] args) {
        // Fenster erstellen
        JFrame frame = new JFrame("5x5 Raster");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // GridLayout mit 5 Zeilen und 5 Spalten
        frame.setLayout(new GridLayout(5, 5));

        // 25 Buttons hinzufügen (5x5)
        for (int i = 1; i <= 25; i++) {
            JButton button = new JButton(String.valueOf(i));
            frame.add(button);
        }

        // Fenster sichtbar machen
        frame.setVisible(true);
    }
}

