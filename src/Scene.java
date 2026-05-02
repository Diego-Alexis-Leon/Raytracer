
import clas.FlatShading;
import objects.Camera;
import objects.Object3D;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    Camera camera;
    java.util.List<Object3D> objects;
    FlatShading ligth;
    Color defaultColor;

    public Scene(Color defaultColor) {
        setDefaultColor(defaultColor);
        setObjects(new ArrayList<>());
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

    public FlatShading getLigth() {
        return ligth;
    }

    public void setLigth(FlatShading ligth) {
        this.ligth = ligth;
    }
}
