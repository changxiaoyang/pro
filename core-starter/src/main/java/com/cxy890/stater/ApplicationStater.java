package com.cxy890.stater;

import com.cxy890.config.GlobalCxyConfig;
import com.cxy890.config.loader.ContextLoader;
import com.cxy890.config.loader.CxyContext;
import com.cxy890.config.loader.environment.EnvironmentLoader;
import com.cxy890.stater.funny.EasterEgg;
import com.cxy890.stater.support.SimpleStopWatch;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author BD-PC27
 */
@Slf4j
public class ApplicationStater {

    public static void start(String ...args) throws Throwable {
        SimpleStopWatch watch = new SimpleStopWatch(true);
        ApplicationStater stater = new ApplicationStater();
        stater.before();
        log.info(String.format("Prepare complete, use time :%s ms", watch.issueAndReset()));

        ContextLoader.load("com.cxy890");
        EnvironmentLoader.load();
        CxyContext.initBeans();
        GlobalCxyConfig.runnerStart();
        GlobalCxyConfig.startServer(ApplicationStater.class);
    }

    private void before() throws IOException {
        log.info("Welcome \n\r" + EasterEgg.getBanner());
        EnvironmentLoader.load();
        log.info("Environment load complete");
    }

    public static void main(String[] args) throws Throwable {
        start();
    }

}
