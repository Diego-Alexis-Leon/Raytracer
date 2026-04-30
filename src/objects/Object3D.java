package objects;

import clas.Intersection;
import clas.Ray;
import clas.Vector;

import java.awt.*;

public abstract class Object3D {
    Color color;
    private Vector position;

    public Object3D(Vector position, Color color) {
        setPosition(position);
        this. color = color;
    }

    public Color getColor() {
        return color;
    }
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public abstract Intersection getIntersection(Ray ray);

}
