package com.coffeeandice.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


/**
 * @Todo 神經語言語句，存在特殊定制等調整
 * <p>
 * only support {southeastasia、eastus、eastus2、westeurope}
 */
@XmlRootElement(name = "speak")
public class VoiceNatureXml implements VoiceBase<VoiceNatureXml> {

    @XmlAttribute(name = "version")
    private String version = "1.0";

    @XmlAttribute(name = "xmlns")
    private String xmlns = "http://www.w3.org/2001/10/synthesis";

    @XmlAttribute(name = "xmlns:mstts")
    private String xmlns_mstts = "https://www.w3.org/2001/mstts";


    @XmlAttribute(name = "xml:lang")
    private String lang = "zh-HK";

    @XmlElement(name = "voice")
    private static Voice voice = new Voice();

    public VoiceNatureXml() {
    }

    @Override
    public void voiceLang(String name) {
        voice.name = name;
    }

    public VoiceNatureXml(String name) {
        if (null != name) {
            voice.name = name;
        }
    }

    @Override
    public VoiceNatureXml voiceText(String text) {
        voice.mstts.html = text;
        return null;
    }

    public static class Voice {

        @XmlAttribute(name = "name")
        private String name = "zh-HK-HiuGaaiNeural";

        @XmlElement(name = "mstts:express-as")
        private Mstts mstts = new Mstts();

    }

    public static class Mstts {

        @XmlAttribute(name = "style")
        private String style = "cheerful";

        /**
         * 用於轉義成語音的文段
         */
        @XmlValue
        private String html = "This is awesome";

    }


}
