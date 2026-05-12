
import Lights.Light;
import Lights.pointLight;
import objects.Camera;
import objects.Object3D;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    Camera camera;
    java.util.List<Object3D> objects;
    java.util.List<Light> light;
    Color defaultColor;

    public Scene(Color defaultColor) {
        setDefaultColor(defaultColor);
        setObjects(new ArrayList<>());
        setLight(new ArrayList<>());
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }



    public void addObject(Object3D object){
        getObjects().add(object);
    }

    public java.util.List<Object3D> getObjects() {
        if (objects == null){
            objects = new ArrayList<>();
        }
        return objects;
    }

    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }

    public List<Light> getLight() {
        if(light == null){
            light = new ArrayList<>();
        }
        return light;
    }

    public void setLight(List<Light> ligth) {
        this.light = ligth;
    }

    public void addLight(Light light){
        getLight().add(light);
    }
}
