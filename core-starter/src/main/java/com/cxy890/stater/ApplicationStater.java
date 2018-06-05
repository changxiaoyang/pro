package com.cxy890.stater;

import com.cxy890.config.GlobalCxyConfig;
import com.cxy890.config.annotation.AppStater;
import com.cxy890.config.loader.context.ContextLoader;
import com.cxy890.config.loader.context.CxyContext;
import com.cxy890.config.loader.environment.EnvironmentLoader;
import com.cxy890.config.util.StringUtil;
import com.cxy890.stater.funny.EasterEgg;
import com.cxy890.stater.support.SimpleStopWatch;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * @author BD-PC27
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

    public void start() throws Throwable {
        SimpleStopWatch watch = new SimpleStopWatch(true);
        before();
        log.info(String.format("Prepare complete, use time :%s ms", watch.issueAndReset()));

        CxyContext.startServer();
        CxyContext.runnerStart();
        log.info(String.format("Server started, use time :%s ms", watch.issueAndReset()));
    }

    public static void start(Class<?> mainApplication, String ...args) throws Throwable {
        new ApplicationStater(mainApplication, args).start();
    }

    private void before() throws IOException {
        EasterEgg.printBanner();
        if (EnvironmentLoader.load())
            log.info("Environment loadApplication complete");

        if (ContextLoader.loadFramework())
            log.info("Framework configuration load complete");
        Annotation[] annotations = this.mainClass.getAnnotations();
        for (Annotation annotation : annotations)
            if (ContextLoader.loadImport(annotation))
                log.info("Import configuration load complete");

        if (ContextLoader.loadApplication(getBasePath()))
            log.info("Application configuration load complete");

        CxyContext.initBeans();
    }

    private String getBasePath() {
        String baseScanPackage = null;
        if (mainClass.isAnnotationPresent(AppStater.class)) {
            AppStater appStater = mainClass.getAnnotation(AppStater.class);
            baseScanPackage = appStater.basePackage();
        }
        if (StringUtil.isNull(baseScanPackage))
            baseScanPackage = mainClass.getPackage().getName();
        return baseScanPackage;
    }

    public static void main(String[] args) throws Throwable {
        start(ApplicationStater.class, args);
    }

}
