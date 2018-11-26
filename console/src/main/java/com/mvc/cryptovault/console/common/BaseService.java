package com.mvc.cryptovault.console.common;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigInteger;
import java.util.List;

public interface BaseService<T> {
    /**
     * 持久化
     *
     * @param model
     */
    void save(T model);

    /**
     * 批量持久化
     *
     * @param models
     */
    void save(List<T> models);

    /**
     * 通过主鍵刪除
     *
     * @param id
     */
    void deleteById(BigInteger id);

    /**
     * 批量刪除 eg：ids -> “1,2,3,4”
     *
     * @param ids
     */
    void deleteByIds(String ids);

    /**
     * 更新
     *
     * @param model
     */
    void update(T model);

    /**
     * 通过ID查找
     *
     * @param id
     * @return
     */
    T findById(BigInteger id);

    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
     *
     * @param fieldName
     * @param value
     * @return
     * @throws TooManyResultsException
     */
    List<T> findBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
     *
     * @param fieldName
     * @param value
     * @return
     * @throws TooManyResultsException
     */
    T findOneBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * 通过多个ID查找//eg：ids -> “1,2,3,4”
     *
     * @param ids
     * @return
     */
    List<T> findByIds(String ids);

    /**
     * 根据条件查找
     *
     * @param t
     * @return
     */
    List<T> findByEntity(T t);

    /**
     * 根据条件查找
     *
     * @param t
     * @return
     */
    T findOneByEntity(T t);

    /**
     * 根据条件查找
     *
     * @param t
     * @return
     */
    List<T> findByCondition(Condition t);

    /**
     * 获取所有
     *
     * @return
     */
    List<T> findAll(String... orderBy);

    /**
     * 更新缓存
     */
    void updateAllCache(String... orderBy);

    /**
     * 更新指定缓存
     * @param pvKey
     */
    void updateCache(Object pvKey);
}