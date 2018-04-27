package bm.gameUtil;

import java.awt.*;

public class Country {

    private String name;

    private Color outlineColor;

    private int posX;
    private int posY;

    private boolean capital;

    Country(Color cColor, String name, int posX, int posY) {
        this.name = name;
        this.outlineColor = cColor;
        this.posX = posX;
        this.posY = posY;
    }

    Country(Color cColor, String name) {
        this.name = name;
        this.outlineColor = cColor;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

    public boolean isCapital() {
        return this.capital;
    }

    public void setCapital(boolean isCapital) {
        this.capital = isCapital;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return this.posX;
    }

    public int getY() {
        return this.posY;
    }
}
