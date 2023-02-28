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
@RequestMapping("obj")
public class ObjController extends BaseController {
    @Resource
    private JpaInfos jpaInfos;
    @Resource
    private ObjTempleteData objTempleteData;

    @RequestMapping("/createObj")
    Object createObj(HttpServletRequest request, String type, String objId) {
        return jpaInfos.getJpaMapping();
    }


}
