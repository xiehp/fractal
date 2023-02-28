package xie.fractal.db.jpa;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import xie.fractal.db.jpa.base.BaseDao;
import xie.fractal.db.jpa.dao.GoodsDao;
import xie.fractal.db.jpa.dao.PeopleDao;
import xie.fractal.db.jpa.dao.ResourcesDao;
import xie.fractal.obj.goods.Goods;
import xie.fractal.obj.people.People;
import xie.fractal.obj.resource.Resources;

@Getter
@Component
public class JpaInfos {

    @Resource
    ResourcesDao resourceDao;

    @Resource
    PeopleDao peopleDao;

    @Resource
    GoodsDao goodsDao;

    private Map<String, JpaInfo> jpaMapping = new LinkedHashMap<>();

    @PostConstruct
    public void init() 
    {
        jpaMapping.put("Resources", JpaInfo.create("Resources", Resources.class, resourceDao));
        jpaMapping.put("People", JpaInfo.create("People", People.class, peopleDao));
        jpaMapping.put("Goods", JpaInfo.create("Goods", Goods.class, goodsDao));
    }


    public Class<?> getEntityClass(String name) {
        return jpaMapping.get(name).getEntityClass();
    }

    public BaseDao<?, ?> getDao(String name) {
        return jpaMapping.get(name).getDao();
    }
}
