package xie.fractal.web.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import xie.fractal.base.BaseLoop;
import xie.fractal.base.InitDbProcess;
import xie.fractal.db.jooq.JooqUtils;
import xie.fractal.db.jpa.JpaInfo;
import xie.fractal.db.jpa.JpaInfos;
import xie.fractal.db.jpa.base.BaseDao;
import xie.fractal.db.jpa.dao.ResourcesDao;
import xie.fractal.obj.resource.Resources;
import xie.utils.ExcelUtils;

@Slf4j
@RestController
@RequestMapping("db")
public class DbController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private ResourcesDao resourceDao;
    @Autowired
    private InitDbProcess initDbProcess;
    @Autowired
    private JpaInfos jpaInfos;

    private String indexView = "/index";

    @Autowired
    private BaseLoop loop;
    
    @Autowired
    private DSLContext dslContext;

    @RequestMapping("/jdbc")
    Object jdbc(HttpServletRequest request) {

        // Map<String, Object> map = jdbcTemplate.queryForMap("select * from resource");
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from resource where type = ?", "well");

        return list;
    }

    @RequestMapping("/jpa")
    Object jpa(HttpServletRequest request) {
        List<Resources> list = resourceDao.findAll();

        return list;
    }

    @RequestMapping("/jooq")
    Object jooq(HttpServletRequest request) {
        Result<Record> fetchResult = dslContext.select().from("Resources").fetch();
        List<Resources> list = fetchResult.into(Resources.class);
        // List<Map<String,Object>> list = fetchResult.into(Map.class);

        return list;
    }

    @RequestMapping("/create")
    Resources create(HttpServletRequest request) {
        Resources resource = new Resources();
        resource.setParentType("resource");
        resource.setType("well");
        resource.setName("well1");
        resource.setMaxCapacity(10000 + (new Random().nextInt(10)));
        resource.setMiningSpeed(20);
        resource.setRecoverySpeed(5);
        Resources newResource = resourceDao.save(resource);

        return newResource;
    }

    @RequestMapping("/update")
    Resources update(HttpServletRequest request) {
        Resources r = new Resources();
        r.setType("well");
        Example<Resources> e = Example.of(r);
        List<Resources> list = resourceDao.findAll(e);

        Resources resource = list.get(0);
        resource.setRecoverySpeed(resource.getRecoverySpeed() + 1);
        Resources newResource = resourceDao.save(resource);

        return newResource;
    }

    @RequestMapping("/readExcel")
    List<List<Map<String, String>>> readExcel(HttpServletRequest request) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        /**
         * getResource()方法会去classpath下找这个文件，获取到url resource, 得到这个资源后，调用url.getFile获取到 文件
         * 的绝对路径
         */
        URL url = classLoader.getResource("InitData.xlsx");
        /**
         * url.getFile() 得到这个文件的绝对路径
         */
        System.out.println(url.getFile());
        File file = new File(url.getFile());
        System.out.println(file.exists());

        List<List<Map<String, String>>> table = ExcelUtils.readTable(file);
        return table;
    }

    @RequestMapping("/exportExcel")
    void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Resources> list = resourceDao.findAll();
        ExcelUtils.writeExcel(response, list, Resources.class);
    }

    @RequestMapping("/exportExcel2")
    void exportExcel2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Resources> list = resourceDao.findAll();
        ExcelUtils.writeExcel(response, list);
    }

    // @RequestMapping("/exportExcelJooq")
    // void exportExcel3(HttpServletRequest request, HttpServletResponse response)
    // throws Exception {

    // List<List<?>> allList = new ArrayList<>();
    // List<String> tableList = Arrays.asList("Resources", "People", "Goods");

    // for (String table : tableList) {
    // try {
    // Result<Record> fetchResult = dslContext.select().from(table).fetch();
    // List<Map<String, Object>> list = fetchResult.into(Map.class);

    // allList.add(list);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    // ExcelUtils.writeExcelMutiSheet(response, allList);
    // }

    @RequestMapping("/exportExcelJpa")
    void exportExcelJpa(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<List<?>> allList = new ArrayList<>();
        List<List<String>> allTitleList = new ArrayList<>();
        Map<String, List<?>> allDataMap = new LinkedHashMap<>();
        Map<String, List<String>> allTitleMap = new LinkedHashMap<>();

        for (JpaInfo info : jpaInfos.getJpaMapping().values()) {
            try {
                BaseDao<?, ?> dao = info.getDao();
                List<Object> list = (List<Object>) dao.findAll();
                if (list.size() == 0) {
                    Object aaa = info.getEntityClass().getDeclaredConstructor().newInstance();
                    list.add(aaa);
                }

                allList.add(list);
                allDataMap.put(info.getName(), list);

                // title
                Result<Record> fetchResult = dslContext.select().from(info.getName()).fetch();
                List<String> titleList = JooqUtils.getTitle(fetchResult);
                allTitleList.add(titleList);
                allTitleMap.put(info.getName(), titleList);
            } catch (Exception e) {
                log.error("exportExcelJooq error. {}", e.toString());
            }
        }

        ExcelUtils.writeExcelMutiSheetByMap(response, allDataMap, allTitleMap);
    }

    @RequestMapping("/exportExcelJooq")
    void exportExcelJooq(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<List<?>> allDataList = new ArrayList<>();
        List<List<?>> allTitleList = new ArrayList<>();

        for (String table : jpaInfos.getJpaMapping().keySet()) {
            try {
                Result<Record> fetchResult = dslContext.select().from(table).fetch();
                // List<Map<String, Object>> list = getListMap(fetchResult);
                List<Map<String, Object>> list = fetchResult.intoMaps();

                allDataList.add(list);
            } catch (Exception e) {
                log.error("exportExcelJooq error.", e.toString());
            }
        }

        ExcelUtils.writeExcelMutiSheet(response, allDataList);
    }

    private List<Map<String, Object>> getListMap(Result<Record> fetchResult) {
        List<Map<String, Object>> list = new ArrayList<>();
        fetchResult.forEach((record) -> {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Field f : record.fields()) {
                Object o = record.get(f);
                map.put(f.getName(), o);
            }
            list.add(map);
        });
        return list;
    }

    @RequestMapping("/initDb")
    void initDb(HttpServletRequest request, HttpServletResponse response) throws Exception {
        initDbProcess.prepareBaseData();
    }
}
