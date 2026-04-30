package clas;


import objects.Object3D;
import objects.Sphere;

public class Intersection {

    private double distance;
    private Vector position;
    private Vector normal;
    private Object3D object;

    public Intersection(Vector position, double distance, Vector normal, Object3D object) {
        setPosition(position);
        setDistance(distance);
        setNormal(normal);
        setObject(object);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getNormal() {
        return normal;
    }

    public void setNormal(Vector normal) {
        this.normal = normal;
    }

    public Object3D getObject() {
        return object;
    }

    public void setObject(Object3D object) {
        this.object = object;
    }

}
