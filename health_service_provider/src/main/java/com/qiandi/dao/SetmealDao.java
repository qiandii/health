package com.qiandi.dao;

import com.github.pagehelper.Page;
import com.qiandi.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author fxc
 * @create 2020-07-04 7:45
 */
public interface SetmealDao {

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> findByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);
}
