import clas.Intersection;
import clas.Ray;
import clas.Vector;
import objects.Camera;
import objects.Sphere;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Scene {
    int width;
    int height;

    public Scene(int width, int height) {
        this.height = height;
        this. width = width;
    }

    public void scene(Sphere sphere, Camera camera){

        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        File outputImage = new File("image.jpg");

        Vector p2 = new Vector(camera.getX2(), camera.getY2(), camera.getZ2());
        Ray p1= new Ray(camera,p2);



        Vector rigth = new Vector(p1.getDirection().getY() * 0 - p1.getDirection().getZ() * 1,
                p1.getDirection().getZ() * 0 - p1.getDirection().getX() * 0,
                p1.getDirection().getX() * 1 - p1.getDirection().getY() * 0);

        Vector up = new Vector(rigth.getY() * p1.getDirection().getZ() - rigth.getZ() * p1.getDirection().getY(),
                rigth.getZ() * p1.getDirection().getX() - rigth.getX() * p1.getDirection().getZ(),
                rigth.getX() * p1.getDirection().getY() - rigth.getY() * p1.getDirection().getX());

         double Ax = camera.getX2()-camera.getX();
         double Ay = camera.getY2()-camera.getY();
         double Az = camera.getZ2()-camera.getZ();

        for (int j=0; j<height; j++){
            for (int i=0; i<width; i++){

                Vector pixel= new Vector(camera.getX2()-Ax/2+ (Ax/width)*i,
                        camera.getY2()+ Ay/2- (Ay/height)*j,5);

                Ray ray = new Ray(camera,pixel);

                if (Intersection.intersection(ray, sphere)){
                    image.setRGB(i, j, sphere.getColor().getRGB());
                }

            }
        }
        try{
            ImageIO.write(image, "jpg", outputImage);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
