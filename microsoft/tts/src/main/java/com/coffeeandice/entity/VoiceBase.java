package com.coffeeandice.entity;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;


public interface VoiceBase<T> {

    /**
     * 设置输出内容
     *
     * @param text
     * @return
     */
    T voiceText(String text);

    /**
     * 设置语言
     * xml上的name值
     *
     * @param name
     */
    void voiceLang(String name);


    /**
     * 将对象直接转换成String类型的 XML输出
     *
     * @return
     */
    default String convertToXml() {
        Object obj = this;
        // 创建输出流
        StringWriter sw = new StringWriter();
        try {
            // 利用jdk中自带的转换类实现
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            // 格式化xml输出的格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // 将对象转换成输出流形式的xml
            marshaller.marshal(obj, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

}
