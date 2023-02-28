package xie.fractal.base;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import xie.fractal.obj.base.factory.PeopleFactory;
import xie.fractal.obj.base.factory.PeopleType;
import xie.fractal.obj.base.factory.ResourcesType;
import xie.fractal.obj.base.factory.ResourcesFactory;
import xie.fractal.obj.people.People;
import xie.fractal.obj.resource.Resources;

@Configuration
public class DefaultConfiguration {

    @ConditionalOnMissingBean()
    @Bean(name = "world")
    public BaseWorld worldInit1() {
        BaseWorld world = new BaseWorld();

        List<Resources> resourcesList = world.getResourcesList();
        resourcesList.add(ResourcesFactory.createResources(ResourcesType.Well, "well1"));
        resourcesList.add(ResourcesFactory.createResources(ResourcesType.Forest, "forest1"));
        resourcesList.add(ResourcesFactory.createResources(ResourcesType.Tree, "tree1"));
        resourcesList.add(ResourcesFactory.createResources(ResourcesType.Fruit, "fruit1"));


        List<People> peopleList = world.getPeopleList();
        peopleList.add(PeopleFactory.createPeople(PeopleType.Farmer, "farmer1"));
        peopleList.add(PeopleFactory.createPeople(PeopleType.Farmer, "farmer2"));
        peopleList.add(PeopleFactory.createPeople(PeopleType.Worker, "worker1"));
        peopleList.add(PeopleFactory.createPeople(PeopleType.Worker, "worker2"));
        peopleList.add(PeopleFactory.createPeople(PeopleType.King, "king1"));

        return world;
    }

    // @ConditionalOnMissingBean()
    // @Bean(name = "world")
    // public BaseWorld world() {
    //     BaseWorld world = new BaseWorld();
    //     List<People> people = world.getPeopleList();
    //     people.add(new Noble());
    //     people.add(new King());
    //     people.add(new Worker());
    //     people.add(new Worker());
    //     people.add(new Farmer("f1"));
    //     people.add(new Farmer("f2"));
    //     return world;
    // }


    
}
