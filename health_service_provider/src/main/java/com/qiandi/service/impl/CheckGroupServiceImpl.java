package com.qiandi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qiandi.dao.CheckGroupDao;
import com.qiandi.entity.PageResult;
import com.qiandi.entity.QueryPageBean;
import com.qiandi.pojo.CheckGroup;
import com.qiandi.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fxc
 * @create 2020-06-30 11:15
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;


    //新增检查组,同时让检查组关联检查项
    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        //新增检查组checkGroup
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的关联：多对多checkitemIds
        //通过新增检查组中selectKey将id加入到checkGroup中，在通过checkGroup的getId()方法获取组自增id
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();//页码
        Integer pageSize = queryPageBean.getPageSize();//每页记录数
        String queryString = queryPageBean.getQueryString();//查询条件
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //根据ID查询检查组信息
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }


    //根据检查组ID查询关联的多个检查项ID，查询中间关系表
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }


    //编辑检查组,关联检查项
    @Override
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        //修改检查组基本信息，操作检查组t_checkgroup表
        checkGroupDao.edit(checkGroup);
        //清理当前检查组关联的检查项，操作中间关系表t_checkgroup_checkitem表
        checkGroupDao.deleteAssocication(checkGroup.getId());
        //重新建立当前检查组和检查项的关联关系
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    //根据检查组ID删除检查组
    @Override
    public void delete(Integer id) {
        //清理当前检查组关联的检查项，操作中间关系表t_checkgroup_checkitem表
        checkGroupDao.deleteAssocication(id);
        //删除检查组
        checkGroupDao.delete(id);
    }

    //查询所有检查组
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }


    //建立检查组和检查项多对多关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length > 0){
            Map<String, Integer> map = null;
            for (Integer checkitemId : checkitemIds) {
                 map = new HashMap<>();
                map.put("checkGroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }



}
