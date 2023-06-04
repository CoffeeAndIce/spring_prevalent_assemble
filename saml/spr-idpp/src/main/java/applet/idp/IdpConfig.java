package applet.idp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname IdpConfig
 * @Description TODO IDP 需要的基础内容(固定为单配置的模式，实际上都是支持多个配置的)
 * @Date 2022/6/22 14:13
 * @Created by CoffeeAndIce
 */
@Configuration
public class IdpConfig {
    /**
     * @desc IDP 元数据对应的实体类ID
     */
    @Value("${idp.testId:https://coffeeandice/idp/trust}")
    public String idp_entity_id;
    /**
     * @desc idp对应的 logon 端点
     */
    @Value("${ipd.logon:http://localhost:8088/idp/logon}")
    public String idp_sso_logon;
    /**
     * @desc idp 对应的logout 端点 （通常是可选）
     */
    @Value("${ipd.logout:http://localhost:8088/idp/logout}")
    public String idp_sso_logout;
    /**
     * @desc idp 对应的artifact service 端点 （通常是可选）
     * @extra 需要idp方开启 artifact service 服务
     */
    @Value("${ipd.artifact:http://localhost:8088/idp/logout}")
    public String idp_artifact_service;
}
