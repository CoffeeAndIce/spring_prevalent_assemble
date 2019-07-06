package com.coffeeandice.services.services.impl;


import com.coffeeandice.entity.User;
import com.coffeeandice.mapper.UserMapper;
import com.coffeeandice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : CoffeeAndIce
 */
@Service
public class UserImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getUser() {
        return userMapper.selectUser();
    }
}
