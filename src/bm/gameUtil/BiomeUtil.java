package bm.gameUtil;

import java.awt.*;
import java.util.Random;

public class BiomeUtil {

    private String[] names = {"Tundra", "Taiga", "Deciduous forest", "Grasslands", "Desert", "High Plateaus",
                              "Tropical forest", "Coniferous forest", "Tropical savanna", "Montane rainforest",
                              "Woodland", "Alpine grasslands", "Wetland"};

    private Color[] colors = {new Color(255, 51, 0), new Color(102, 153, 0),
                              new Color(255, 204, 0), new Color(0, 204, 0),
                              new Color(255, 255, 153), new Color(209, 209, 224),
                              new Color(51, 153, 51), new Color(0, 255, 153),
                              new Color(153, 204, 0), new Color(0, 102, 0),
                              new Color(51, 77, 0), new Color(0, 153, 0),
                              new Color(0, 153, 115)};

    public Biome[][] generateBiomes(int sizeX, int sizeY, Random rnd, int biomeCount, Biome[] startBiomes) {
        Biome[][] toRet = new Biome[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Biome nearest = new Biome(Color.BLACK);
                int dist = Integer.MAX_VALUE;


            }
        }

        return toRet;
    }
}
