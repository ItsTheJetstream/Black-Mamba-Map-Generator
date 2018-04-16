package bm.util;

import java.awt.*;

public class Hexagon {
    private Color c;

    public Hexagon(Color bodyColor) {
        this.c = bodyColor;
    }

    public Color getColor() {
        return this.c;
    }

    public void setColor(Color newColor) {
        this.c = newColor;
    }
}
