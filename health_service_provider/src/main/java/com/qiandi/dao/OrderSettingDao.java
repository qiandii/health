package com.qiandi.dao;

import com.qiandi.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fxc
 * @create 2020-07-05 14:27
 */
public interface OrderSettingDao {

    void add(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    long findCountByOrderDate(Date orderDate);
    List<OrderSetting> getOrderSettingByMonth(Map map);
    OrderSetting getOrderSettingByDate(Date date);
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
