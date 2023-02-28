package xie.fractal.obj.base.factory;

import xie.fractal.obj.base.utils.ObjNameAutoIndex;
import xie.fractal.obj.resource.Resources;

public class ResourcesFactory {

    public static Resources createResource(ResourcesType resourceType) {
        Resources o = new Resources();
        o.setId(ObjNameAutoIndex.getNextIndex("Resources"));
        o.setType(resourceType.name());
        return o;
    }

    public static Resources createResources(ResourcesType resourceType, String name) {
        Resources o = new Resources();
        o.setId(ObjNameAutoIndex.getNextIndex("Resource"));
        o.setType(resourceType.name());
        o.setName(name);
        return o;
    }

    public static void createFarmer() {
        Resources o = new Resources();
        o.setType("Well");
    }

    public static void createWorker() {
        Resources o = new Resources();
        o.setType("Worker");
    }
}
