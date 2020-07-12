package com.qiandi.service;

import com.qiandi.entity.Result;

import java.util.Map;

/**
 * @author fxc
 * @create 2020-07-07 20:42
 */
public interface OrderService {
    Result order(Map map) throws Exception;

    Map<String, Object> findById(Integer id) throws Exception;
}
