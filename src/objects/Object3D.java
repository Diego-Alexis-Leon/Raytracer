package objects;

import clas.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Object3D implements Interface {
    Color color;
    private Vector position;
    private double reflection;
    private double transparency;
    private double refractiveIndex;
    private BufferedImage texture;

    public Object3D(Vector position, Color color, double reflection,double transparency, double refractiveIndex) {
        setPosition(position);
        setColor(color);
        setReflection(reflection);
        setTransparency(transparency);
        setRefractiveIndex(refractiveIndex);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public abstract Intersection getIntersection(Ray ray);

    public void setReflection(double reflection){this.reflection = reflection;}

    public double getReflection() {
        return reflection;
    }

    public double getTransparency() {
        return transparency;
    }

    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    public double getRefractiveIndex() {
        return refractiveIndex;
    }

    public void setRefractiveIndex(double refractiveIndex) {
        this.refractiveIndex = refractiveIndex;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public Color getColor(Vector2 uv) {
        if (texture == null || uv == null) {
            return getColor();
        }

        double u = uv.getU();
        double v = uv.getV();

        u = u - Math.floor(u);
        v = v - Math.floor(v);

        int x = (int)(u * (texture.getWidth() - 1));
        int y = (int)((1.0 - v) * (texture.getHeight() - 1));

        return new Color(texture.getRGB(x, y));
    }
    //public abstract Color calculateDiffuce(FlatShading light);

}
