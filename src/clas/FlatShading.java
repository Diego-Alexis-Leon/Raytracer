package clas;

import objects.Object3D;

import java.awt.*;

public class FlatShading {
    private Color ligthColor;
    private Vector position;
    private Vector direction;

    public FlatShading(Vector position, Vector direction, Color ligthColor){
        this. position = position;
        this. direction = direction;
        this. ligthColor = ligthColor;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Color calculateDiffuse(Intersection intersection){

        Vector L = Vector.normalize(
                Vector.substract(this.getPosition(), intersection.getPosition())
        );

        double intensity = Vector.dotProduct(intersection.getNormal(), L);
        intensity = Math.max(0, intensity);

        Color base = intersection.getObject().getColor();

        int r = (int)(base.getRed() * intensity);
        int g = (int)(base.getGreen() * intensity);
        int b = (int)(base.getBlue() * intensity);

        return new Color(
                Math.min(255, r),
                Math.min(255, g),
                Math.min(255, b)
        );
    }

}
