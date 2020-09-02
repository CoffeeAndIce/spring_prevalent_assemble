package com.coffeeandice.entity;

/**
 * @Classname SpeechConst
 * @Description TODO 暫時沒用上，是狀態的信息
 * @Date 2020/9/1 1:50
 * @Created by CoffeeAndIce
 */
public enum SpeechConst {
    BAD_REQUSET(400, "必需的参数丢失，为空或为null。或者，传递给必需或可选参数的值无效。一个常见的问题是标题太长。"),

    UN_AUTH(401, "该请求未被授权。检查以确保您的订阅密钥或令牌有效并且在正确的区域中"),

    BIG_ENTITY(413,"SSML输入超过1024个字符。"),

    UN_SUPPORT_MEDIA(415,"可能Content-Type提供了错误的信息。Content-Type应该设置为application/ssml+xml。"),

    BIG_REQUEST(429,"您已超出订阅允许的配额或请求速率。"),

    BAD_GATEWAY(502,"网络或服务器端问题。也可能指示无效的标题"),

    ;

    private int code;

    private String desc;

    private SpeechConst(int code, String desc) {

    }

}
