package objects;

import clas.Intersection;
import clas.Ray;
import clas.Vector;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Model3D extends Object3D {
    private List<Triangle> triangles;

    public Model3D(Vector position, Triangle[] triangles, Color color) {
        super(position, color);
        setTriangles(triangles);
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(Triangle[] triangles) {
        Vector position = getPosition();
        Set<Vector> uniqueVertices = new HashSet<>();
        for(Triangle triangle : triangles){
            uniqueVertices.addAll(Arrays.asList(triangle.getVertices()));
        }

        for(Vector vertex : uniqueVertices){
            vertex.setX(vertex.getX() + position.getX());
            vertex.setY(vertex.getY() + position.getY());
            vertex.setZ(vertex.getZ() + position.getZ());
        }
        this.triangles = Arrays.asList(triangles);
    }


    @Override
    public Intersection getIntersection(Ray ray) {
        double distance = -1;
        Vector position = Vector.ZERO();
        Vector normal = Vector.ZERO();

        for(Triangle triangle : getTriangles()){
            Intersection intersection = triangle.getIntersection(ray);
            double intersectionDistance = intersection.getDistance();
            if(intersectionDistance > 0 &&
                    (intersectionDistance < distance || distance < 0)){
                distance = intersectionDistance;
                position = Vector.add(ray.getOrigin(), Vector.scalarMultiplication(ray.getDirection(), distance));
                normal = triangle.getNormal();
            }
        }

        if(distance == -1){
            return null;
        }

        return new Intersection(position, distance, normal, this);
    }

    public static Triangle[] readerOBJ(String path) throws IOException {
        Triangle[] list = new Triangle[]{
                new Triangle(Vector.ZERO(), new Vector(1, 0, 0), new Vector(1,-1,0)),
                new Triangle(Vector.ZERO(), new Vector(1,-1,0), new Vector(0,-1,0))
        };
        List<Triangle> temporalList = new ArrayList<>();
        List<Vector> vectors = new ArrayList<>();
        List<Vector> vectorsN = new ArrayList<>();
        List<int[]> caras = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {
            //System.out.println(line);
            /*
             separa las lineas de el archibo cuando encuentra un espacio, por ejemplo con:
                vectores:
             v -0.524753 -0.707811 -1.37533  ->  ["v","-0.524753","-0.707811","-1.37533"]
                caras:
             f 7/1/1 8/2/1 14/3/1 13/4/1   ->   ["f","7/1/1","8/2/1","13/4/1"]
            */
            String[] partes = line.trim().split("\\s+");


            if (partes[0].equals("v")) {
                double x = Double.parseDouble(partes[1]);
                double y = Double.parseDouble(partes[2]);
                double z = Double.parseDouble(partes[3]);
                vectors.add(new Vector(x,y,z));
            }

            if (partes[0].equals("vn")) {
                double x = Double.parseDouble(partes[1]);
                double y = Double.parseDouble(partes[2]);
                double z = Double.parseDouble(partes[3]);
                vectorsN.add(new Vector(x,y,z));
            }

            if (partes[0].equals("f")) {
                /*
                separa las partes de un arreglo en partes mas pequeñas cuando la etiqueta es f,
                por ejemplo con: ["f","7/1/1","8/2/1","13/4/1"]
                p1 = [7,1,1]
                p2 = [8,2,1]
                p3 = [13,4,1]
                */
                //System.out.println(partes[0]+" "+partes[1]+" "+partes[2]+" "+partes[3]+" "+partes[4]);
                String[] p1 = partes[1].trim().split("/");
                String[] p2 = partes[2].trim().split("/");
                String[] p3 = partes[3].trim().split("/");
                int v1 = Integer.parseInt(p1[0]) - 1;
                int v2 = Integer.parseInt(p2[0]) - 1;
                int v3 = Integer.parseInt(p3[0]) - 1;

                String[] p4 = new String[]{};
                int v4 = 0;
                if (partes.length > 4){ // verifica si una cara tiene 3 o 4 vertices.
                    p4 = partes[4].trim().split("/");
                    v4 = Integer.parseInt(p4[0]) - 1;
                    caras.add(new int[]{v1, v2, v3,v4});
                    //System.out.println(v1+" "+v2+" "+v3+" "+v4);
                    //System.out.println(partes[0]+" "+p1[0]+" "+p2[0]+" "+p3[0]+" "+p4[0]);
                }else {
                    caras.add(new int[]{v1, v2, v3});
                    //System.out.println(v1+" "+v2+" "+v3);
                    //System.out.println(partes[0]+" "+p1[0]+" "+p2[0]+" "+p3[0]);
                }
            }
        }

        for (int[] triangle : caras){
            if (triangle.length > 3){ // verifica si una cara tiene 3 o 4 vertices.
                temporalList.add(new Triangle(vectors.get(triangle[0]),vectors.get(triangle[1]),vectors.get(triangle[2])));
                temporalList.add(new Triangle(vectors.get(triangle[0]),vectors.get(triangle[2]),vectors.get(triangle[3])));
                //System.out.println("Vertices="+triangle.length+" "+triangle[0]+" "+triangle[1]+" "+triangle[2]+" "+triangle[3]);
            }else {
                temporalList.add(new Triangle(vectors.get(triangle[0]),vectors.get(triangle[1]),vectors.get(triangle[2])));
                //System.out.println("Vertices="+triangle.length+" "+triangle[0]+" "+triangle[1]+" "+triangle[2]);
            }
        }
        Triangle[] result = temporalList.toArray(new Triangle[0]);
        //return list;
        return result;
    }
}