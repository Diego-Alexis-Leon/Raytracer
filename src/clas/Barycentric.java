package clas;

import objects.Triangle;

public class Barycentric {

    private Barycentric() {
    }

    public static double[] CalculateBarycentricCoordinates(Vector point, Triangle triangle) {
        double u, v, w;
        Vector[] vertices = triangle.getVertices();
        Vector a = vertices[0];
        Vector b = vertices[1];
        Vector c = vertices[2];

        Vector v0 = Vector.substract(b, a);
        Vector v1 = Vector.substract(c, a);
        Vector v2 = Vector.substract(point, a);
        double d00 = Vector.dotProduct(v0, v0);
        double d01 = Vector.dotProduct(v0, v1);
        double d11 = Vector.dotProduct(v1, v1);
        double d20 = Vector.dotProduct(v2, v0);
        double d21 = Vector.dotProduct(v2, v1);
        double denominator = d00 * d11 - d01 * d01;
        v = (d11 * d20 - d01 * d21) / denominator;
        w = (d00 * d21 - d01 * d20) / denominator;
        u = 1.0 - v - w;

        return new double[]{u, v, w};
    }

}