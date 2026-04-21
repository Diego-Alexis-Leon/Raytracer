package clas;

import objects.Sphere;

public class Intersection {
    public static boolean intersection(Ray ray, Sphere sphere){

        boolean punch=false;
        Vector L = new Vector(sphere.getX()-ray.getX(), sphere.getY()- ray.getY(), sphere.getZ()- ray.getZ());
        double L2 = (L.getX()* L.getX()) + (L.getY()* L.getY()) + (L.getZ()* L.getZ());
        double tca =L.getX()*ray.getDirection().getX()+
                    L.getY()*ray.getDirection().getY()+
                    L.getZ()*ray.getDirection().getZ();
        if(tca<0){
            return false;
        }else {
            double d = Math.sqrt((L2-tca*tca));
            //System.out.println("true    d = "+L2+"   tca = "+tca*tca);

            //return true;

            if (d<0){
                return false;
            }else {

                double thc = Math.sqrt(sphere.getRadius()*sphere.getRadius()-d*d);
                double t0 = tca -thc;
                double t1 = tca +thc;
                System.out.println("true  t0 ="+t0+"  t1="+t1);
                if (t0 ==t1 || (t0>0 && t1>0)){
                    //System.out.println("true");
                    punch = true;
                }
            }
        }

        return punch;
    }
}
