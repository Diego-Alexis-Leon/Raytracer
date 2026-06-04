package clas;

public class Ray {
    private Vector origin;
    private Vector direction;

    public Ray(Vector origin, Vector target) {
        setOrigin(origin);
        setDirection(Vector.substract(target, origin));
    }

    public static Ray fromDirection(Vector origin, Vector direction) {
        Ray ray = new Ray(origin, Vector.add(origin, direction));
        return ray;
    }

    public Vector getOrigin() {
        return origin;
    }

    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = Vector.normalize(direction);
    }
}