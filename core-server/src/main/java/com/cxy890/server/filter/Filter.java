package com.cxy890.server.filter;

/**
 * @author BD-PC27
 */
public interface Filter {

    boolean permit() throws Exception;

    void before() throws Exception;
}
