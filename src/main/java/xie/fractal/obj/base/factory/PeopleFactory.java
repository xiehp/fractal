package xie.fractal.obj.base.factory;

import xie.fractal.obj.base.utils.ObjNameAutoIndex;
import xie.fractal.obj.people.People;

public class PeopleFactory {

    public static People createPeople(PeopleType peopleType) {
        People o = new People();
        o.setId(ObjNameAutoIndex.getNextIndex("people"));
        o.setType(peopleType.name());
        return o;
    }

    public static People createPeople(PeopleType peopleType, String name) {
        People o = new People();
        o.setId(ObjNameAutoIndex.getNextIndex("people"));
        o.setType(peopleType.name());
        o.setName(name);
        return o;
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
