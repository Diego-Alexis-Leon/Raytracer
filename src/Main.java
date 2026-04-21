
import objects.Camera;
import objects.Sphere;

import java.awt.*;


public class Main {
    public static void main(String[] args) {

        Sphere sphere = new Sphere(10,10,10, Color.red,2);
        Camera camera = new Camera(1,1,1,null, 5,5,5);

        Scene scene = new Scene(500,500);
        scene.scene(sphere,camera);

    }
}