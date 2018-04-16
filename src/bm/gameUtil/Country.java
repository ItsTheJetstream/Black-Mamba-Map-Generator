package bm.gameUtil;

import java.awt.*;

public class Country {

    private String name;

    private Color outlineColor;

    public Country(Color cColor, String name) {
        this.name = name;
        this.outlineColor = cColor;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    public String getName() {
        return name;
    }
}
