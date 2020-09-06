package com.coffeeandice.target;

/**
 * @author : CoffeeAndIce
 * @Todo:
 */

public class TargetServiceImpl implements TargetService {
    @Override
    public void testAdvice(String param) {
        System.out.println("方法进来了,参数是:"+param);
    }

    @Override
    public void testExceptAdvice(String param) {
        System.out.println("方法进来了,即将异常，参数是:"+param);
         int j = 1 / 0;
    }
}
