package xie.fractal.base;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import xie.fractal.obj.Warehouse;
import xie.fractal.obj.goods.Goods;
import xie.fractal.obj.people.People;
import xie.fractal.obj.resource.Resources;

@Slf4j // 日志
@ToString
public class BaseWorld {
    @Getter
    private int count;

    @Getter
    private List<People> peopleList = new ArrayList<>();
    @Getter
    private List<Goods> goodsList = new ArrayList<>();
    @Getter
    private List<Resources> resourcesList = new ArrayList<>();
    @Getter
    private Warehouse warehouse = new Warehouse();

    public void run() {
        count++;
        if (count % 100 == 0) {
            log.info("BaseWorld run count: {}， instance: {}", count, this.hashCode());
        }
    }

    public void stop() {
        log.info("BaseWorld stop");
    }
}
