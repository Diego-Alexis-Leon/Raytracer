package objects;

import clas.*;
import clas.Vector;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Model3D extends Object3D {
    private List<Triangle> triangles;

    public Model3D(Vector position, Triangle[] triangles, Color color, double reflection, double transparency, double refractiveIndex) {
        super(position, color,reflection, transparency, refractiveIndex);
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
        Vector2 uv = Vector2.ZERO();

        for(Triangle triangle : getTriangles()){
            Intersection intersection = triangle.getIntersection(ray);
            double intersectionDistance = intersection.getDistance();
            if(intersectionDistance > 0 &&
                    (intersectionDistance < distance || distance < 0)){
                distance = intersectionDistance;
                position = Vector.add(ray.getOrigin(), Vector.scalarMultiplication(ray.getDirection(), distance));

                /*
                Vector[] vertex = triangle.getVertices();
                Vector v = Vector.substract(vertex[1],vertex[0]);
                Vector w = Vector.substract(vertex[0],vertex[2]);
                //normal = triangle.getNormal();
                normal = Vector.normalize(Vector.crossProduct(v,w));
                /*/
                normal = Vector.ZERO();
                double[] uVw = Barycentric.CalculateBarycentricCoordinates(position, triangle);
                Vector2[] triangleUVs = triangle.getTextureCoordinates();
                if (triangleUVs != null && triangleUVs.length == 3) {
                    for (int i = 0; i < uVw.length; i++) {
                        uv = Vector2.add(uv,Vector2.scalarMultiplication(triangleUVs[i], uVw[i]));
                    }
                }

                Vector[] normals = triangle.getNormals();
                for(int i = 0; i < uVw.length; i++){
                    normal = Vector.add(normal, Vector.scalarMultiplication(normals[i], uVw[i]));

                }
            }
        }

        if(distance == -1){
            return null;
        }
        normal = Vector.normalize(normal);

        return new Intersection(position, distance, normal, this, uv);
    }

    public static Triangle[] readerOBJ(String path) throws IOException {
        /*
        En un archibo OBJ las caras o los triangulos se reprecentan como:
        f 7/1/1 8/2/1 14/3/1 13/4/1

        Esta cara esta compuesta por 4 vertices, 4 texturas y 4 normales
        de forma que al separar cada tipo de elementos que forman al
        triangulo quedaria de esta forma:

        o  Vectoes = {7,8,14,13}
        o  Textura = {1,2,3,4}
        o  Normales = {1,1,1,1}

        Esto porque las caras se comportan asi: f vector/textura/normal
        */
        List<Triangle> temporalList = new ArrayList<>();
        List<Vector> vectors = new ArrayList<>();
        List<Vector> vectorsN = new ArrayList<>();
        List<Vector2> vectorsT = new ArrayList<>();
        List<int[]> caras = new ArrayList<>();
        List<Integer> smoothingGroupsCaras = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(path));


        int defaultSmoothingGroup = -1;
        int smoothingGroup = defaultSmoothingGroup;
        Map<Integer, List<Triangle>> smoothingMap = new HashMap<>();

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

            if (partes[0].equals("vt")) {
                double u = Double.parseDouble(partes[1]);
                double v = Double.parseDouble(partes[2]);
                vectorsT.add(new Vector2(u, v));
            }

            if(partes[0].equals("s")) {
                if(partes.length > 1) {
                    if(partes[1].equals("off")){
                        smoothingGroup = defaultSmoothingGroup;
                    } else {
                        try {
                            smoothingGroup = Integer.parseInt(partes[1]);
                        } catch (NumberFormatException nfe){
                            smoothingGroup = defaultSmoothingGroup;
                        }
                    }
                }
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
                //Aqui se obtiene el indice de los vertices, texturas y las normales
                int v0 = Integer.parseInt(p1[0]) - 1;
                int v1 = Integer.parseInt(p2[0]) - 1;
                int v2 = Integer.parseInt(p3[0]) - 1;

                int t0 = Integer.parseInt(p1[1]) - 1;
                int t1 = Integer.parseInt(p2[1]) - 1;
                int t2 = Integer.parseInt(p3[1]) - 1;

                int n0 = Integer.parseInt(p1[2]) -1;
                int n1= Integer.parseInt(p2[2]) -1;
                int n2 = Integer.parseInt(p3[2]) -1;

                if (partes.length > 4){ // verifica si una cara tiene 3 o 4 vertices.
                    String[] p4 = partes[4].trim().split("/");
                    int v3 = Integer.parseInt(p4[0]) - 1;
                    int t3 = Integer.parseInt(p4[1]) - 1;
                    int n3 = Integer.parseInt(p4[2]) -1;

                    caras.add(new int[]{v0, v1, v2, v3, t0, t1, t2, t3, n0, n1, n2, n3});
                    smoothingGroupsCaras.add(smoothingGroup);
                    //System.out.println(v1+" "+v2+" "+v3+" "+v4);
                    //System.out.println(partes[0]+" "+p1[0]+" "+p2[0]+" "+p3[0]+" "+p4[0]);
                }else {
                    caras.add(new int[]{v0, v1, v2, t0, t1, t2, n0, n1, n2});
                    smoothingGroupsCaras.add(smoothingGroup);
                    //System.out.println(v1+" "+v2+" "+v3);
                    //System.out.println(partes[0]+" "+p1[0]+" "+p2[0]+" "+p3[0]);
                }
            }

        }

        for (int index = 0; index < caras.size(); index++) {

            int[] triangle = caras.get(index);
            int group = smoothingGroupsCaras.get(index);

            List<Triangle> trianglesInMap = smoothingMap.get(group);

            if (trianglesInMap == null) {
                trianglesInMap = new ArrayList<>();
            }

            if (triangle.length > 9){ // verifica si una cara tiene 3 o 4 vertices.
                Vector[] vertices1 = {vectors.get(triangle[1]),vectors.get(triangle[0]),vectors.get(triangle[2])};
                Vector[] normals1 = {vectorsN.get(triangle[9]), vectorsN.get(triangle[8]), vectorsN.get(triangle[10])};
                Vector2[] textures1 = {vectorsT.get(triangle[5]), vectorsT.get(triangle[4]), vectorsT.get(triangle[6])};
                // caras= {v0,v1,v2,v3,t4,t5,t6,t7,n8,n9,n10,n11}
                Vector[] vertices2 = {vectors.get(triangle[2]),vectors.get(triangle[0]),vectors.get(triangle[3])};
                Vector2[] textures2 = {vectorsT.get(triangle[6]), vectorsT.get(triangle[4]),vectorsT.get(triangle[7])};
                Vector[] normals2 = {vectorsN.get(triangle[10]), vectorsN.get(triangle[8]), vectorsN.get(triangle[11])};
                Triangle t1 = new Triangle(vertices1,normals1,textures1);
                Triangle t2 = new Triangle(vertices2,normals2,textures2);


                temporalList.add(t1);
                temporalList.add(t2);
                trianglesInMap.add(t1);
                trianglesInMap.add(t2);

                if (group != defaultSmoothingGroup) {
                    trianglesInMap.add(t1);
                    trianglesInMap.add(t2);
                    smoothingMap.put(group, trianglesInMap);
                }
                //System.out.println("Vertices="+triangle.length+" "+triangle[0]+" "+triangle[1]+" "+triangle[2]+" "+triangle[3]);
            }else {
                Vector[] vertices1 = {vectors.get(triangle[1]),vectors.get(triangle[0]),vectors.get(triangle[2])};
                Vector[] normals1 = {vectorsN.get(triangle[7]), vectorsN.get(triangle[6]), vectorsN.get(triangle[8])};
                Vector2[] textures1 = {vectorsT.get(triangle[4]), vectorsT.get(triangle[3]), vectorsT.get(triangle[5])};
                // caras= {v0,v1,v2,n3,n4,n5}
                Triangle t = new Triangle(vertices1,normals1,textures1);

                temporalList.add(t);
                trianglesInMap.add(t);

                if (group != defaultSmoothingGroup) {
                    trianglesInMap.add(t);
                    smoothingMap.put(group, trianglesInMap);
                }
                //System.out.println("Vertices="+triangle.length+" "+triangle[0]+" "+triangle[1]+" "+triangle[2]);
            }
        }

        class NormalPair{
            Vector normal;
            int count;

            public NormalPair() {
                normal = Vector.ZERO();
                count = 0;
            }
        }

        //Smooth vertices normals
        for (Integer key : smoothingMap.keySet()) {
            Map<Vector, NormalPair> vertexMap = new HashMap<>();
            List<Triangle> trianglesInMap = smoothingMap.get(key);
            for (Triangle triangle : trianglesInMap) {
                Vector[] triangleVertices = triangle.getVertices();
                Vector[] triangleNormals = triangle.getNormals();
                for (int i = 0; i < triangleVertices.length; i++) {
                    NormalPair normalsVertex = vertexMap.get(triangleVertices[i]);
                    if (normalsVertex == null) {
                        normalsVertex = new NormalPair();
                    }
                    if (triangleNormals.length > 0 && i < triangleNormals.length) {
                        normalsVertex.normal = Vector.add(normalsVertex.normal, triangleNormals[i]);
                        normalsVertex.count++;
                    }
                    vertexMap.put(triangleVertices[i], normalsVertex);
                }
            }
            for (Triangle triangle : trianglesInMap) {
                Vector[] triangleVertices = triangle.getVertices();
                Vector[] triangleNormals = triangle.getNormals();
                for (int i = 0; i < triangleVertices.length; i++) {
                    NormalPair normalsVertex = vertexMap.get(triangleVertices[i]);
                    triangleNormals[i] = Vector.scalarMultiplication(normalsVertex.normal, 1.0 / (double) normalsVertex.count);
                }
                triangle.setNormals(triangleNormals[0], triangleNormals[1], triangleNormals[2]);
            }
        }

        return temporalList.toArray(new Triangle[temporalList.size()]);
    }
}