package com.cxy890.stater;

import com.cxy890.config.loader.context.ContextLoader;
import com.cxy890.config.loader.context.CxyContext;
import com.cxy890.config.loader.environment.YmlHelper;
import com.cxy890.stater.funny.EasterEgg;
import com.cxy890.stater.support.SimpleStopWatch;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;

/**
 * @author ChangXiaoyang
 */
@Slf4j
public class ApplicationStater {

    private Class<?> mainClass;

    private String[] args;

    public ApplicationStater(Class<?> mainClass) {
        this.mainClass = mainClass;
    }

    public ApplicationStater(Class<?> mainClass, String...args) {
        this.mainClass = mainClass;
        this.args = args;
    }

    public void start() {
        SimpleStopWatch watch = new SimpleStopWatch(true);
        before();
        log.info(String.format("Prepare complete, use time :%s ms", watch.issueAndReset()));

        CxyContext.startServer();
        CxyContext.runnerStart();
        log.info(String.format("Server started, use time :%s ms", watch.totalCostMillis()));
    }

    public static void start(Class<?> mainApplication, String ...args) {
        new ApplicationStater(mainApplication, args).start();
    }

    private void before() {
        EasterEgg.printBanner();
        if (YmlHelper.load(mainClass))
            log.info("Environment loadApplication complete.");

        if (ContextLoader.loadFramework())
            log.info("Framework configuration load complete.");

        Annotation[] annotations = this.mainClass.getAnnotations();
        for (Annotation annotation : annotations)
            if (ContextLoader.loadImport(annotation))
                log.info("Import configuration load complete.");

        if (ContextLoader.loadApplication())
            log.info("Application configuration load complete.");

        CxyContext.initStuff();
    }

}
