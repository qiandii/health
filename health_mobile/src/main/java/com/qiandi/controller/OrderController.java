package com.qiandi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qiandi.constant.MessageConstant;
import com.qiandi.constant.RedisMessageConstant;
import com.qiandi.entity.Result;
import com.qiandi.pojo.Order;
import com.qiandi.service.OrderService;
import com.qiandi.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;


/**
 * @author fxc
 * @create 2020-07-07 20:15
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    //体检预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {

        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_GETPWD);
        //校验手机验证码
        if (codeInRedis != null && codeInRedis.equals(validateCode)) {
            //验证码正确，调用服务完成预约任务
            Result result = new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            try {
                map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型；电话预约/微信预约
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
                //预约失败
                return result;
            }
            if (result.isFlag()) {
                try {
                    SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, "success");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }


    //根据id查询预约信息，包括套餐信息和会员信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map<String,Object> map = orderService.findById(id);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }



}
