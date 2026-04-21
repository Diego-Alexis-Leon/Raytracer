package objects;

import clas.Vector;

import java.awt.*;

public abstract class Object3D extends Vector {
    Color color;

    public Object3D(double x, double y, double z, Color color) {
        super(x, y, z);
        this. color = color;
    }

    public Color getColor() {
        return color;
    }
}
