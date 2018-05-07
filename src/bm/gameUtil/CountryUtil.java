package bm.gameUtil;

import bm.hexagonUtil.HexagonUtil;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CountryUtil {

    private String[] countryNames = {"Germany", "France", "England", "America", "Finland", "Sweden", "BananaLand",
                                    "Ukraine", "Netherlands", "Denmark", "Norway", "Australia", "New Zealand", "Italy",
                                    "Russia", "Ireland", "Brazil", "Spain", "Austria", "Chile", "China", "Japan",
                                    "Cambodia", "Congo", "Madagaskar", "Poland", "Czech Republic", "Singapore",
                                    "Indonesia", "Taiwan", "Nepal", "Mongolia", "India", "Uruquay", "Korea", "Estonia",
                                    "Liechtenstein", "Skyrim", "Kanto", "Argentina", "Peru", "Equador", "Panama",
                                    "Egypt", "Bolivia"};

    private Random rnd;

    private Country waterCountry = new Country(Color.BLUE, "WATER");

    private boolean[][] capitals;

    public CountryUtil(Random rnd) {
        this.rnd = rnd;
    }
    
    private ArrayList<Country> initStartingCountries(int count, int sizeX, int sizeY, boolean[][] landmass) {
        ArrayList<Country> toRet = new ArrayList<>();
        capitals = new boolean[sizeX][sizeY];

        int k = 0;
        while (k < count) {
            int newX = rnd.nextInt(sizeX);
            int newY = rnd.nextInt(sizeY);

            boolean already = false;

            for (Country c : toRet) {
                if (c.getX() == newX && c.getY() == newY) {
                    already = true;
                    break;
                }
            }

            if (!already && landmass[newX][newY]) {
                boolean tooShort = false;

                for (Country c : toRet) {
                    if (HexagonUtil.hex_distance(c.getX(), c.getY(), newX, newY) < 5) {
                        tooShort = true;
                    }
                }

                if (!tooShort) {
                    Country addC = new Country(Color.BLACK, countryNames[k], newX, newY);
                    capitals[newX][newY] = true;
                    toRet.add(addC);
                    k += 1;
                }
            }
        }
        
        return toRet;
    }

    public boolean[][] getCapitals() {
        return this.capitals;
    }

    public Country[][] initCountries(int count, int sizeX, int sizeY, boolean[][] landmass, String[] nameList) {
        if (nameList == null) nameList = fillCountries(count);
        if (count > nameList.length) return null;

        Country[][] toRet = new Country[sizeX][sizeY];

        ArrayList<Country> startCountries = initStartingCountries(count, sizeX, sizeY, landmass);
    
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Country nearest = new Country(Color.CYAN, "[PLACEHOLDER]");
                int dist = Integer.MAX_VALUE;

                for (Country c : startCountries) {
                    int cdist = new Double(HexagonUtil.hex_distance(i, j, c.getX(), c.getY())).intValue();

                    if (cdist < dist) {
                        nearest = c;
                        dist = cdist;
                    }
                }

                if (landmass[i][j]) {
                    toRet[i][j] = nearest;
                } else {
                    toRet[i][j] = waterCountry;
                }
            }
        }
        return toRet;
    }

    private String[] fillCountries(int count) {
        String[] toRet = new String[count];
        for (int i = 0; i < count; i++) {
            toRet[i] = countryNames[i];
        }
        return toRet;
    }

    public static Pane newCountryPane() {
        Pane toRet = new Pane();
        toRet.setPrefSize(170, 40);

        CheckBox toRemBox = new CheckBox();
        toRemBox.setText("");
        toRemBox.setLayoutX(14); toRemBox.setLayoutY(12);

        javafx.scene.control.TextField nameField = new TextField();
        nameField.setPrefSize(90, 25);
        nameField.setPromptText("Country Name");
        nameField.setLayoutX(37); nameField.setLayoutY(8);

        ColorPicker cP = new ColorPicker();
        cP.setPrefSize(120, 25);
        cP.setLayoutX(135); cP.setLayoutY(8);

        toRet.getChildren().add(0, toRemBox);
        toRet.getChildren().add(1, nameField);
        toRet.getChildren().add(2, cP);

        return toRet;
    }
}
