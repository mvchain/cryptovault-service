package com.mvc.cryptovault.console.common;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T> implements BaseService<T> {

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Autowired
    protected MyMapper<T> mapper;
    private Class<T> modelClass;

    public AbstractService() {
        // 获得具体model，通过反射来根据属性条件查找数据
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public void save(T model) {
        mapper.insertSelective(model);
    }

    @Override
    public void save(List<T> models) {
        mapper.insertList(models);
    }

    @Override
    public void deleteById(BigInteger id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    @Override
    public void update(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public T findById(BigInteger id) {
        T obj = null;
        String key = modelClass.getSimpleName().toUpperCase() + "_" + id;
        String json = redisTemplate.opsForValue().get(key);
        if (null == json) {
            obj = mapper.selectByPrimaryKey(id);
            if (null == obj) {
                redisTemplate.opsForValue().set(key, "", 30, TimeUnit.MINUTES);
            } else {
                redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), 24, TimeUnit.HOURS);
            }
        } else if ("".equals(json)) {
            obj = null;
        } else {
            obj = JSON.parseObject(json, modelClass);
        }
        return obj;
    }

    @Override
    public T findBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.selectOne(model);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    @Override
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Override
    public List<T> findByCondition(T t) {
        return mapper.select(t);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }
}