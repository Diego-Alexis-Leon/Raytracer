package objects;

import java.awt.*;

public class Sphere extends Object3D{
    int radius;

    public Sphere(int x, int y, int z, Color color, int radius) {
        super(x, y, z, color);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
}
