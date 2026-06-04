package Lights;

import clas.Intersection;
import clas.Vector;

import java.awt.*;

public class pointLight extends Light{
    //private Vector direction;

    public pointLight(Vector position, Color ligthColor, double intensity){
        super(position,ligthColor,intensity);
        //this. direction = direction;
    }

    public Color calculateDiffuse(Intersection intersection){

        Vector L = Vector.normalize(
                Vector.substract(this.getPosition(), intersection.getPosition())
        );

        double intensity = Vector.dotProduct(intersection.getNormal(), L);
        intensity = Math.max(0, intensity);

        Color base = intersection.getObject().getColor();

        int r = (int)((base.getRed() * intensity));
        int g = (int)((base.getGreen() * intensity));
        int b = (int)((base.getBlue() * intensity));

        return new Color(
                Math.min(255, r),
                Math.min(255, g),
                Math.min(255, b)
        );
    }

    @Override
    public double getNDotL(Intersection intersection) {
        Vector L = Vector.normalize(
                Vector.substract(this.getPosition(), intersection.getPosition())
        );
        return Math.max(Vector.dotProduct(intersection.getNormal(), L),0.0);
        //return Math.max(Vector.dotProduct(intersection.getNormal(), Vector.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }

    @Override
    public double getDistance( Intersection intersection) {
        Vector L = getPosition();
        Vector I = intersection.getPosition();

        double zL = L.getZ();
        double yL = L.getY();
        double xL = L.getX();

        double xI = I.getX();
        double yI = I.getY();
        double zI = I.getZ();

        double distance2 = (Math.pow(xI-xL,2)+Math.pow(yI-yL,2)+Math.pow(zI-zL,2));
        return Math.sqrt(distance2);
    }

}
