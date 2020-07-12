package com.qiandi.dao;

import com.github.pagehelper.Page;
import com.qiandi.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @author fxc
 * @create 2020-06-30 13:55
 */
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    Page<CheckGroup> findByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssocication(Integer id);

    void delete(Integer id);

    List<CheckGroup> findAll();
}
