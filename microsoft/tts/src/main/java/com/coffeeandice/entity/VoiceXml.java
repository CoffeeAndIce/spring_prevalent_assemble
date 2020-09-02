package com.coffeeandice.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


/**
 * @Todo 普通解析語句，不存在語速等調整
 */
@XmlRootElement(name = "speak")
public class VoiceXml implements VoiceBase<VoiceXml> {

    @XmlAttribute(name = "version")
    private String version = "1.0";

    @XmlAttribute(name = "xmlns")
    private String xmlns = "http://www.w3.org/2001/10/synthesis";

    @XmlAttribute(name = "xml:lang")
    private String lang = "zh-HK";

    @XmlElement(name = "voice")
    private static Voice voice = new Voice();

    public VoiceXml() {
    }

    @Override
    public void voiceLang(String name) {
        voice.name = name;
    }

    public VoiceXml(String name) {
        if (null != name) {
            voice.name = name;
        }
    }


    @Override
    public VoiceXml voiceText(String text) {
        voice.html = text;
        return null;
    }

    public static class Voice {

        @XmlAttribute(name = "name")
        private String name = "zh-HK-TracyRUS";

//        @XmlElement(name = "prosody",required = false)
//        private Prosody prosody;

        /**
         * 用於轉義成語音的文段
         */
        @XmlValue
        private String html = "This is awesome";
    }

    public static class Prosody {

        @XmlAttribute(name = "prosody")
        private String style = "cheerful";

        /**
         * 用於轉義成語音的文段
         */
        @XmlElement
        private String html = "This is awesome";

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }
    }


}
