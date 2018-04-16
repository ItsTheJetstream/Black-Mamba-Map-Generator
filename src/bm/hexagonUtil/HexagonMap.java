package bm.hexagonUtil;

import bm.gameUtil.Country;

import java.awt.*;

public class HexagonMap {

    private int sizeX;
    private int sizeY;

    private int borders;

    private int height;
    private int side;
    private int radius;
    private int tSide;

    private boolean offSet;

    private Hexagon[][] map;

    private Graphics2D g2;

    private Color defaultBodyColor = Color.GREEN;
    private Color defaultOutlineColor = Color.BLACK;

    /**
     *
     * @param g2
     * @param offSet
     * @param borders
     * @param hexHeight
     * @param mapSizeX
     * @param mapSizeY
     */
    public HexagonMap(Graphics2D g2, boolean offSet, int borders, int hexHeight, int mapSizeX, int mapSizeY) {
        this.height = hexHeight;			// h = basic dimension: height (distance between two adj centresr aka size)
        this.radius = height / 2;			// r = radius of inscribed circle
        this.side = (int) (height / 1.73205);	// s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h / sqrt(3)
        this.tSide = (int) (radius / 1.73205);	// t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2 sqrt(3)) = r / sqrt(3)

        this.g2 = g2; // setting the graphics this map works on

        this.borders = borders; // setting the border thickness

        this.sizeX = mapSizeX; // the tile map size in x-direction
        this.sizeY = mapSizeY; // the tile map size in y-direction

        this.offSet = offSet;
    }

    public void initialize() {
        if (offSet) map = new Hexagon[sizeX + 1][sizeY + 1]; // making the map larger if there is anOffSet
        else map = new Hexagon[sizeX][sizeY];

        Color[] lulu = {Color.CYAN, Color.BLACK, Color.YELLOW, Color.RED, Color.WHITE, Color.MAGENTA};

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = new Hexagon(defaultBodyColor, lulu);
            }
        }
    }

    public boolean fillHex() {
        return false;
    }

    public boolean drawGrid() {
        if (map == null) {
            return false;
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int x0 = offSet ? i - 1 : i;
                int y0 = offSet ? j - 1 : j;

                int x = x0 * (side + tSide);
                int y = y0 * height + (x0 % 2) * height / 2;

                Hexagon workHex = map[i][j];

                for (int k = 0; k < 6; k++) {
                    drawSide(workHex, x, y, k);
                }
            }
        }
        return true;
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

    public boolean drawCountries(Country[][] countries) {
        return false;
    }

    public boolean drawBiomes() {
        return false;
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
