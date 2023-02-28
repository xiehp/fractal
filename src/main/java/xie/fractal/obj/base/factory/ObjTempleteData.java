package xie.fractal.obj.base.factory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import xie.fractal.db.jpa.JpaInfos;
import xie.fractal.db.jpa.base.BaseDao;
import xie.fractal.db.jpa.base.ObjDao;
import xie.fractal.obj.base.Obj;

@Component
public class ObjTempleteData {
    @Resource
    JpaInfos jpaInfos;

    Map<String, Map<String, Obj>> templeteMap = new LinkedHashMap<>();

    public Map<String, Map<String, Obj>> getTempleteMap() {
        return templeteMap;
    }

    /**
     * 初始化模板数据，从数据库中搜出所有template为true的数据
     */
    @PostConstruct
    private void init() {
        jpaInfos.getJpaMapping().values().forEach(jpaInfo -> {
            BaseDao<?, ?> dao = jpaInfo.getDao();
            if (dao instanceof ObjDao) {
                List<? extends Obj> list = ((ObjDao) dao).findByIsTemplete(true);
                for (Obj obj : list) {
                    putObjToTempleteMap(templeteMap, obj);
                }
            }
        });
    }

    private void putObjToTempleteMap(Map<String, Map<String, Obj>> templeteMap, Obj obj) {
        String parentType = obj.getParentType();
        Map<String, Obj> typeMap = templeteMap.computeIfAbsent(parentType, k -> {
            Map<String, Obj> map = new LinkedHashMap<>();
            return map;
        });
        templeteMap.put(parentType, typeMap);

        String type = obj.getType();
        typeMap.put(type, obj);
    }
}
