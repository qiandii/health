package com.qiandi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qiandi.dao.OrderSettingDao;
import com.qiandi.pojo.OrderSetting;
import com.qiandi.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fxc
 * @create 2020-07-05 14:20
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    //批量导入预约设置数据
    public void add(List<OrderSetting> list) {
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate > 0) {
                    //已经进行了预约设置，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    //没有进行预约设置，执行插入操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //根据月份查询预约数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String begin = date + "-1";
        String end = date + "-31";
        Map<String, Object> map = null;
        map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            Calendar calendar = Calendar.getInstance();
            for (OrderSetting orderSetting : list) {
                map = new HashMap<>();
                calendar.setTime(orderSetting.getOrderDate());
                int dateTime = calendar.get(Calendar.DATE);
                map.put("date", dateTime);
                map.put("number", orderSetting.getNumber());
                map.put("reservations", orderSetting.getReservations());
                result.add(map);
            }
        }
        return result;
    }

    @Override
    //根据日期设置对应的预约设置数据
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行了预约设置
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        if(count > 0){
            //当前日期已经进行了预约设置，需要执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //当前日期没有就那些预约设置，需要执行插入操作
            orderSettingDao.add(orderSetting);
        }
    }


}
