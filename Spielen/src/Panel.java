import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    String typ;
    String text;
    String richtung;

    public Panel(String typ, String text, String richtung) {
        this.typ = typ;
        this.text = text;
        this.richtung = richtung;
        this.setBackground(typ.equals("border") ? Color.LIGHT_GRAY : Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // <— Gitterlinie
    }

    public String getText() {
        return this.text;
    }

    public void setText(String newText) {
        this.text = newText;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 4);
    }
}
