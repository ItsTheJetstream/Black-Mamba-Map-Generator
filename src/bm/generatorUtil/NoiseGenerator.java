package bm.generatorUtil;

public class NoiseGenerator {
    public static double[][] generateNoiseMap(int mapWidth, int mapHeight, double scale, int octaves, double persistance, double lacunarity, long seed) {
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);
        double noiseMap[][] = new double[mapWidth][mapHeight];

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {

                double amplitude = 1;
                double frequency = 1;
                double noiseHeight = 0;

                for (int i = 0; i < octaves; i++) {
                    double sampleX = x / scale * frequency;
                    double sampleY = y / scale * frequency;

                    double value = noise.eval(sampleX, sampleY, 0.0);

                    noiseHeight += value * amplitude;

                    amplitude *= persistance;
                    frequency *= lacunarity;
                }

                noiseMap[x][y] = noiseHeight;

            }
        }

        return noiseMap;
    }
}
