package xie.fractal.db.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xie.fractal.db.jpa.base.BaseDao;
import xie.fractal.obj.base.Obj;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JpaInfo {

    String name;
    Class<? extends Obj> entityClass;
    BaseDao<? extends Obj, ?> dao;

    public static JpaInfo create(String name, Class<? extends Obj> entityClass, BaseDao<? extends Obj, ?> dao) {
        JpaInfo info = new JpaInfo();
        info.name = name;
        info.entityClass = entityClass;
        info.dao = dao;
        return info;
    }
}
