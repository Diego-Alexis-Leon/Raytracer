package clas;

import objects.Camera;

public class Ray extends Vector{
    Vector direction;

    public Ray(Camera camera, Vector pixel) {
        super(camera.getX(), camera.getY(), camera.getZ()); //origin of ray

        double Ax = pixel.getX()-camera.getX();
        double Ay = pixel.getY()-camera.getY();
        double Az = pixel.getZ()-camera.getZ();
        double v = Math.sqrt(Ax*Ax+Ay*Ay+Az*Az);

        this. direction = new Vector(Ax/v,Ay/v,Az/v);
    }

    public Vector getDirection() {
        return direction;
    }
}
