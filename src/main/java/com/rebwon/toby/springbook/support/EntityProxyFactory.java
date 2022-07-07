package com.rebwon.toby.springbook.support;

import com.rebwon.toby.springbook.dao.GenericDao;

public interface EntityProxyFactory {

    <T> T createProxy(Class<T> clazz, GenericDao<T> dao, int id);
}
