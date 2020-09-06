package com.coffeeandice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author : CoffeeAndIce
 * @Todo: 相当于替代了配置文件
 */

@Configuration
@ComponentScan("com.coffeeandice.*")
@EnableAspectJAutoProxy
public class AspectConfig {
}
