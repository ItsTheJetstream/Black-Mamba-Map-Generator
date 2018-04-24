package bm.hexagonUtil;

import bm.gameUtil.Biome;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class HexagonUtil {

    private int h = 0;
    private int r = 0;
    private int s = 0;
    private int t = 0;
    private int b = 0;

    BufferedImage workImage;

    private Hexagon[][] field;

    private int fieldSizeX;
    private int fieldSizeY;

    private int xOffSet;
    private int yOffSet;

    Graphics2D g2;

    public static double hex_distance(int x1, int y1, int x2, int y2) {
        return (Math.abs(x1 - x2) + Math.abs(x1 + y1 - x2 -y2) + Math.abs(y1 - y2)) / 2;
    }

    /*
    public HexagonUtil(int height, int border, int sizeX, int sizeY, int xOffSet, int yOffSet, BufferedImage img) {
        h = height;			// h = basic dimension: height (distance between two adj centresr aka size)
        r = h/2;			// r = radius of inscribed circle
        s = (int) (h / 1.73205);	// s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h / sqrt(3)
        t = (int) (r / 1.73205);	// t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2 sqrt(3)) = r / sqrt(3)

        this.fieldSizeX = sizeX;
        this.fieldSizeY = sizeY;

        b = border;
        workImage = img;

        field = new Hexagon[sizeX+1][sizeY+1];

        this.xOffSet = xOffSet;
        this.yOffSet = yOffSet;

        g2 = (Graphics2D) img.getGraphics(); // initializes the graphics for the image
    }
    */

    public Polygon hexagon(int x0, int y0) {
        int x = x0 + b;
        int y = y0 + b;

        int[] cx = new int[] {x+t,x+s+t,x+s+t+t,x+s+t,x+t,x};
        int[] cy = new int[] {y,y,y+r,y+r+r,y+r+r,y+r};

        return new Polygon(cx, cy,6);
    }

    public void fillHex(int x0, int y0, Color c, Graphics2D g2) {
        int x = x0 * (s+t);
        int y = y0 * h + (x0%2) * h/2;

        Polygon poly = hexagon(x, y);

        g2.setColor(c);
        g2.fillPolygon(poly);
    }

    public void fillHex(int x0, int y0) {
        int x = x0 * (s+t);
        int y = y0 * h + (x0%2) * h/2;

        int countBlue = 0;
        int countGreen = 0;

        int ra = h/2; // radius of the circle
        int mx = x + t + s/2; // x-coordinate of the middle of the circle
        int my = y + r; // y-coordinate of the middle of the circle

        // loops through every pixel of the circle
        for (int i = my-ra; i < my+ra; i++) {
            for (int j = mx; (j-mx)*(j-mx) + (i-my)*(i-my) <= ra*ra; j--) {
                try {
                    if (workImage.getRGB(j , i) == Color.BLUE.getRGB()) {
                        countBlue += 1;
                    } else {
                        countGreen += 1;
                    }
                } catch (Exception e) {
                    // nothing
                }
            }
            for (int j = mx+1; (j-mx)*(j-mx) + (i-my)*(i-my) <= ra*ra; j++) {
                try {
                    if (workImage.getRGB(j , i) == Color.BLUE.getRGB()) {
                        countBlue += 1;
                    } else {
                        countGreen += 1;
                    }
                } catch (Exception e) {
                    // nothing
                }
            }
        }

        Polygon poly = hexagon(x, y);

        /*
        if (countGreen > countBlue) {
            g2.setColor(Color.GREEN);
            try {
                field[x0+1][y0+1] = new Hexagon(Color.GREEN);
            } catch(Exception e) {
                // nothing
            }
        } else {
            g2.setColor(Color.BLUE);
            try {
                field[x0+1][y0+1] = new Hexagon(Color.BLUE);
            } catch(Exception e) {
                // nothing
            }
        }

        g2.fillPolygon(poly);
        */
    }

    public void fillBiomes(int biomeCount, int seed) {
        Color[] usable = {new Color(255, 204, 102), new Color(0, 204, 102),
                new Color(153, 102, 51), new Color(51, 102, 0)};

        ArrayList<Biome> biomes = new ArrayList<Biome>();

        Random rnd = new Random(Integer.toUnsignedLong(seed));
        Random bR = new Random(Integer.toUnsignedLong(seed + rnd.nextInt(10)));

        int c = 0;
        while (c < biomeCount) {
            int nx = rnd.nextInt(fieldSizeX);
            int ny = rnd.nextInt(fieldSizeY);

            boolean already = false;
            for (int i = 0; i < biomes.size(); i++) {
                if (biomes.get(i).getX() == nx && biomes.get(i).getY() == ny) already = true;
            }

            if (!already) {
                biomes.add(new Biome(usable[bR.nextInt(4)], nx, ny));
                c += 1;
            }
        }

        for (int i = xOffSet; i < field.length-1; i++) {
            for (int j = yOffSet; j < field[0].length-1; j++) {
                Color nearest = Color.BLACK;
                int dist = Integer.MAX_VALUE;

                for (int z = 0; z < biomes.size(); z++) {
                    int xdiff = biomes.get(z).getX() - i;
                    int ydiff = biomes.get(z).getY() - j;

                    int cdist = xdiff*xdiff + ydiff*ydiff;

                    if (cdist < dist) {
                        nearest = biomes.get(z).getType();
                        dist = cdist;
                    }
                }

                /*
                if (field[i+1][j+1].getColor() == Color.GREEN) {
                    field[i+1][j+1].setColor(nearest);
                    fillHex(i, j, nearest, g2);
                }
                */
            }
        }
    }

    public void outlineHex(int x0, int y0, Color c) {
        int x = x0 * (s+t);
        int y = y0 * h + (x0%2) * h/2;

        Polygon poly = hexagon(x, y);

        g2.setColor(c);
        g2.drawPolygon(poly);
    }
}
