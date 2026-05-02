package objects;

import clas.Interface;
import clas.Intersection;
import clas.Ray;
import clas.Vector;

public class Triangle implements Interface {
    public static final double EPSILON = 0.0000001;
    private Vector[] vertices;
    private Vector[] normals;

    public Triangle(Vector v0, Vector v1, Vector v2) {
        setVertices(v0, v1, v2);
        setNormals(null);
    }

    public Vector[] getVertices() {
        return vertices;
    }

    // Hay dos metodos set por si se da la matriz de 3 vectores completa o se dan
    // los 3 vectores para despues convertirlos en matriz.
    private void setVertices(Vector[] vertices) {
        this.vertices = vertices;
    }

    public void setVertices(Vector v0, Vector v1, Vector v2) {
        setVertices(new Vector[]{v0, v1, v2});
    }

    public Vector getNormal(){
        //return new Vector(1,1,1);
        return Vector.ZERO();
    }

    public Vector[] getNormals() {
        return normals;
    }

    private void setNormals(Vector[] normals) {
        this.normals = normals;
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        Intersection intersection = new Intersection(null, -1, null, null);

        Vector[] vert = getVertices();
        Vector v2v0 = Vector.substract(vert[2], vert[0]);
        Vector v1v0 = Vector.substract(vert[1], vert[0]);
        Vector vectorP = Vector.crossProduct(ray.getDirection(), v1v0);
        double det = Vector.dotProduct(v2v0, vectorP);
        double invDet = 1.0 / det;
        Vector vectorT = Vector.substract(ray.getOrigin(), vert[0]);
        double u = invDet * Vector.dotProduct(vectorT, vectorP);

        if (!(u < 0 || u > 1)) {
            Vector vectorQ = Vector.crossProduct(vectorT, v2v0);
            double v = invDet * Vector.dotProduct(ray.getDirection(), vectorQ);
            if (!(v < 0 || (u + v) > (1.0 + EPSILON))) {
                double t = invDet * Vector.dotProduct(vectorQ, v1v0);
                intersection.setDistance(t);
            }
        }

        return intersection;
    }
}

