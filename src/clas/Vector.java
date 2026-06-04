package clas;

public class Vector {
    private double x;
    private double y;
    private double z;
    private static final Vector ZERO = new Vector(0.0, 0.0, 0.0);

    public Vector(double x, double y, double z) {
        this. x = x;
        this. y = y;
        this. z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vector clone() {
        return new Vector(getX(), getY(), getZ());
    }

    public static Vector ZERO() {
        return ZERO.clone();
    }

    @Override
    public String toString() {
        return "Vector3D {" +
                "x=" + getX() +
                ", y=" + getY() +
                ", z=" + getZ() +
                "}";
    }
    public static double dotProduct(Vector vectorA, Vector vectorB) {
        return (vectorA.getX() * vectorB.getX()) + (vectorA.getY() * vectorB.getY()) + (vectorA.getZ() * vectorB.getZ());
    }

    public static Vector crossProduct(Vector vectorA, Vector vectorB) {
        return new Vector((vectorA.getY() * vectorB.getZ()) - (vectorA.getZ() * vectorB.getY()),
                (vectorA.getZ() * vectorB.getX()) - (vectorA.getX() * vectorB.getZ()),
                (vectorA.getX() * vectorB.getY()) - (vectorA.getY() * vectorB.getX()));
    }

    public static double magnitude (Vector vectorA) {
        return Math.sqrt(dotProduct(vectorA, vectorA));
    }

    public static Vector add(Vector vectorA, Vector vectorB) {
        return new Vector(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY(), vectorA.getZ() + vectorB.getZ());
    }

    public static Vector substract(Vector vectorA, Vector vectorB) {
        return new Vector(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY(), vectorA.getZ() - vectorB.getZ());
    }

    public static Vector normalize(Vector vectorA) {
        double mag = Vector.magnitude(vectorA);
        return new Vector(vectorA.getX() / mag, vectorA.getY() / mag, vectorA.getZ() / mag);
    }

    public static Vector scalarMultiplication(Vector vectorA, double scalar) {
        return new Vector(vectorA.getX() * scalar, vectorA.getY() * scalar, vectorA.getZ() * scalar);
    }
    //Calcula la direccion de un rayo reflejado usando la normal de el objeto reflectivo y el rayo de incidencia
    public static Vector dirReflection(Vector incident, Vector normal){
        Vector i = Vector.normalize(incident);
        Vector n = Vector.normalize(normal);

        return Vector.normalize(
                Vector.substract(i,Vector.scalarMultiplication(n, 2.0 * Vector.dotProduct(i, n))));
    }

    public static Vector refract(Vector incident, Vector normal, double n1, double n2) {
        Vector I = Vector.normalize(incident);
        Vector N = Vector.normalize(normal);

        double cosI = -Vector.dotProduct(N, I);
        double eta = n1 / n2;
        double k = 1.0 - eta * eta * (1.0 - cosI * cosI);

        if (k < 0) {
            return null; // reflexión interna total
        }

        return Vector.normalize(
                Vector.add(Vector.scalarMultiplication(I, eta),
                        Vector.scalarMultiplication(N, eta * cosI - Math.sqrt(k))
                )
        );
    }
}
