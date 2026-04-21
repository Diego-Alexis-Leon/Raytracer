package clas;

public class Vector {
    double x; // angulo en que se esta lansando el vector, entre 0 y 360
    double y; //signo que tiene el vector, si es positivo o negativo
    double z; // tambien llamado tamaño, es la longitud del vector

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
}
