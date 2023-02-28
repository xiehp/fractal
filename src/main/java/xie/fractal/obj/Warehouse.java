package xie.fractal.obj;

import java.util.Map;

import lombok.Data;
import lombok.ToString;
import xie.fractal.obj.base.Obj;
import xie.fractal.obj.base.utils.Count;
import xie.fractal.obj.goods.Goods;

@Data
@ToString(callSuper = true)
public class Warehouse extends Obj {
    Map<Goods, Count> resourceCount;

    public Warehouse() {
        this.setId(1);
        this.setName("warehouse1");
        this.setType("Warehouse");
    }
}
