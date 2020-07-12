package com.qiandi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qiandi.dao.MemberDao;
import com.qiandi.pojo.Member;
import com.qiandi.service.MemberService;
import com.qiandi.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author fxc
 * @create 2020-07-09 9:42
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    //通过手机号获取会员信息
    @Override
    public Member findBytelephone(String telephone) {

        Member member = memberDao.findByTelephone(telephone);
        //不是会员
        if (member == null){
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            add(member);
        }
        return member;
    }


    //保存会员信息
    public void add(Member member) {
        if(member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }


}
