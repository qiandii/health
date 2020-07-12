package com.qiandi.service;

import com.qiandi.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fxc
 * @create 2020-07-05 10:53
 */
public interface OrderSettingService {


    void add(List<OrderSetting> list);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);



}
