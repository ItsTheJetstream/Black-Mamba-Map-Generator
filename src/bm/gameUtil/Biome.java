package bm.gameUtil;

import java.awt.*;

public class Biome {

    private int x;
    private int y;
    private Color type;

    private String name;

    public Biome(Color t, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = t;
    }

    public Biome(Color t) {
        this.type = t;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return this.name;
    }

    public void setType(Color c) {
        this.type = c;
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
