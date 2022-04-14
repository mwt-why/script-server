package com.mwt.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class DbUtil {

    /**
     * bean的class
     */
    private static final String CLASS_PROPERTY_NAME = "class";

    private DbUtil() {
    }

    public static UpdateDefinition getUpdate(Object o) {
        return getUpdate(o, Collections.EMPTY_LIST);
    }

    /**
     * 根据传入的对象生成update
     *
     * @param o
     * @param excludeProperty
     * @return
     */
    public static UpdateDefinition getUpdate(Object o, List<String> excludeProperty) {
        Objects.requireNonNull(o);
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        Update update = new Update();
        for (PropertyDescriptor pd : propertyDescriptors) {
            String name = pd.getName();
            //排除class属性
            if (CLASS_PROPERTY_NAME.equals(name)) {
                continue;
            }
            //排除需要排除的字段
            if (excludeProperty.contains(name)) {
                continue;
            }
            Object value = beanWrapper.getPropertyValue(name);
            //排除null和空字符串
            if (Objects.isNull(value) ||
                    (value instanceof String && StringUtils.isBlank(value.toString()))) {
                continue;
            }
            update.set(name, value);
        }
        return update;
    }

    /**
     * 根据id获取query
     *
     * @param id
     * @return
     */
    public static Query getQueryById(String id) {
        return getQuery("_id", id);
    }

    public static Query getQuery(String key, String value) {
        return Query.query(Criteria.where(key).is(value));
    }

    public static Query getSimpleQuery(Object o) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        Criteria criteria = new Criteria();
        for (PropertyDescriptor pd : propertyDescriptors) {
            String name = pd.getName();
            //排除class属性
            if (CLASS_PROPERTY_NAME.equals(name)) {
                continue;
            }
            Object value = beanWrapper.getPropertyValue(name);
            //排除null和空字符串
            if (Objects.isNull(value) ||
                    (value instanceof String && StringUtils.isBlank(value.toString()))) {
                continue;
            }

        }
        return new Query(criteria);
    }

}
