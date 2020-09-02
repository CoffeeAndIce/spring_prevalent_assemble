package com.coffeeandice.entity;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @Todo 語速調整語句，存在語速等調整
 */
@XmlRootElement(name = "speak")
public class VoiceRateXml implements VoiceBase<VoiceRateXml> {
    private static final String Default = "default";

    @XmlAttribute(name = "version")
    private String version = "1.0";

    @XmlAttribute(name = "xmlns")
    private String xmlns = "http://www.w3.org/2001/10/synthesis";

    @XmlAttribute(name = "xml:lang")
    private String lang = "zh-HK";

    @XmlElement(name = "voice")
    private static Voice voice = new Voice();

    public VoiceRateXml() {
    }

    @Override
    public void voiceLang(String lang) {
        if (null != lang) {
            lang = lang;
        }
    }

    public VoiceRateXml VoiceSpeakName(String name) {
        if (null != name) {
            voice.name = name;
        }
        return this;
    }

    public VoiceRateXml(String name) {
        if (null != name) {
            voice.name = name;
        }
    }


    @Override
    public VoiceRateXml voiceText(String text) {
        voice.prosody.html = text;
        return this;
    }

    /**
     * 取值为0 ~1.0 ,默认值为defalut
     * x-slow,slow,medium,fast,x-fast
     * {@link Prosody#rate}
     *
     * @param rate
     * @return
     */
    public VoiceRateXml VoiceRate(String rate) {
        if (StringUtils.isNotEmpty(rate)) {
            voice.prosody.rate = rate;
        } else {
            voice.prosody.rate = Default;
        }
        return this;
    }

    /**
     * 音量可選值：0.0 ~ 100.0  or silent, x-soft,soft,medium,loud,x-loud
     * 默認值為defalut
     * {@link Prosody#volume}
     *
     * @param volume
     * @return
     */
    public VoiceRateXml VoiceVolume(String volume) {
        if (StringUtils.isNotEmpty(volume)) {
            voice.prosody.volume = volume;
        } else {
            voice.prosody.volume = Default;
        }
        return this;
    }

    /**
     * 音量可選值：0.0 ~ 100.0  or x-low,low,medium,high,x-high
     * 默認值為defalut
     * {@link Prosody#pitch}
     *
     * @param pitch
     * @return
     */
    public VoiceRateXml VoicePitch(String pitch) {
        if (StringUtils.isNotEmpty(pitch)) {
            voice.prosody.pitch = pitch;
        } else {
            voice.prosody.pitch = Default;
        }
        return this;
    }

    public static class Voice {

        @XmlAttribute(name = "name")
        private String name = "zh-HK-TracyRUS";

        @XmlElement(name = "prosody")
        private Prosody prosody = new Prosody();

    }

    public static class Prosody {

        @XmlAttribute(name = "rate")
        private String rate = "default";

        @XmlAttribute(name = "volume")
        private String volume = "default";

        @XmlAttribute(name = "pitch")
        private String pitch = "default";

        /**
         * 用於轉義成語音的文段
         */
        @XmlValue
        private String html = "This is awesome";
    }


}