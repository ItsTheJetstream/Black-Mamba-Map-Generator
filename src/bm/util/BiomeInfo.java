package bm.util;

import java.awt.*;

public class BiomeInfo {

    int x;
    int y;
    Color type;

    public BiomeInfo(Color t, int xx, int yy) {
        this.x = xx;
        this.y = yy;
        this.type = t;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Color getType() {
        return this.type;
    }

}
