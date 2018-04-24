package bm.gameUtil;

import bm.hexagonUtil.HexagonUtil;

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

    public CountryUtil(Random rnd) {
        this.rnd = rnd;
    }
    
    private ArrayList<Country> initStartingCountries(int count, int sizeX, int sizeY, boolean[][] landmass) {
        ArrayList<Country> toRet = new ArrayList<>();

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
                toRet.add(new Country(Color.BLACK, countryNames[k], newX, newY));
                k += 1;
            }
        }
        
        return toRet;
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
}
