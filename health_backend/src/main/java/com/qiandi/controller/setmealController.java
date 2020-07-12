package com.qiandi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qiandi.constant.MessageConstant;
import com.qiandi.constant.RedisConstant;
import com.qiandi.entity.PageResult;
import com.qiandi.entity.QueryPageBean;
import com.qiandi.entity.Result;
import com.qiandi.pojo.Setmeal;
import com.qiandi.service.SetmealService;
import com.qiandi.utils.HuaWeiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * 体检套餐管理
 *
 * @author fxc
 * @create 2020-07-03 15:02
 */
@RestController
@RequestMapping("/setmeal")
public class setmealController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;


    //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();//得到原始文件名 abc.jpg
        int index = originalFilename.lastIndexOf(".");//原始文件最后一个.所在的索引
        String extention = originalFilename.substring(index);// .jpg
        String fileName = UUID.randomUUID().toString() + extention;//自动生成主键  44e128a5-ac7a-4c9a-be4c-224b6bf81b20.jpg

        try {
            //将文件上传至华为云
            HuaWeiUtils.uploadByteHuaWei(fileName, imgFile.getBytes());
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }


    //新增套餐
    @RequestMapping("/add")
    public Result add(Integer[] checkgroupIds,@RequestBody Setmeal setmeal) {
        try {
            setmealService.add(checkgroupIds, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }




    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean);
    }



}
