package com.cxy890.cxy.config;

import com.cxy890.cxy.annotation.CxyConfig;
import com.cxy890.cxy.annotation.Value;

/**
 * Created by ChangXiaoyang on 2017/5/17.
 */
@CxyConfig
public class Configqww {

    @Value("summer.test.value")
    private String test;

    public void aa(){
        System.out.println("test:" + test);
    }
}
