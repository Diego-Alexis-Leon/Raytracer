package objects;

import clas.Intersection;
import clas.Ray;
import clas.Vector;

import java.awt.*;

import static clas.Vector.scalarMultiplication;

public class Camera extends Object3D{

    private double[] fieldOfView = new double[2];
    private double defaultZ = 15.0;
    private int[] resolution = new int[2];
    private double[] nearFarPlanes = new double[2];
    private Vector forward = new Vector(0, 0, 1);
    private Vector right = new Vector(1, 0, 0);
    private Vector up = new Vector(0, 1, 0);

    public Camera(Vector position, double fovH, double fovV, int width, int height, double nearPlane, double farPlane, Vector direction) {
        super(position, Color.BLACK,0,0,0);
        setFOV(fovH, fovV);
        setResolution(width, height);
        setNearFarPlanes(new double[]{nearPlane, farPlane});
        lookAt(direction);
    }


    public double[] getFieldOfView() {
        return fieldOfView;
    }

    private void setFieldOfView(double[] fieldOfView) {
        this.fieldOfView = fieldOfView;
    }

    public double getFOVHorizontal() {
        return fieldOfView[0];
    }

    public double getFOVVertical() {
        return fieldOfView[1];
    }

    public void setFOVHorizontal(double fovH) {
        fieldOfView[0] = fovH;
    }

    public void setFOVVertical(double fovV) {
        fieldOfView[1] = fovV;
    }

    public void setFOV(double fovH, double fovV) {
        setFOVHorizontal(fovH);
        setFOVVertical(fovV);
    }

    public double getDefaultZ() {
        return defaultZ;
    }

    public void setDefaultZ(double defaultZ) {
        this.defaultZ = defaultZ;
    }

    public int[] getResolution() {
        return resolution;
    }

    public void setResolutionWidth(int width) {
        resolution[0] = width;
    }

    public void setResolutionHeight(int height) {
        resolution[1] = height;
    }

    public void setResolution(int width, int height) {
        setResolutionWidth(width);
        setResolutionHeight(height);
    }

    public int getResolutionWidth() {
        return resolution[0];
    }

    public int getResolutionHeight() {
        return resolution[1];
    }

    private void setNearFarPlanes(double[] nearFarPlanes) {
        this.nearFarPlanes = nearFarPlanes;
    }
    public double[] getNearFarPlanes() {
        return nearFarPlanes;
    }

    public Vector[][] calculatePositionsToRay() {
        //double rotateinY =Math.toRadians(30);

        // Horizontal boundary
        double angleMaxX = Math.toRadians(getFOVHorizontal() / 2.0);
        double maxX = getDefaultZ() * Math.tan(angleMaxX);
        double minX = -maxX;

        // Vertical boundary
        double angleMaxY = Math.toRadians(getFOVVertical() / 2.0);
        double maxY = getDefaultZ() * Math.tan(angleMaxY);
        double minY = -maxY;

        // Set grid positions
        Vector[][] positions = new Vector[getResolutionWidth()][getResolutionHeight()];
        double posZ = getDefaultZ();

        double stepX = (maxX - minX) / getResolutionWidth();
        double stepY = (maxY - minY) / getResolutionHeight();
        for(int x = 0; x < positions.length; x++){
            for(int y = 0; y < positions[x].length; y++){
                double posX = minX + (stepX * x);
                double posY = maxY - (stepY * y);

                Vector pixelPosition = getPosition();

                pixelPosition = Vector.add(pixelPosition,scalarMultiplication(right,posX));

                pixelPosition=Vector.add(pixelPosition,scalarMultiplication(up,posY));

                pixelPosition =Vector.add(pixelPosition,scalarMultiplication(forward,posZ));
                positions[x][y] = pixelPosition;
                //positions[x][y] = new Vector(posX, posY, posZ);

            }
        }

        return positions;
    }
    public void lookAt(Vector target) {
        Vector worldUp = new Vector(0, 1, 0);

        forward = Vector.normalize(Vector.substract(target, getPosition()));

        if (Math.abs(Vector.dotProduct(forward, worldUp)) > 0.999) {
            worldUp = new Vector(1, 0, 0);
        }

        right = Vector.normalize(Vector.crossProduct(forward, worldUp));
        up = Vector.normalize(Vector.crossProduct(right, forward));
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector.ZERO(), -1, Vector.ZERO(), null,null);
    }

    static int getX(int  r, int t){
        //double cos0 = t * 0.0174533;
        double cos0 = Math.toRadians(t);

        int x = (int)(r*Math.cos(cos0));
        return x;
    }
    static int getY(int  r, int t){
        //double sin0 = t * 0.0174533;
        double sin0 = Math.toRadians(t);
        int y = (int)(r*Math.sin(sin0));
        return y;
    }
}
