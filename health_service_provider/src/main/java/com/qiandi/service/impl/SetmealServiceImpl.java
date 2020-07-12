package com.qiandi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qiandi.constant.RedisConstant;
import com.qiandi.dao.SetmealDao;
import com.qiandi.entity.PageResult;
import com.qiandi.entity.QueryPageBean;
import com.qiandi.pojo.Setmeal;
import com.qiandi.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fxc
 * @create 2020-07-04 7:44
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")
    private String outPutPath;//从属性文件中读取要生成的html对应的目录


    //新增套餐,关联检查组
    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();//获取套餐id
        this.setCheckGroupAndCheckItem(setmealId, checkgroupIds);
        //将图片名称保存在Redis集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
        //当添加套餐后需要重新生成静态页面（套餐列表页面、套餐详情页面）
        generateMobileStaticHtml();
    }

    //页面静态化（复制的）
    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml() {
        //在生成静态页面之前需要查询数据
        List<Setmeal> list = setmealDao.findAll();

        //需要生成套餐列表静态页面
        generateMobileSetmealListHtml(list);

        //需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        Map map = new HashMap();
        //为模板提供数据，用于生成静态页面
        map.put("setmealList",list);
        generteHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐详情静态页面（可能有多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            map.put("setmeal",setmealDao.findById(setmeal.getId()));
            generteHtml("mobile_setmeal_detail.ftl","setmeal_detail_" + setmeal.getId() + ".html",map);
        }
    }


    //通用的方法，用于生成静态页面
    public void generteHtml(String templateName,String htmlPageName,Map map){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();//获得配置对象
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            //构造输出流
            out = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            //输出文件
            template.process(map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //页面静态化完成


    //建立检查组和套餐多对多关系
    public void setCheckGroupAndCheckItem(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            Map<String, Integer> map = null;
            for (Integer checkgroupId : checkgroupIds) {
                map = new HashMap<>();
                map.put("setmealId", setmealId);
                map.put("checkgroupId", checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }


    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();//页码
        Integer pageSize = queryPageBean.getPageSize();//每页记录数
        String queryString = queryPageBean.getQueryString();//查询条件
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //查询所有套餐
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //根据id获取套餐信息(包括检查组，检查项)
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    //根据id获取套餐信息(不包括检查组，检查项)
    @Override
    public Setmeal getSetmealById(Integer id) {
        return setmealDao.getSetmealById(id);
    }


}
