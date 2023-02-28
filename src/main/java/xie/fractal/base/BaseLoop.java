package xie.fractal.base;

import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 基本的循环
 */
@Component(value = "loop")
@Slf4j
@ToString
public class BaseLoop implements Runnable {

    @Resource
    @Getter
    private BaseWorld world;

    @Getter
    private boolean isRunning = false;

    // @Accessors
    private boolean endFlag = false;

    private Thread loopThread;

    @Override
    public void run() {
        try {
            isRunning = true;

            while (true) {
                if (endFlag) {
                    // TODO end world function
                    System.out.println("endFlag:" + endFlag);
                    break;
                }

                // log.info("isRunning: {}, {}", this, isRunning);
                if (isRunning) {
                    world.run();
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.info("isRunning: {}, {}", this, isRunning);
            log.warn("InterruptedException");
            stopLoop();
        }
    }

    public void startLoopThread() throws InterruptedException {
        log.info("BaseInitializin afterPropertiesSet");
        loopThread = new Thread(this);
        loopThread.setName("Loop");
        loopThread.start();
    }

    public void beginLoop() throws InterruptedException {
        isRunning = true;
    }

    public void stopLoop() {
        isRunning = false;
    }

}
