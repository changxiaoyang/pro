package com.cxy890.elasticsearch;

import com.cxy890.config.annotation.Import;
import com.cxy890.elasticsearch.client.Clients;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author BD-PC27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(Clients.class)
public @interface EnableElasticsearch {

}
