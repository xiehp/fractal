package xie.fractal.obj.goods;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;
import xie.fractal.obj.base.Obj;

@Data
@Entity
@ToString(callSuper = true)
public class Goods extends Obj {
    private Integer p1;
    private Integer p2;
}
