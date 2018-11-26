package com.mvc.cryptovault.console.common;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import tk.mybatis.mapper.entity.Condition;

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
    protected HTreeMap hTreeMap;

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
        String key = modelClass.getSimpleName().toUpperCase() + "_" + id;
        mapper.deleteByPrimaryKey(id);
        redisTemplate.opsForValue().set(key, "", 24, TimeUnit.HOURS);
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
                //填入空内容,防止穿透
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
    public T findOneBy(String fieldName, Object value) throws TooManyResultsException {
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
    public List<T> findBy(String fieldName, Object value) throws TooManyResultsException {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.select(model);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    @Override
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Override
    public List<T> findByCondition(Condition t) {
        return mapper.selectByCondition(t);
    }

    @Override
    public List<T> findByEntity(T t) {
        return mapper.select(t);
    }

    @Override
    public T findOneByEntity(T t) {
        return mapper.selectOne(t);
    }

    /**
     * 通常都走单独的按条件搜索。搜索所有的都是少量数据，因此直接在堆中缓存
     *
     * @return
     */
    @Override
    public List<T> findAll(String... orderBy) {
        if (null != orderBy) {
            PageHelper.startPage(0, 9999, orderBy[0]);
        } else {
            PageHelper.startPage(0, 9999);
        }
        String key = modelClass.getSimpleName().toUpperCase();
        List<T> list = null;
        list = (List<T>) hTreeMap.get(key);
        if (null == list) {
            list = mapper.selectAll();
            hTreeMap.put(key, list);
        }
        return list;
    }

    @Override
    public void updateAllCache(String... orderBy) {
        if (null != orderBy && orderBy.length > 0) {
            PageHelper.startPage(0, 9999, orderBy[0]);
        } else {
            PageHelper.startPage(0, 9999);
        }
        String key = modelClass.getSimpleName().toUpperCase();
        PageHelper.startPage(0, 9999);
        List<T> list = mapper.selectAll();
        hTreeMap.put(key, list);
    }

    @Override
    public void updateCache(Object pvKey) {
        String key = modelClass.getSimpleName().toUpperCase() + "_" + pvKey;
        T obj = mapper.selectByPrimaryKey(pvKey);
        if (null == obj) {
            //填入空内容,防止穿透
            redisTemplate.opsForValue().set(key, "", 30, TimeUnit.MINUTES);
        } else {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), 24, TimeUnit.HOURS);
        }
    }
}