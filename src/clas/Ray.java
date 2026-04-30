package clas;

import objects.Camera;

public class Ray {
    private Vector origin;
    private Vector direction;

    public Ray(Vector origin, Vector direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    public Vector getOrigin() {
        return origin;
    }

    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    public Vector getDirection() {
        return Vector.normalize(direction);
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }
}
