package xie.fractal.base;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BaseInitializin implements InitializingBean {

    @Autowired
    private BaseLoop loop;

    @Autowired
    InitDbProcess initDbProcess;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("BaseInitializin afterPropertiesSet");
        // Thread thread = new Thread(() -> {
        // try {
        // loop.beginLoop();
        // } catch (InterruptedException e) {
        // log.warn("InterruptedException");
        // loop.stopLoop();
        // }
        // });
        // thread.setName("Loop");
        // thread.start();

        log.info("prepare base info");

        try {
            initDbProcess.prepareBaseData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loop.startLoopThread();
    }

}
