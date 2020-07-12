package com.qiandi.service;

import com.qiandi.entity.PageResult;
import com.qiandi.entity.QueryPageBean;
import com.qiandi.pojo.CheckGroup;

import java.util.List;

/**
 * @author fxc
 * @create 2020-06-30 10:41
 */
public interface CheckGroupService {

    void add(Integer[] checkitemIds,CheckGroup checkGroup);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(Integer[] checkitemIds, CheckGroup checkGroup);

    void delete(Integer id);

    List<CheckGroup> findAll();
}
