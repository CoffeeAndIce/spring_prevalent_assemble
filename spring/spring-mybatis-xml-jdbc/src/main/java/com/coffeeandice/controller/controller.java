package com.coffeeandice.controller;

import com.coffeeandice.services.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * author : CoffeeAndIce
 */
@Controller
public class controller {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    @ResponseBody
    public String getUserList() {
        System.out.println(555);
        return JSONObject.toJSONString(userService.getUser());
    }
    @GetMapping("/page")
    public String view() {
        System.out.println(666);
       return "index";
    }

}
