package applet.sp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname SpConfig
 * @Description TODO  SP需要的基础内容
 * @Date 2022/6/22 14:36
 * @Created by CoffeeAndIce
 */
@Configuration
public class SpConfig {
	/**
	 * @desc SP 元数据对应的实体类ID
	 */
	@Value("${sp.testId:https://coffeeandice/sp/trust}")
	public String sp_entity_id;
	/**
	 * @desc SP 对应的消费地址，也就是正常的回调地址。（可以是登入/登出端点的一种）
	 */
	@Value("${sp.consumer:http://localhost:8088/sp/consumer}")
	public String sp_consumer;

    /**
     * @desc 授權會話鍵值名稱
     * @detail K-V 結構的K
     */
    @Value("${idp.AUTHENTICATED_SESSION_ATTRIBUTE:authenticated}")
    public String AUTHENTICATED_SESSION_ATTRIBUTE;
    /**
     * @desc 存儲跳轉地址的鍵值名稱
     * @detail K-V 結構的K
     */
    @Value("${idp.GOTO_URL_SESSION_ATTRIBUTE:gotoURL}")
    public String GOTO_URL_SESSION_ATTRIBUTE = "gotoURL";
}
