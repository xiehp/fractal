package xie.fractal.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.Resource;
import xie.fractal.db.jpa.JpaInfos;
import xie.fractal.obj.base.factory.ObjTempleteData;

public class BaseController {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Resource
    private JpaInfos jpaInfos;
    @Resource
    private ObjTempleteData objTempleteData;

}
