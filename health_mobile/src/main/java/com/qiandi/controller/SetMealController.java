package com.qiandi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qiandi.constant.MessageConstant;
import com.qiandi.entity.Result;
import com.qiandi.pojo.Setmeal;
import com.qiandi.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fxc
 * @create 2020-07-06 12:47
 */
@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetmealService setmealService;

    //查询所有套餐
    @RequestMapping("/getAllSetmeal")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }


    //根据id获取套餐信息(包括检查组，检查项)
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    //根据id获取套餐信息(不包括检查组，检查项)
    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id) {
        try {
            Setmeal setmeal = setmealService.getSetmealById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }


}
