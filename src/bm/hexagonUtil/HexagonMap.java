package bm.hexagonUtil;

import bm.gameUtil.BiomeInfo;
import bm.gameUtil.Country;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    private Graphics2D g2; // the graphics object to work on, usually of a BufferedImage

    private Color defaultBodyColor = Color.GREEN; // the default Body color for the Hexagons
    private Color defaultOutlineColor = Color.BLACK; // the default outline Color for the Hexagons

    private Color borderColor = Color.BLACK; // the color of the country borders, set to black for default

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
    }

    public void initialize() {
        if (offSet) map = new Hexagon[sizeX + 1][sizeY + 1]; // making the map larger if there is anOffSet
        else map = new Hexagon[sizeX][sizeY];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = new Hexagon(defaultBodyColor, defaultOutlineColor);
            }
        }
    }

    public void initializeLandmass(BufferedImage image) {
        if (map == null) initialize();

        int start = offSet ? -1 : 0;

        for (int i = start; i < sizeX; i++) {
            for (int j = start; j < sizeY; j++) {
                int x = i * (side + tSide);
                int y = j * height + (i % 2) * height / 2;

                int countBlue = 0;
                int countGreen = 0;

                int ra = radius; // radius of the circle
                int mx = x + tSide + side / 2; // x-coordinate of the middle of the circle
                int my = y + radius; // y-coordinate of the middle of the circle

                // loops through every pixel of the circle
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

                int xS = offSet ? i + 1 : i;
                int yS = offSet ? j + 1 : j;

                if (countGreen > countBlue) {
                    Hexagon change = map[xS][yS];
                    change.setBodyColor(Color.GREEN);
                } else {
                    Hexagon change = map[xS][yS];
                    change.setBodyColor(Color.BLUE);
                }
            }
        }
    }

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

    public boolean initializeCountries(Country[][] countries) {
        if (map == null || countries.length < map.length || countries[0].length < map[0].length) return false;

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

        int[] xChangerOdd = {-1, 0, 1, 1, 1, 0};
        int[] yChangerOdd = {0, 1, 1, 0, -1, -1};

        int[] xChangerEven = {-1, -1, 0, 1, 0, -1};
        int[] yChangerEven = {0, 1, 1, 0, -1, -1};

        for (int i = 0; i < newSizeX; i++) {
            for (int j = 0; j < newSizeY; j++) {
                Hexagon currentHex = map[i][j];

                if (i % 2 == 0) {
                    for (int k = 0; k < 6; k++) {
                        try {
                            Hexagon h0 = map[i + xChangerEven[k]][j + yChangerEven[k]];
                            if (h0.getCountry().getName().equals(currentHex.getCountry().getName())) {
                                currentHex.setSideColor(k, borderColor);
                                h0.setSideColor((k + 3) % 6, borderColor);
                            } else {
                                currentHex.setSideColor(k, new Color( 0, 0,0,0));
                                h0.setSideColor((k + 3) % 6, new Color( 0, 0,0,0));
                            }
                        } catch (Exception e) {
                            currentHex.setSideColor(k, borderColor);
                        }
                    }
                } else {
                    for (int k = 0; k < 6; k++) {
                        try {
                            Hexagon h0 = map[i + xChangerOdd[k]][j + yChangerOdd[k]];
                            if (h0.getCountry().getName().equals(currentHex.getCountry().getName())) {
                                currentHex.setSideColor(k, borderColor);
                                h0.setSideColor((k + 3) % 6, borderColor);
                            } else {
                                currentHex.setSideColor(k, new Color( 0, 0,0,0));
                                h0.setSideColor((k + 3) % 6, new Color( 0, 0,0,0));
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

    public boolean drawBiomes(int count, BiomeInfo[] biomeTypes) {
        return false;
    }

    private void drawSide(Hexagon hex, int posX, int posY, int sideID) {
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

    public double hex_distance(int x1, int y1, int x2, int y2) {
        return (Math.abs(x1 - x2) + Math.abs(x1 + y1 - x2 -y2) + Math.abs(y1 - y2)) / 2;
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
