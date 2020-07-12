package com.qiandi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qiandi.dao.CheckItemDao;
import com.qiandi.entity.PageResult;
import com.qiandi.entity.QueryPageBean;
import com.qiandi.pojo.CheckItem;
import com.qiandi.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 *
 * @author fxc
 * @create 2020-06-28 9:45
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    //注入DAO对象
    @Autowired
    private CheckItemDao checkItemDao;

    //新增检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }


    //检查项分页查询
    @Override
    public PageResult pagerQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();//页码
        Integer pageSize = queryPageBean.getPageSize();//每页记录数
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mabatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();//总记录数
        List<CheckItem> rows = page.getResult();//当前页结果

        return new PageResult(total, rows);
    }

    @Override
    //根据ID删除检查项
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        System.out.println(count);
        if(count > 0){
            throw new RuntimeException("当前检查项已经被关联到检查组，不允许删除");
        }
        checkItemDao.deleteById(id);
    }

    //编辑检查项
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    //回显检查项
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }


    //查询所有检查项
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}
