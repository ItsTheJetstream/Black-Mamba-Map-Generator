package bm.hexagonUtil;

import bm.gameUtil.Biome;
import bm.gameUtil.Country;
import bm.gameUtil.CountryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class HexagonMap {

    private int sizeX; // map size (x-axis)
    private int sizeY; // map size (y-axis)

    private int borders; // border thickness ("extra offset")

    /**
     *     tSide  side
     *      ____ ______
     *             0
     *           ______       |         |
     *       5 /        \ 1   | radius  |
     *       /           \    |         | height
     *       \           /              |
     *      4 \________/  2             |
     *            3
     */

    private int height; // height value for the hexagons
    private int side; // side length of the hexagons
    private int radius; // radius of the hexagons
    private int tSide; // "t-Side" length of the Hexagon

    private int seed; // the seed for the generation, used for Biomes, Countries and Landmasses

    private boolean offSet; // true if the grid is offset by (-1,-1) to ensure better generation at the edges
                            // of the image (to leave less bugs)

    private Hexagon[][] map; // the actual Map of Hexagons
    private boolean[][] landmass; // the landmass represented as "true" in this array

    private Graphics2D g2; // the graphics object to work on, usually of a BufferedImage

    private Color defaultBodyColor = Color.GREEN; // the default Body color for the Hexagons
    private Color defaultOutlineColor = Color.BLACK; // the default outline Color for the Hexagons

    private Color borderColor = Color.BLACK; // the color of the country borders, set to black for default

    private Random rnd;

    /**
     *
     * @param g2
     * @param offSet
     * @param borders
     * @param hexHeight
     * @param mapSizeX
     * @param mapSizeY
     */
    public HexagonMap(Graphics2D g2, int seed, boolean offSet, int borders, int hexHeight, int mapSizeX, int mapSizeY) {
        this.height = hexHeight;			// h = basic dimension: height (distance between two adj centresr aka size)
        this.radius = height / 2;			// r = radius of inscribed circle
        this.side = (int) (height / 1.73205);	// s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h / sqrt(3)
        this.tSide = (int) (radius / 1.73205);	// t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2 sqrt(3)) = r / sqrt(3)

        this.g2 = g2; // setting the graphics this map works on

        this.borders = borders; // setting the border thickness

        this.sizeX = mapSizeX; // the tile map size in x-direction
        this.sizeY = mapSizeY; // the tile map size in y-direction

        this.seed = seed;

        this.offSet = offSet;

        this.rnd = new Random(Integer.toUnsignedLong(seed));
    }

    public void initialize() {
        int maxX = offSet ? sizeX + 1 : sizeX;
        int maxY = offSet ? sizeY + 1 : sizeY;

        map = new Hexagon[maxX][maxY];
        landmass = new boolean[maxX][maxY];

        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                map[i][j] = new Hexagon(defaultBodyColor, defaultOutlineColor);
                landmass[i][j] = true;
            }
        }
    }

    public void initializeLandmass(BufferedImage image) {
        if (map == null) initialize(); // initilaizes the map first if it has not been done yet

        int start = offSet ? -1 : 0; // start value depending on if the hexmap is offset or not

        for (int i = start; i < sizeX; i++) {
            for (int j = start; j < sizeY; j++) {
                int x = i * (side + tSide); // x-position on the canvas
                int y = j * height + (i % 2) * height / 2; // y-position on the canvas

                int countBlue = 0; // amount of pixels with blue color
                int countGreen = 0; // amount of pixels with green color

                int ra = radius; // radius of the circle
                int mx = x + tSide + side / 2; // x-coordinate of the middle of the circle
                int my = y + radius; // y-coordinate of the middle of the circle

                // loops through every pixel of the circle
                // searches for green and blue, increments the according values
                for (int k = my-ra; k < my+ra; k++) {
                    for (int l = mx; (l-mx)*(l-mx) + (k-my)*(k-my) <= ra*ra; l--) {
                        try {
                            if (image.getRGB(l , k) == Color.BLUE.getRGB()) {
                                countBlue += 1;
                            } else {
                                countGreen += 1;
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                    }

                    for (int l = mx+1; (l-mx)*(l-mx) + (k-my)*(k-my) <= ra*ra; l++) {
                        try {
                            if (image.getRGB(l , k) == Color.BLUE.getRGB()) {
                                countBlue += 1;
                            } else {
                                countGreen += 1;
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                    }
                }

                // new values for x and y to work on the array
                int xS = offSet ? i + 1 : i;
                int yS = offSet ? j + 1 : j;

                // colors the according hexagon either blue or green, whatever has been found the most
                if (countGreen > countBlue) {
                    Hexagon change = map[xS][yS];
                    change.setBodyColor(Color.GREEN);
                    landmass[xS][yS] = true;
                } else {
                    Hexagon change = map[xS][yS];
                    change.setBodyColor(Color.BLUE);
                    landmass[xS][yS] = false;
                }
            }
        }
    }

    /**
     * Loops through every Hexagon in the array and draws its body color onto the Graphics Object
     * @return true if everything worked, false if an error occured (map not intitialized)
     */
    public boolean drawFill() {
        if (map == null) return false;

        int start = offSet ? -1 : 0;

        for (int i = start; i < sizeX ; i++) {
            for (int j = start; j < sizeY; j++) {
                int x = i * (side + tSide);
                int y = j * height + (i % 2) * height / 2;

                Polygon poly = hexagon(x, y);

                int xS = offSet ? i + 1 : i;
                int yS = offSet ? j + 1 : j;

                colorHex(x, y, map[xS][yS].getBodyColor());

                g2.setColor(map[xS][yS].getBodyColor());
                g2.fillPolygon(poly);
            }
        }

        return true;
    }

    public boolean drawGrid() {
        if (map == null) return false;


        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int x0 = offSet ? i - 1 : i; // subtracts one from the x-Pos if the offset is activated
                int y0 = offSet ? j - 1 : j; // subtracts one from the y-Pos if the offset is activated

                int x = x0 * (side + tSide); // calculates the actual x position for the drawing
                int y = y0 * height + (x0 % 2) * height / 2; // -"- same for y position

                Hexagon workHex = map[i][j]; // the hexagon currently looked at

                for (int k = 0; k < 6; k++) {
                    drawSide(workHex, x, y, k);
                }
            }
        }
        return true;
    }

    public boolean initializeCountries(int count) {
        CountryUtil cU = new CountryUtil(rnd);
        Country[][] countries = cU.initCountries(count, sizeX, sizeY, landmass, null);

        int newSizeX = offSet ? sizeX + 1 : sizeX;
        int newSizeY = offSet ? sizeY + 1 : sizeY;

        for (int i = 0; i < newSizeX; i++) {
            for (int j = 0; j < newSizeY; j++) {
                Hexagon workHex = map[i][j];
                Country workCountry = countries[i][j];

                workHex.setCountry(workCountry);

                System.out.println(workHex.getCountry().getName());
            }
        }


        int[] xChangerEven = {0, 1, 1, 0, -1, -1};
        int[] yChangerEven = {-1, -1, 0, 1, 0, -1};

        int[] xChangerOdd = {0, 1, 1, 0, -1, -1};
        int[] yChangerOdd = {-1, 0, 1, 1, 1, 0};

        for (int i = 0; i < newSizeX; i++) {
            for (int j = 0; j < newSizeY; j++) {
                Hexagon currentHex = map[i][j];

                if (i % 2 == 0) {
                    for (int k = 0; k < 6; k++) {
                        try {
                            Hexagon h0 = map[i + xChangerEven[k]][j + yChangerEven[k]];
                            System.out.println(h0.getCountry().getName());
                            if (h0.getCountry().getName().equals(currentHex.getCountry().getName())) {
                                currentHex.setSideColor(k, new Color( 0, 0,0,0));
                                h0.setSideColor((k + 3) % 6, new Color( 0, 0,0,0));
                            } else {
                                currentHex.setSideColor(k, borderColor);
                                h0.setSideColor((k + 3) % 6, borderColor);
                            }
                        } catch (Exception e) {
                            currentHex.setSideColor(k, borderColor);
                        }
                    }
                } else {
                    for (int k = 0; k < 6; k++) {
                        try {
                            Hexagon h0 = map[i + xChangerOdd[k]][j + yChangerOdd[k]];
                            System.out.println(h0.getCountry().getName());
                            if (h0.getCountry().getName().equals(currentHex.getCountry().getName())) {
                                currentHex.setSideColor(k, new Color( 0, 0,0,0));
                                h0.setSideColor((k + 3) % 6, new Color( 0, 0,0,0));
                            } else {
                                currentHex.setSideColor(k, borderColor);
                                h0.setSideColor((k + 3) % 6, borderColor);
                            }
                        } catch (Exception e) {
                            currentHex.setSideColor(k, borderColor);
                        }
                    }
                }
            }
        }


        return true;
    }

    public ArrayList<Hexagon> getNeighbors(int x, int y) {
        ArrayList<Hexagon> toRet = new ArrayList<>();
        return toRet;
    }

    public boolean initializeBiomes(int count, Biome[] biomeTypes) {
        if (map == null) return false;

        ArrayList<Biome> startBiomes = new ArrayList<>();

        int k = 0;
        while (k < count) {
            int newX = rnd.nextInt(sizeX);
            int newY = rnd.nextInt(sizeY);

            boolean already = false;

            for (Biome b : startBiomes) {
                if (b.getX() == newX && b.getY() == newY) {
                    already = true;
                    break;
                }
            }

            if (!already) {
                Biome a = biomeTypes[rnd.nextInt(4)];
                startBiomes.add(new Biome(a.getType(), newX, newY));
                k += 1;
            }
        }

        int bMapSizeX = offSet ? sizeX + 1 : sizeX;
        int bMapSizeY = offSet ? sizeY + 1 : sizeY;

        Biome[][] biomeMap = new Biome[bMapSizeX][bMapSizeY];

        for (int i = 0; i < bMapSizeX; i++) {
            for (int j = 0; j < bMapSizeY; j++) {
                Biome nearest = new Biome(Color.BLACK);
                int dist = Integer.MAX_VALUE;

                for (Biome b : startBiomes) {
                    int cdist = new Double(HexagonUtil.hex_distance(i, j, b.getX(), b.getY())).intValue();

                    if (cdist < dist) {
                        nearest.setType(b.getType());
                        nearest.setName(b.getName());
                        dist = cdist;
                    }
                }

                biomeMap[i][j] = nearest;
            }
        }

        for (int i = 0; i < bMapSizeX; i++) {
            for (int j = 0; j < bMapSizeY; j++) {
                if(map[i][j].getBodyColor() == Color.GREEN) {
                    map[i][j].setBodyColor(biomeMap[i][j].getType());
                }
            }
        }


        return true;
    }

    private void drawSide(Hexagon hex, int posX, int posY, int sideID) {
        g2.setStroke(new BasicStroke(2));
        g2.setColor(hex.getSideColor(sideID));
        switch (sideID) {
            case 0:
                g2.drawLine(posX + tSide, posY, posX + tSide + side, posY);
                break;
            case 1:
                g2.drawLine(posX + tSide + side, posY, posX + tSide + side + tSide, posY + radius);
                break;
            case 2:
                g2.drawLine(posX + tSide + side + tSide, posY + radius, posX + tSide + side, posY + radius + radius);
                break;
            case 3:
                g2.drawLine(posX + tSide + side, posY + radius + radius, posX + tSide, posY + radius + radius);
                break;
            case 4:
                g2.drawLine(posX + tSide, posY + radius + radius, posX, posY + radius);
                break;
            case 5:
                g2.drawLine(posX, posY + radius, posX + tSide, posY);
                break;
        }
    }

    public boolean colorHex(int x, int y, Color c) {
        try {
            Hexagon workHex = map[x][y];
            workHex.setBodyColor(c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    private Polygon hexagon(int x0, int y0) {
        int x = x0 + borders;
        int y = y0 + borders;

        int[] cx = new int[] {x + tSide, x + side + tSide, x + side + tSide + tSide, x + side + tSide, x+tSide, x};
        int[] cy = new int[] {y, y, y + radius, y + radius + radius, y + radius + radius, y + radius};

        return new Polygon(cx, cy,6);
    }
}
