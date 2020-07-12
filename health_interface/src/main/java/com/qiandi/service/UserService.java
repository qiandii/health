package com.qiandi.service;

import com.qiandi.pojo.User;

public interface UserService {
    public User findByUsername(String username);
}
