package com.cxy890.cxy;

import com.cxy890.cxy.annotation.CxyConfig;
import com.cxy890.cxy.annotation.ForceAssign;
import com.cxy890.cxy.config.Configqww;
import com.cxy890.cxy.global.CxyApplication;
import com.cxy890.cxy.runner.Runner;

/**
 * Application入口
 *
 * Created by ChangXiaoyang on 2017/5/17.
 */
@CxyConfig
public class Application implements Runner {

    @ForceAssign
    private Configqww configqww;

    /**
     * 工程main入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        CxyApplication.run(Application.class, args);
    }

    @Override
    public void run() {
        configqww.aa();
    }
}
