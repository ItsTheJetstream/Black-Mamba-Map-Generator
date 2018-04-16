package bm.gameUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class CountryUtils {

    private HashMap<Country, Color> countries;

    private String[] countryNames = {"Germany", "France", "England", "America", "Finland", "Sweden", "BananaLand",
                                    "Ukraine", "Netherlands", "Denmark", "Norway", "Australia", "New Zealand", "Italy",
                                    "Russia", "Ireland", "Brazil", "Spain", "Austria", "Chile", "China", "Japan",
                                    "Cambodia", "Congo", "Madagaskar", "Poland", "Czech Republic", "Singapore",
                                    "Indonesia", "Taiwan", "Nepal", "Mongolia", "India", "Uruquay", "Korea", "Estonia",
                                    "Liechtenstein", "Skyrim", "Kanto", "Argentina", "Peru", "Equador", "Panama",
                                    "Egypt", "Bolivia"};

    private Random rnd;

    public CountryUtils(int seed) {
        rnd = new Random(Integer.toUnsignedLong(seed));
    }

    public Country[][] generateCountries(int count, int sizeX, int sizeY) {
        if (countries == null) fillCountries(count);
        if (count < countries.size()) return null;

        Country[][] ret = new Country[sizeX][sizeY];



        return null;
    }

    private void fillCountries(int count) {

    }
}
