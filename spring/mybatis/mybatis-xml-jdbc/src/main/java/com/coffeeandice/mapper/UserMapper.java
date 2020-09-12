package com.coffeeandice.mapper;

import com.coffeeandice.entity.User;

import java.util.List;

/**
* author : CoffeeAndIce
*/
public interface UserMapper {
    List<User> selectUser();
}
