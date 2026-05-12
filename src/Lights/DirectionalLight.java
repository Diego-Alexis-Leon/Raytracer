package Lights;

import clas.Intersection;
import clas.Vector;

import java.awt.*;

public class DirectionalLight extends Light{
    private Vector direction;

    public DirectionalLight(Vector direction, Color color, double intensity) {
        super(Vector.ZERO(), color, intensity);
        setDirection(direction);
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = Vector.normalize(direction);
    }

    @Override
    public double getNDotL(Intersection intersection) {
        return Math.max(Vector.dotProduct(intersection.getNormal(), Vector.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }

}

