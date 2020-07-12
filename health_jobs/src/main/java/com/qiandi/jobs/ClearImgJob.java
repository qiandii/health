package com.qiandi.jobs;

import com.qiandi.constant.RedisConstant;
import com.qiandi.utils.HuaWeiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 实现定时清理垃圾图片
 *
 * @author fxc
 * @create 2020-07-04 15:28
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        //sdiff():a和b的差集:"+jedis.sdiff("a","b"));//a中有，b中没有
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set != null) {
            for (String imgName : set) {
                //删除华为云服务器上的图片
                HuaWeiUtils.deleteFileFromHuaWei(imgName);
                //从Redis集合中删除图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, imgName);
                System.out.println("清理垃圾图片" + imgName);
            }
        }
    }

    public void clearRedis() {
        this.clearImg();
        //删除当前选择数据库中的所有key
        jedisPool.getResource().del(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

    }


}
