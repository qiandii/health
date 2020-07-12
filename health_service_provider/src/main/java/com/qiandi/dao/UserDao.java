package com.qiandi.dao;

import com.qiandi.pojo.User;

public interface UserDao {
    public User findByUsername(String username);
}
