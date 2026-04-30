
import clas.Intersection;
import clas.Ray;
import clas.Vector;
import objects.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        Scene scene01 = new Scene(Color.WHITE);
        scene01.setCamera(new Camera(new Vector(0, 5, -10), 60, 60, 800,800));
        scene01.addObject(new Sphere(new Vector(0.5, 1, 8), 0.8, Color.RED));
        scene01.addObject(new Sphere(new Vector(0.1, 1, 6), 0.5, Color.BLUE));

        // Aqui se crean dos triangulos y se agregan a una lista en el metodo
        // addObject se añade esta lista a la lista de objetos que ya tenia la ecena

        scene01.addObject(new Model3D(new Vector(-1, -1, 3),// tengo que cambiar este valor por un metodo
                Model3D.readerOBJ("Lowpoly_tree_sample.obj"),
                /*
                new Triangle[]{
                        new Triangle(Vector.ZERO(), new Vector(1, 0, 0), new Vector(1,-1,0)),
                        new Triangle(Vector.ZERO(), new Vector(1,-1,0), new Vector(0,-1,0))},
                */
                Color.GREEN));

        BufferedImage image = raytrace(scene01);
        File outputImage = new File("image.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        java.util.List<Object3D> objects = scene.getObjects();
        Vector[][] posRaytrace = mainCamera.calculatePositionsToRay();
        Vector pos = mainCamera.getPosition();

        for (int i = 0; i < posRaytrace.length; i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                double x = posRaytrace[i][j].getX() + pos.getX();
                double y = posRaytrace[i][j].getY() + pos.getY();
                double z = posRaytrace[i][j].getZ() + pos.getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector(x, y, z));
                Intersection closestIntersection = raycast(ray, objects, null);

                Color pixelColor = scene.getDefaultColor();
                if (closestIntersection != null) {
                    pixelColor = closestIntersection.getObject().getColor();
                }
                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
    }

    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster) {
        Intersection closestIntersection = null;

        for (int i = 0; i < objects.size(); i++) {
            Object3D currObj = objects.get(i);
            if (caster == null || !currObj.equals(caster)) {
                Intersection intersection = currObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    if (distance >= 0 && (closestIntersection == null || distance < closestIntersection.getDistance())) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }
}