package Lights;

import clas.Intersection;
import clas.Ray;
import clas.Vector;
import objects.Object3D;

import java.awt.*;

public abstract class Light extends Object3D {
    private double intensity;

    public Light(Vector position, Color color, double intensity) {
        super(position, color,0,0,0);
        setIntensity(intensity);
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public abstract double getNDotL(Intersection intersection);

    @Override
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector.ZERO(), -1, Vector.ZERO(), null, null);
    }

    public abstract double getDistance(Intersection intersection);
}
