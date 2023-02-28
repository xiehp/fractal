package xie.fractal.base;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import xie.fractal.db.jpa.JpaInfos;
import xie.fractal.db.jpa.base.ObjDao;
import xie.fractal.obj.base.Obj;
import xie.fractal.obj.goods.Goods;
import xie.fractal.obj.people.People;
import xie.fractal.obj.resource.Resources;
import xie.utils.ExcelUtils;

@Component
@Slf4j
public class InitDbProcess {

    @Resource
    private JpaInfos jpaInfos;

    public void prepareBaseData() throws Exception {
        log.info("prepare resource info");

        // Resources co = new Resources();
        // co.setType("well");
        // ExampleMatcher matcher = ExampleMatcher.matching()
        // .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.exact());
        // Example<Resources> example = Example.of(co, matcher);
        // List<Resources> resourcesList = resourcesDao.findAll(example);

        Map<String, List<Map<String, String>>> excel = getExcelData();
        initDbTable(excel, Resources.class, jpaInfos.getResourceDao());
        initDbTable(excel, People.class, jpaInfos.getPeopleDao());
        initDbTable(excel, Goods.class, jpaInfos.getGoodsDao());
    }

    private Map<String, List<Map<String, String>>> getExcelData() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("InitData.xlsx");
        File file = new File(url.getFile());

        Map<String, List<Map<String, String>>> excel = ExcelUtils.readExcelSheetAsMap(file, "null");

        //

        return excel;
    }

    // 获取insert sql
    public void getInsertSql() {
        Map<String, List<Map<String, String>>> excel = null;
        try {
            excel = getExcelData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, String>> list = excel.get(Resources.class.getSimpleName());
        for (Map<String, String> map : list) {
            Resources obj = BeanUtil.mapToBean(map, Resources.class, true);
            // System.out.println(obj.getInsertSql());
        }
    }

    private <T extends Obj, ID> void initDbTable(Map<String, List<Map<String, String>>> excel, Class<T> cls,
            ObjDao<T, ID> objDao)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        List<Map<String, String>> sheet = excel.get(cls.getSimpleName());
        System.out.println(sheet);

        for (Map<String, String> row : sheet) {
            // String type = row.get("type");
            String objId = row.get("objId");
            List<T> objList = objDao.findByObjId(objId);
            T obj = objList.size() > 0 ? objList.get(0) : cls.getConstructor().newInstance();
            Integer id = obj.getId();
            BeanUtil.copyProperties(row, obj);
            obj.setId(id);
            objDao.save(obj);
        }

    }
}
