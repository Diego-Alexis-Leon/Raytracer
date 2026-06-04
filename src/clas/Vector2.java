package clas;

public class Vector2 {
    private double u;
    private double v;

    public Vector2(double u, double v) {
        this.u = u;
        this.v = v;
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }

    public static Vector2 add(Vector2 a, Vector2 b) {
        return new Vector2(a.u + b.u, a.v + b.v);
    }

    public static Vector2 scalarMultiplication(Vector2 a, double scalar) {
        return new Vector2(a.u * scalar, a.v * scalar);
    }

    public static Vector2 ZERO() {
        return new Vector2(0, 0);
    }
}
