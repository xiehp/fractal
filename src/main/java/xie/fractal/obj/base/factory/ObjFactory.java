package xie.fractal.obj.base.factory;

import xie.fractal.obj.people.People;

public class ObjFactory {

    private ObjFactory() {
    }
    
    public static void createObject() {
        People p =  new People();
        p.setType("Farmer");
    }

    public static void createFarmer() {
        People p = new People();
        p.setType("Farmer");
    }

    public static void createWorker() {
        People p = new People();
        p.setType("Worker");
    }
}
