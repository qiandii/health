package com.qiandi.dao;

import com.github.pagehelper.Page;
import com.qiandi.pojo.CheckItem;

import java.util.List;

/**
 * @author fxc
 * @create 2020-06-28 9:49
 */
public interface CheckItemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
