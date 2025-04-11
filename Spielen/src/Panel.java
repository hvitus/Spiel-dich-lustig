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

    public String toString() {
        return value;
    }
}