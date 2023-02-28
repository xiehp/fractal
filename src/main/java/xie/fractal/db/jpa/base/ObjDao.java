package xie.fractal.db.jpa.base;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import xie.fractal.obj.base.Obj;

@NoRepositoryBean
public interface ObjDao<T extends Obj, ID> extends BaseDao<T, ID> {

    public List<T> findByType(String type);
    public List<T> findByObjId(String objId);
    public List<T> findByIsTemplete(Boolean isTemplete);
}
