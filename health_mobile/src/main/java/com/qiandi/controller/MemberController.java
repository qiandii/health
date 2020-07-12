package com.qiandi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qiandi.constant.MessageConstant;
import com.qiandi.constant.RedisMessageConstant;
import com.qiandi.entity.Result;
import com.qiandi.pojo.Member;
import com.qiandi.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author fxc
 * @create 2020-07-09 9:13
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;


    //使用手机号和验证码登录
    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取缓存的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //验证码不存在或验证码不正确
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //登录
        Member member = memberService.findBytelephone(telephone);

        //写入Cookie，跟踪用户
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setPath("/");//路径  '/'所有的请求都会带入Cookie.如果设置为'/abc' 则只有含有/abc的请求会带入Cookie.如localhost:8080/abc/...
        cookie.setMaxAge(60 * 60 * 24 * 30);//有效期30天
        response.addCookie(cookie);
        //保存会员信息到Redis中.
        // 为什么使用Json
        // 1.如果对象中有级联关系，则反序列化时会报错
        // 2.有transient修饰的属性不会被序列化，反序列化时会丢失数据
        //fastJson提供的方法 把member对象序列化为json串
        String json = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone, 60 * 30, json);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }


}
