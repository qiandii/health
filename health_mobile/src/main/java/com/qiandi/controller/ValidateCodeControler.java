package com.qiandi.controller;

import com.qiandi.constant.MessageConstant;
import com.qiandi.constant.RedisMessageConstant;
import com.qiandi.entity.Result;
import com.qiandi.utils.SMSUtils;
import com.qiandi.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import static com.qiandi.utils.SMSUtils.sendShortMessage;

/**
 * @author fxc
 * @create 2020-07-07 16:03
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeControler {


    @Autowired
    private JedisPool jedisPool;


    public static void main(String[] args) {
        ValidateCodeControler validateCodeControler = new ValidateCodeControler();
        validateCodeControler.send4Order("13190024896");
    }

    //用户在线体检预约发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try {
            //阿里云发送验证码
            sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将生成的验证码缓存到redis
        //setex定时保存(String key,int time,String value)
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_GETPWD,300,validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


    //用户登录发送验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //生成6位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            //阿里云发送验证码
            sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }



}
