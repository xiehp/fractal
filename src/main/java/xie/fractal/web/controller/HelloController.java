package xie.fractal.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xie.fractal.base.BaseLoop;
import xie.fractal.db.jpa.JpaInfo;
import xie.fractal.db.jpa.JpaInfos;
import xie.fractal.obj.base.factory.ObjTempleteData;
import xie.fractal.obj.people.People;

@Slf4j
@Controller
public class HelloController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Resource
    JpaInfos jpaInfos;
    @Resource
    ObjTempleteData objTempleteData;

    @Resource
    private BaseLoop loop;

    private String indexView = "/index";

    @RequestMapping("/")
    String hello(HttpServletRequest request) {
        request.setAttribute("count", loop.getWorld().getCount());
        request.setAttribute("loop", loop);
        request.setAttribute("world", loop.getWorld());
        request.setAttribute("peopleList", loop.getWorld().getPeopleList());
        request.setAttribute("peopleClass", People.class);

        Map<String, JpaInfo> jpaMapping = jpaInfos.getJpaMapping();
        request.setAttribute("jpaMapping", jpaMapping);
        request.setAttribute("jpaMappingKeys", jpaMapping.keySet());

        
        request.setAttribute("objTempleteData", objTempleteData);

        // model.setViewName("/index");
        return indexView;
    }

    @RequestMapping("/template")
    String template(HttpServletRequest request) {
        request.setAttribute("count", loop.getWorld().getCount());
        request.setAttribute("loop", loop);
        request.setAttribute("world", loop.getWorld());
        request.setAttribute("peopleList", loop.getWorld().getPeopleList());
        request.setAttribute("peopleClass", People.class);

        // model.setViewName("/index");
        return "/template";
    }

    @RequestMapping("/begin")
    String begin(HttpServletRequest request)  {
        log.info("World begin");
        try {
            loop.beginLoop();
        } catch (InterruptedException e) {
            log.warn("InterruptedException");
            loop.stopLoop();
        }
        return hello(request);
    }

    @RequestMapping("/stop")
    String stop(HttpServletRequest request) {
        loop.stopLoop();
        log.info("World stop");
        return hello(request);
    }

    @RequestMapping("/status")
    @ResponseBody
    BaseLoop status(HttpServletRequest request) {
        loop.stopLoop();
        log.info("World status");
        return loop;
    }

    @Data
    static class Result {
        private final int left;
        private final int right;
        private final long answer;
    }

    // // SQL sample
    // @RequestMapping("calc")
    // Result calc(@RequestParam int left, @RequestParam int right) {
    // MapSqlParameterSource source = new MapSqlParameterSource()
    // .addValue("left", left)
    // .addValue("right", right);
    // return jdbcTemplate.queryForObject("SELECT :left + :right AS answer", source,
    // (rs, rowNum) -> new Result(left, right, rs.getLong("answer")));
    // }
}
