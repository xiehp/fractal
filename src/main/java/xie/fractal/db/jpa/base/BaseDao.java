package xie.fractal.db.jpa.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import xie.fractal.obj.base.Obj;

@NoRepositoryBean
public interface BaseDao<T extends Obj, ID> extends JpaRepository<T, ID> {

}
