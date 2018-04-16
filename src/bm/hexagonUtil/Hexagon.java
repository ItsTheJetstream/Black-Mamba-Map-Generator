package bm.hexagonUtil;

import bm.gameUtil.Country;

import java.awt.*;
import java.util.HashMap;

public class Hexagon {
    private Color c; // the color of the body of this hexagon
    private HashMap<Integer, Color> sides; // a HashMap representing the colors of this hexagon

    private Country country; // the country this Hexagon belongs to

    /**
     * Initializes a Hexagon with body-color and one side-color for all sides
     * @param bodyColor the color of its body
     * @param sideColor the color of its side
     */
    public Hexagon(Color bodyColor, Color sideColor) {
        this.c = bodyColor;
        sides = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            sides.put(i, sideColor);
        }
    }

    /**
     * Initializes a Hexagon with body-color and 6 side-colors <br>
     * The sideColor[] must be a length of 6 or black will be used for all sides
     * @param bodyColor the color of its body
     * @param sideColor the colors of its sides in an array
     */
    public Hexagon(Color bodyColor, Color[] sideColor) {
        this.c = bodyColor;
        sides = new HashMap<>();
        if (sideColor == null || sideColor.length != 6) {
            for (int i = 0; i < 6; i++) {
                sides.put(i, Color.BLACK); // in case that the array is not given correctly use standard color
            }
        } else {
            for (int i = 0; i < 6; i++) {
                sides.put(i, sideColor[i]);
            }
        }
    }

    /**
     * Returns all colors of the hexagons sides
     * @return the colors of the sides as an array
     */
    public Color[] getSideColors() {
        Color[] ret = new Color[6];
        for (int i = 0; i < sides.size(); i++) {
            ret[i] = sides.get(i);
        }
        return ret;
    }

    public Color getSideColor(int sideID) {
        return sideID > 5 ? null : sides.get(sideID);
    }

    public boolean setSideColor(int sideID, Color newColor) {
        if (sideID > 5) {
            return false;
        } else {
            sides.put(sideID, newColor);
            return true;
        }
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country c) {
        this.country = c;
    }

    public Color getBodyColor() {
        return this.c;
    }

    public void setBodyColor(Color newColor) {
        this.c = newColor;
    }
}
