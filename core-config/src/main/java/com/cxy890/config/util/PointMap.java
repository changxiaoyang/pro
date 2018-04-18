package com.cxy890.config.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 三维点Map， 新的一种Map实现， 具体作用未知。。。
 *
 * Created by ChangXiaoyang on 2017/5/20.
 */
public class PointMap<Z, X, Y> implements Map<X, Y> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Y get(Object key) {
        return null;
    }

    @Override
    public Y put(X key, Y value) {
        return null;
    }

    @Override
    public Y remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends X, ? extends Y> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<X> keySet() {
        return null;
    }

    @Override
    public Collection<Y> values() {
        return null;
    }

    @Override
    public Set<Entry<X, Y>> entrySet() {
        return null;
    }
}
