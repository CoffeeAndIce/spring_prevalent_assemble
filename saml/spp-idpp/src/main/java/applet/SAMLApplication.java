package applet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: CoffeeAndIce
 * @date: 2022/06/03
 */
@EnableScheduling
@SpringBootApplication
@EnableAsync
public class SAMLApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SAMLApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(SAMLApplication.class);
	}


}
