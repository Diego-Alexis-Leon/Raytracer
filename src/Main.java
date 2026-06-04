
import Lights.DirectionalLight;
import Lights.Light;
import Lights.pointLight;
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
        Scene scene01 = new Scene(Color.BLACK); //0,5,-10
        BufferedImage texture = ImageIO.read(new File("src/texture/textura_madera.jpg"));
        Model3D teapot =new Model3D(new Vector(0, 5, -8), // -1,-1,3
                Model3D.readerOBJ("src/Models/SmallTeapot.obj"),Color.GREEN,0,0,0);
        teapot.setTexture(texture);
        scene01.addObject(teapot);

        scene01.setCamera(new Camera(
                new Vector(0, 7, -2)/*  position  */,
                60, 60,
                800,800,
                0.6, 50.0,
                new Vector(0, 5, -8))); /*  direction  */
        //-1,1,3
        Color color = new Color(50,100,100);
        scene01.addLight(new pointLight(new Vector(5,7,-5),Color.WHITE,3));
        scene01.addLight(new pointLight(new Vector(-5,7,-5),Color.WHITE,3));
        //scene01.addLight(new DirectionalLight(new Vector(0, -1.0, 0.0), Color.WHITE, 0.5));
        //new Vector(0,10,-10),new Vector(5,5,0),Color.white)
        Triangle[] floor = new Triangle[]{
                new Triangle(Vector.ZERO(), new Vector(10, 0, 0), new Vector(10,0,10)),
                new Triangle(Vector.ZERO(), new Vector(10,0,10), new Vector(0,0,10))};
        Triangle[] mirror = new Triangle[]{
                new Triangle(Vector.ZERO(), new Vector(0,10,0),new Vector(10,10,0)),
                new Triangle(Vector.ZERO(),new Vector(10,10,0), new Vector(10, 0, 0))
                };
        scene01.addObject(new Model3D(new Vector(-5, 5, -13),floor,Color.WHITE,0,0,0));
        scene01.addObject(new Model3D(new Vector(-5, 5, -13),mirror,Color.CYAN,0,0,0));
        scene01.addObject(new Sphere(new Vector(-2.1, 5.50, -8), 0.5, Color.BLUE, 0,1,1.5));

        // Aqui se crean dos triangulos y se agregan a una lista en el metodo
        // addObject se añade esta lista a la lista de objetos que ya tenia la ecena


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
        //mainCamera.lookAt(new Vector(0, 6, -8));
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        Vector[][] posRaytrace = mainCamera.calculatePositionsToRay(); //Grid de pixeles
        double shininess = 32.0;
        double specularStrength = 0.5;

        for (int i = 0; i < posRaytrace.length; i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                //posicion en x y z de cada pixel en el Grig
                //double x = posRaytrace[i][j].getX() + pos.getX();
                //double y = posRaytrace[i][j].getY() + pos.getY();
                //double z = posRaytrace[i][j].getZ() + pos.getZ();

                //Ray ray = new Ray(mainCamera.getPosition(), new Vector(x, y, z));
                Ray ray = new Ray(mainCamera.getPosition(), posRaytrace[i][j]);


                // FindColor busca el color del pixel calculando intersecciones, sombras, luces, reflejos
                Color pixelColor = findColor(ray,scene,shininess,specularStrength,2,null);

                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
    }

    public static Color addColor(Color original, Color otherColor) {
        float red = (float) Math.clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) Math.clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) Math.clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }
    public static Color substractColor(Color original, Color otherColor) {
        float red = (float) Math.clamp((original.getRed() / 255.0) - (otherColor.getRed() / 255.0), 0.0, 1.0);
        float green = (float) Math.clamp((original.getGreen() / 255.0) - (otherColor.getGreen() / 255.0), 0.0, 1.0);
        float blue = (float) Math.clamp((original.getBlue() / 255.0) - (otherColor.getBlue() / 255.0), 0.0, 1.0);
        return new Color(red, green, blue);
    }

    public static Color mixColors(Color base, Color reflection, double factor) {
        factor = Math.clamp(factor, 0.0, 1.0);

        double r = (base.getRed() / 255.0) * (1.0 - factor) + (reflection.getRed() / 255.0) * factor;
        double g = (base.getGreen() / 255.0) * (1.0 - factor) + (reflection.getGreen() / 255.0) * factor;
        double b = (base.getBlue() / 255.0) * (1.0 - factor) + (reflection.getBlue() / 255.0) * factor;

        return new Color(
                (float) Math.clamp(r, 0.0, 1.0),
                (float) Math.clamp(g, 0.0, 1.0),
                (float) Math.clamp(b, 0.0, 1.0)
        );
    }

    public static Color findColor(Ray ray, Scene scene, double shininess, double specularStrength, int countReflections, Object3D caster){
        List<Light> lights = scene.getLight();
        List<Object3D> objects = scene.getObjects();
        // en raycast se le dan el rayo y su direccion y la lista de objetos
        // esto se repite por cada uno de los rayos para dalcular con que objeto
        // choca el rayo
        Intersection closestIntersection = raycast(ray, objects, caster);
        //Color color = scene.getDefaultColor();
        Color pixelColor = scene.getDefaultColor();
        if (closestIntersection != null) {
            //pixelColor = closestIntersection.getObject().getColor();
            Color objColor = closestIntersection.getObject().getColor(closestIntersection.getUv());
            for (Light light : lights){
                double nDotL = light.getNDotL(closestIntersection);
                Color lightColor = light.getColor();
                double distance2 = light.getDistance(closestIntersection);
                double intensity = (light.getIntensity() * nDotL)/distance2;
                double specularIntensity = 0;

                Vector shadowOrigin = Vector.add(
                        closestIntersection.getPosition(),
                        Vector.scalarMultiplication(closestIntersection.getNormal(), 0.001)
                );


                //Para saber si hay sombra en un pixel creamos un rayo desde la interseccion a la luz
                Ray shadowRay = new Ray(shadowOrigin,light.getPosition());
                //vemos si ese rayo choca con algun objeto
                Intersection shadow = raycast(shadowRay, objects, closestIntersection.getObject());

                // si el rayo no choca con nada seintenta calcular el bling-phong
                if (shadow == null){
                    Vector normal = Vector.normalize(closestIntersection.getNormal());
                    Vector lightDir = Vector.normalize(Vector.substract(light.getPosition(), closestIntersection.getPosition()));
                    Vector viewDir = Vector.normalize(Vector.substract(ray.getOrigin(), closestIntersection.getPosition()));
                    // Half vector
                    Vector halfVector = Vector.normalize(Vector.add(lightDir, viewDir));

                    double nDotL2 = Math.max(0, Vector.dotProduct(normal, lightDir));

                    intensity = light.getIntensity() * nDotL2 / distance2;


                    specularIntensity = specularStrength * Math.pow(Math.max(0, Vector.dotProduct(normal, halfVector)),shininess);
                }


                //si choca con algo entra en el if
                //Vemos si el objeto esta entre la interseccion y la luz
                if(shadow != null && shadow.getDistance() < distance2){
                    intensity *=0.5;
                }

                double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
                for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                    objColors[colorIndex] *= intensity * lightColors[colorIndex];

                    objColors[colorIndex] += specularIntensity * lightColors[colorIndex];
                }

                Color diffuse = new Color((float) Math.clamp(objColors[0], 0.0, 1.0),
                        (float) Math.clamp(objColors[1], 0.0, 1.0),
                        (float) Math.clamp(objColors[2], 0.0, 1.0));

                pixelColor = addColor(pixelColor, diffuse);
            }

            if (closestIntersection.getObject().getReflection() >0 && countReflections >0){
                System.out.println("se reflejo");
                Vector normal = Vector.normalize(closestIntersection.getNormal());



                Vector reflectedDirection = Vector.dirReflection(ray.getDirection(), normal);

                Vector reflectionOrigin = Vector.add(
                        closestIntersection.getPosition(),
                        Vector.scalarMultiplication(normal, 0.001)
                );

                Ray reflectionRay = Ray.fromDirection(reflectionOrigin, reflectedDirection);

                Color reflectedColor = findColor(reflectionRay, scene, shininess,specularStrength,countReflections-1, closestIntersection.getObject());

                return mixColors(pixelColor, reflectedColor,  closestIntersection.getObject().getReflection());
            }

            double transparency = closestIntersection.getObject().getTransparency();

            if (transparency > 0 && countReflections > 0) {
                Vector normal = Vector.normalize(closestIntersection.getNormal());
                double n1 = 1.0;
                double n2 = closestIntersection.getObject().getRefractiveIndex();
                if (Vector.dotProduct(ray.getDirection(), normal) > 0) {
                    normal = Vector.scalarMultiplication(normal, -1);
                    n1 = n2;
                    n2 = 1.0;
                }

                Vector refractedDirection = Vector.refract(ray.getDirection(), normal, n1, n2);

                if (refractedDirection != null) {

                    Vector refractionOrigin = Vector.add(
                            closestIntersection.getPosition(),
                            Vector.scalarMultiplication(refractedDirection, 0.001)
                    );

                    Ray refractionRay = new Ray(refractionOrigin, Vector.add(refractionOrigin, refractedDirection));

                    Color refractedColor = findColor(refractionRay, scene, shininess, specularStrength, countReflections -1, closestIntersection.getObject());

                    pixelColor = mixColors(pixelColor, refractedColor, transparency);
                }
            }
        }
        return pixelColor;
    }

    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster) {
        Intersection closestIntersection = null;

        for (int i = 0; i < objects.size(); i++) { // recorre cada objeto en la lista de objetos
            Object3D currObj = objects.get(i);
            if (caster == null || !currObj.equals(caster)) {
                Intersection intersection = currObj.getIntersection(ray); //por cada objeto revisa si choca  con el rayo
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    if (distance > 0.001 && (closestIntersection == null || distance < closestIntersection.getDistance())) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }
}