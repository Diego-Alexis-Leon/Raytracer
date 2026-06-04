package objects;

import clas.*;

public class Triangle implements Interface {
    public static final double EPSILON = 0.0000001;
    private Vector[] vertices;
    private Vector[] normals;
    private Vector2[] textureCoordinates;

    public Triangle(Vector v0, Vector v1, Vector v2) {
        setVertices(v0, v1, v2);
        setNormals(null);
    }

    public Triangle(Vector[] vertices, Vector[] normals, Vector2[] textureCoorinates) {
        if(vertices.length == 3){
            setVertices(vertices[0], vertices[1], vertices[2]);
        } else {
            setVertices(Vector.ZERO(),Vector.ZERO(),Vector.ZERO());
        }
        setNormals(normals);
        setTextureCoordinates(textureCoorinates);
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
        Vector normal = Vector.ZERO();// 0,0,0
        Vector[] normals = this.normals;// null  or  u,v,w

        // si se usa este metodo pero no se le dieron normales al triangulo, calcula la normal con los vertices
        if(normals == null) {
            Vector[] vertices = getVertices();
            Vector v = Vector.substract(vertices[1], vertices[0]);
            Vector w = Vector.substract(vertices[0], vertices[2]);
            normal = Vector.normalize(Vector.crossProduct(v, w));
        } else {
            // si el triangulo si tiene normales las reasigna a la variable "normal"
            // para que las regrese al final del metodo
            for(int i = 0; i < normals.length; i++) {
                normal.setX(normal.getX() + normals[i].getX());
                normal.setY(normal.getY() + normals[i].getY());
                normal.setZ(normal.getZ() + normals[i].getZ());
            }
            normal.setX(normal.getX() / normals.length);
            normal.setY(normal.getY() / normals.length);
            normal.setZ(normal.getZ() / normals.length);
        }
        return normal;
    }

    public Vector[] getNormals() {
        if (normals == null) {
            Vector normal = getNormal();
            setNormals(new Vector[]{normal, normal, normal});
        }
        return normals;
    }

    private void setNormals(Vector[] normals) {
        this.normals = normals;
    }

    public void setNormals(Vector vn0, Vector vn1, Vector vn2) {
        setNormals(new Vector[]{vn0, vn1, vn2});
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        Intersection intersection = new Intersection(null, -1, null, null, null);

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

    public Vector2[] getTextureCoordinates() {
        return textureCoordinates;
    }


    public void setTextureCoordinates(Vector2[] textureCoordinates) {
        this.textureCoordinates = textureCoordinates;
    }
}

