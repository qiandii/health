package com.qiandi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qiandi.constant.MessageConstant;
import com.qiandi.dao.MemberDao;
import com.qiandi.dao.OrderDao;
import com.qiandi.dao.OrderSettingDao;
import com.qiandi.entity.Result;
import com.qiandi.pojo.Member;
import com.qiandi.pojo.Order;
import com.qiandi.pojo.OrderSetting;
import com.qiandi.service.OrderService;
import com.qiandi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fxc
 * @create 2020-07-08 8:34
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    @Override
    public Result order(Map map) throws Exception {
        //检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.getOrderSettingByDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            //预约已满，不能预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);

        //当前用户不是会员，需要添加到会员表
        if (member == null) {
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //是会员或者新注册为会员
        //检查重复预约
        Integer memberId = member.getId();
        int setmealId = Integer.parseInt((String) map.get("setmealId"));
        Order order = new Order(memberId, date, setmealId);
        List<Order> list = orderDao.findByCondition(order);
        if (list != null && list.size() > 0) {
            //已经完成了预约，不能重复预约
            return new Result(false, MessageConstant.HAS_ORDERED);
        }

        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //保存预约信息到预约表
        order = new Order(member.getId(),
                date,
                (String) map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }


    //根据id查询预约信息，包括体检人信息、套餐信息
    @Override
    public Map<String, Object> findById(Integer id) throws Exception {
        Map<String, Object> map = orderDao.findById4Detail(id);
        if (map != null) {
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
