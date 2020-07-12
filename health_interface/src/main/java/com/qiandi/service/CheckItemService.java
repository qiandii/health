package com.qiandi.service;

import com.qiandi.entity.PageResult;
import com.qiandi.entity.QueryPageBean;
import com.qiandi.pojo.CheckItem;

import java.util.List;

/**
 * @author fxc
 * @create 2020-06-28 9:19
 */
public interface CheckItemService {

    //添加检查项
    void add(CheckItem checkItem);

    //分页查询
    PageResult pagerQuery(QueryPageBean queryPageBean);

    //根据id删除检查项
    void deleteById(Integer id);

    //编辑检查项
    void edit(CheckItem checkItem);

    //回显检查项
    CheckItem findById(Integer id);

    //查询所有检查项
    List<CheckItem> findAll();
}
