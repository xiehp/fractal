package xie.fractal.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import xie.fractal.db.jpa.JpaInfos;
import xie.fractal.obj.base.factory.ObjTempleteData;

@Slf4j
@RestController
@RequestMapping("action")
public class ActionController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Resource
    private JpaInfos jpaInfos;
    @Resource
    private ObjTempleteData objTempleteData;

    @RequestMapping("/getInfo")
    Object getInfo(HttpServletRequest request) {
        return jpaInfos.getJpaMapping();
    }

    @RequestMapping("/getObjTempleteData")
    Object getObjTempleteData(HttpServletRequest request) {
        return objTempleteData.getTempleteMap();
    }

}
