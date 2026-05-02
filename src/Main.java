
import clas.FlatShading;
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
        scene01.setLigth(new FlatShading(new Vector(0,10,-10),new Vector(5,5,0),Color.white));
        scene01.addObject(new Sphere(new Vector(0.5, 1, 8), 0.8, Color.RED));
        scene01.addObject(new Sphere(new Vector(0.1, 1, 6), 0.5, Color.BLUE));

        // Aqui se crean dos triangulos y se agregan a una lista en el metodo
        // addObject se añade esta lista a la lista de objetos que ya tenia la ecena

        scene01.addObject(new Model3D(new Vector(-1, -1, 3),
                Model3D.readerOBJ("Lowpoly_tree_sample.obj"),Color.GREEN));
        /*
        new Triangle[]{
                new Triangle(Vector.ZERO(), new Vector(1, 0, 0), new Vector(1,-1,0)),
                new Triangle(Vector.ZERO(), new Vector(1,-1,0), new Vector(0,-1,0))},
        */


        BufferedImage image = raytrace(scene01); // aqui se encuentra el metodo para calcular el Diffuse del color
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
        Vector[][] posRaytrace = mainCamera.calculatePositionsToRay(); //Grid de pixeles
        Vector pos = mainCamera.getPosition();

        FlatShading ligth = scene.getLigth();

        for (int i = 0; i < posRaytrace.length; i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                //posicion en x y z de cada pixel en el Grig
                double x = posRaytrace[i][j].getX() + pos.getX();
                double y = posRaytrace[i][j].getY() + pos.getY();
                double z = posRaytrace[i][j].getZ() + pos.getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector(x, y, z));
                // en raycast se le dan el rayo y su direccion y la lista de objetos
                // esto se repite por cada uno de los rayos para dalcular con que objeto
                // choca el rayo
                Intersection closestIntersection = raycast(ray, objects, null);


                Color pixelColor = scene.getDefaultColor();
                if (closestIntersection != null) {
                    //pixelColor = closestIntersection.getObject().getColor();

                    //aqui cambiare el color original del objeto con el que chica el rayo.
                    pixelColor = ligth.calculateDiffuse(closestIntersection);
                    //se puede cambiar el color del poligono directmente dentro de calculateDiffuse
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