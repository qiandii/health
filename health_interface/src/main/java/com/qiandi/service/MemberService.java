package com.qiandi.service;

import com.qiandi.pojo.Member;

/**
 * @author fxc
 * @create 2020-07-09 9:15
 */
public interface MemberService {


    Member findBytelephone(String telephone);
}
