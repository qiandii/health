package com.qiandi.service;

import com.qiandi.entity.PageResult;
import com.qiandi.entity.QueryPageBean;
import com.qiandi.pojo.Setmeal;

import java.util.List;

/**
 * @author fxc
 * @create 2020-07-03 16:29
 */
public interface SetmealService {

    //新增套餐
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);
}
