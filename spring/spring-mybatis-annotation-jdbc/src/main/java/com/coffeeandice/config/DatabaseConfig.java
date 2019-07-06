package com.coffeeandice.config;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;


/**
* author : CoffeeAndIce
*/
@Configuration
@EnableTransactionManagement // 开启声明式事务处理 等价于xml中<tx:annotation-driven/>
@ComponentScan(basePackages = {"com.coffeeandice.*"})
public class DatabaseConfig {

    /* @Autowired
     * private DataSourceConfig sourceConfig;
     * 不要采用这种方式注入DataSourceConfig,由于类的加载顺序影响会报空指针异常
     * 最好的方式是在DriverManagerDataSource构造中采用参数注入
     */

    /**
     * 配置数据源
     */
    @Bean
    public DriverManagerDataSource dataSource(DataSourceConfig sourceConfig) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(sourceConfig.getDriverClassName());
        dataSource.setUrl(sourceConfig.getUrl());
        dataSource.setUsername(sourceConfig.getUsername());
        dataSource.setPassword(sourceConfig.getPassword());
        return dataSource;
    }


    /**
     * 配置mybatis 会话工厂
     *
     * @param dataSource 这个参数的名称需要保持和上面方法名一致 才能自动注入,因为
     *                   采用@Bean注解生成的bean 默认采用方法名为名称，当然也可以在使用@Bean时指定name属性
     */
    @Bean
    public SqlSessionFactoryBean sessionFactoryBean(DriverManagerDataSource dataSource) throws IOException {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/**/*.xml"));

        //这里可以采取配置文件
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        //   sessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatisConfig.xml"));
        //开启驼峰命名
        configuration.setMapUnderscoreToCamelCase(true);
        //打印查询
        configuration.setLogImpl(StdOutImpl.class);
        //更多settings配置项可以参考官方文档: http://www.mybatis.org/mybatis-3/zh/configuration.html
        sessionFactoryBean.setConfiguration(configuration);
        sessionFactoryBean.setTypeAliasesPackage("com.coffeeandice.entity");
        return sessionFactoryBean;
    }

    /**
     * 配置mybatis 会话工厂
     */
    @Bean
    public MapperScannerConfigurer MapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("sessionFactoryBean");
        configurer.setBasePackage("com.coffeeandice.mapper");
        return configurer;
    }

    /**
     * 定义事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DriverManagerDataSource dataSource) {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }


}
