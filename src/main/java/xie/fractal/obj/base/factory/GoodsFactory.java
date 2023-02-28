package xie.fractal.obj.base.factory;

import xie.fractal.obj.goods.Goods;

public class GoodsFactory {
    public static void createWood() {
        Goods p = new Goods();
        p.setType("Wood");
    }
    public static void createWater() {
        Goods p = new Goods();
        p.setType("Water");
    }

    public static void createWheat() {
        Goods p = new Goods();
        p.setType("Wheat");
    }
}
