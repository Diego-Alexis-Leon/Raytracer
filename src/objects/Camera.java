package objects;

import java.awt.*;

public class Camera extends Object3D{
    double x2;
    double y2;
    double z2;

    public Camera(double x, double y, double z, Color color,
                  double x2, double y2, double z2) {
        super(x, y, z, color);
        this. x2 = x2;
        this. y2 = y2;
        this. z2 = z2;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public double getZ2() {
        return z2;
    }
}
