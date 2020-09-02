package com.coffeeandice.util;

import com.coffeeandice.entity.VoiceBase;
import com.coffeeandice.entity.VoiceNatureXml;
import com.coffeeandice.entity.VoiceRateXml;
import com.coffeeandice.entity.VoiceXml;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.util.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Classname SpeechSynthesizerConfig
 * @Description 配置转换类使用   文本转语音
 * @Date 2020/8/26 11:09
 * @Created by CoffeeAndIce
 * SubscKey 、 Location 需要根据自己情况自行填充
 * <p>
 * 1、config sex and lang method,default use #Man if not set
 * {@link #_translang}
 * <p>
 * 2、Convert the xml file to String format,paramters can be file or absolutePath
 * {@link #xmlToString}
 * <p>
 * 3、parse the XMl format to voice ,the xml will be modify by customer
 * {@link #parseHtmlToVoice}
 * <p>
 * <p>
 * 4、 Here are two ways to trans the text to voice
 * first is simple text
 * 1）{@link #transform}
 * <p>
 * second is put text to xml and transform it,call ssml
 * 2)
 */
public class SpeechSynthesizerUtil {
    private static final Logger logger = LoggerFactory.getLogger(SpeechSynthesizerUtil.class);

    public static final String SubscKey = "yourSubscKey";
    //PropertiesUtils.get
    public static final String Location = "yourlocation";

    private static final int VOICE_NOMAL = 1;

    private static final int VOICE_Rate = 2;

    private static final int VOICE_NATURE = 3;

    private static final String MAN = "man";

    private static final String HUMAN = "human";

    /**
     * 简体
     */
    public static final String SC = "sc";

    public static final String EN = "en";

    public static final String Hk = "hk";

    public static SpeechConfig speechConfig;

    public SpeechSynthesizerUtil() {
        speechConfig = SpeechConfig.fromSubscription(SubscKey, Location);
    }

    /**
     * @param transformText 需要转换的文本
     * @param filePath      存放的位置及其文件名路径
     * @param lang          语言，默认为Hk
     * @return 返回若为空，则是转换失败，否则为路径地址
     */
    public static StringBuffer transform(String transformText,
                                         String filePath,
                                         String lang,
                                         String sex) throws ExecutionException, InterruptedException {
        Boolean handlerStatus = Boolean.TRUE;
        StringBuffer handlerRes = null;

        StringBuffer absolutePath = new StringBuffer(filePath);
        logger.info("進入解析程序");
        logger.info("注冊程序訂閲");
        speechConfig = _translang(speechConfig, lang, sex);
        AudioConfig audioConfig = AudioConfig.fromWavFileOutput(absolutePath.toString());
        SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig, audioConfig);

        //完成和开始属于同一个事件,网络连接失败或中断的时候，触发取消事件

        synthesizer.SynthesisCompleted.addEventListener(new EventHandler<SpeechSynthesisEventArgs>() {
            @Override
            public void onEvent(Object o, SpeechSynthesisEventArgs speechSynthesisEventArgs) {
                logger.info("完成");
            }
        });

        synthesizer.SynthesisCanceled.addEventListener(new EventHandler<SpeechSynthesisEventArgs>() {
            @Override
            public void onEvent(Object o, SpeechSynthesisEventArgs speechSynthesisEventArgs) {
                logger.info("取消");
            }
        });
        SpeechSynthesisResult result = synthesizer.SpeakTextAsync(transformText).get();

        while (handlerStatus) {
            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                System.out.println("Speech synthesized to speaker for text [" + transformText + "]");
                handlerStatus = Boolean.FALSE;
                handlerRes = absolutePath;
                synthesizer.close();
                speechConfig.close();
                audioConfig.close();

            } else if (result.getReason() == ResultReason.Canceled) {
                SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
                System.out.println("CANCELED: Reason=" + cancellation.getReason());

                if (cancellation.getReason() == CancellationReason.Error) {
                    System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                    System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                    System.out.println("CANCELED: Did you update the subscription info?");
                }
                handlerStatus = Boolean.FALSE;
                synthesizer.close();
                speechConfig.close();
                audioConfig.close();
                result.close();
            }
        }
        System.out.println("返回");
        return handlerRes;
    }

    public static StringBuffer transform(String transformText,
                                         String filePath,
                                         String lang,
                                         String sex,
                                         Integer style,
                                         Map<String, String> rateMap) throws IOException, ExecutionException, InterruptedException {
        Boolean handlerStatus = Boolean.TRUE;
        StringBuffer handlerRes = null;
        speechConfig.setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.Riff24Khz16BitMonoPcm);
        _translang(speechConfig, lang, sex);
        speechConfig.requestWordLevelTimestamps();
        //以流的方式進行獲取結果
        SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig, null);

        StringBuffer curFolder = new StringBuffer(filePath.substring(0, filePath.lastIndexOf("/")));
        long start = System.currentTimeMillis();
        String absolutePath = new File(curFolder.toString()).getAbsolutePath();
        curFolder = new StringBuffer(absolutePath).append("/").append(start);


        parseHtmlToVoice(transformText, curFolder.toString(), rateMap, style);
        String ssml = xmlToString(curFolder.toString());
        if (StringUtils.isEmpty(ssml)) {
            return handlerRes;
        }
        SpeechSynthesisResult result = synthesizer.SpeakSsmlAsync(ssml).get();
        while (handlerStatus) {
            System.out.println(result.getReason());
            System.out.println(ResultReason.SynthesizingAudioCompleted);
            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                System.out.println("Speech synthesized to speaker for text [" + transformText + "]");
                handlerStatus = Boolean.FALSE;
                synthesizer.close();
                speechConfig.close();

            } else if (result.getReason() == ResultReason.Canceled) {
                SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
                System.out.println("CANCELED: Reason=" + cancellation.getReason());

                if (cancellation.getReason() == CancellationReason.Error) {
                    System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
                    System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                    System.out.println("CANCELED: Did you update the subscription info?");
                }
                handlerStatus = Boolean.FALSE;
                synthesizer.close();
                speechConfig.close();
                result.close();
            }
        }
        return handlerRes;
    }

    /**
     * 解析文本为语音数据
     *
     * @param html  转换文本语言
     * @param style 语言转换类型
     */
    public static void parseHtmlToVoice(String html,
                                        String path,
                                        Map<String, String> rateMap,
                                        Integer style) throws IOException {
        parseHtmlToVoice(html, new File(path + ".xml"), rateMap, style);
    }

    public static void parseHtmlToVoice(String html, String path) throws IOException {
        parseHtmlToVoice(html, new File(path + ".xml"), null, null);
    }

    public static void parseHtmlToVoice(String html, File file) throws IOException {
        parseHtmlToVoice(html, file, null, null);
    }

    public static void parseHtmlToVoice(String html,
                                        File file,
                                        Map<String, String> rateMap,
                                        Integer style) throws IOException {
        JAXBContext jaxbContext = null;
        try {
            VoiceBase voiceXml = null;
            if (null == style || VOICE_NOMAL == style) {
                jaxbContext = JAXBContext.newInstance(VoiceXml.class);
                voiceXml = new VoiceXml();
            } else if (VOICE_NATURE == style) {
                jaxbContext = JAXBContext.newInstance(VoiceNatureXml.class);
                voiceXml = new VoiceNatureXml();
            } else if (VOICE_Rate == style) {
                jaxbContext = JAXBContext.newInstance(VoiceRateXml.class);
                if (null == rateMap) {
                    voiceXml = new VoiceRateXml();
                } else {
                    voiceXml = new VoiceRateXml()
                            .VoiceVolume(rateMap.get("volume"))
                            .VoiceRate(rateMap.get("rate"))
                            .VoicePitch(rateMap.get("pitch"));
                }
            }

            voiceXml.voiceText(html);

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
            //是否省略xml
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            if (!file.exists()) {
                file.createNewFile();
            }
            marshaller.marshal(voiceXml, file);//直接输出到控制台中
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 轉換XML為String類型方法
     *
     * @param filePath
     * @return
     */
    public static String xmlToString(String filePath) {
        File file = new File(filePath + ".xml");
        return xmlToString(file);
    }

    public static String xmlToString(File file) {
        StringBuffer fileContents = new StringBuffer((int) file.length());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + System.lineSeparator());
            }
            return fileContents.toString().trim();
        } catch (FileNotFoundException ex) {
            return "File not found.";
        }
    }

    /**
     * 默認為男性聲音
     *
     * @param speechConfig
     * @param lang
     * @return
     */
    private static SpeechConfig _translang(SpeechConfig speechConfig, String lang) {
        return _translang(speechConfig, lang, MAN);
    }

    private static SpeechConfig _translang(SpeechConfig speechConfig, String lang, String sex) {
        if (null == sex) {
            sex = HUMAN;
        }
        if (HUMAN.equals(sex)) {
            if (EN.equals(lang)) {
                speechConfig.setSpeechSynthesisLanguage("en-US");
                speechConfig.setSpeechSynthesisVoiceName("en-US-ZiraRUS");
            } else if (SC.equals(lang)) {
                speechConfig.setSpeechSynthesisLanguage("zh-CN");
                speechConfig.setSpeechSynthesisVoiceName("zh-CN-HuihuiRUS");
            } else {
                speechConfig.setSpeechSynthesisLanguage("zh-HK");
                speechConfig.setSpeechSynthesisVoiceName("zh-HK-TracyRUS");
            }
        } else {
            if (EN.equals(lang)) {
                speechConfig.setSpeechSynthesisLanguage("en-US");
                speechConfig.setSpeechSynthesisVoiceName("en-US-BenjaminRUS");
            } else if (SC.equals(lang)) {
                speechConfig.setSpeechSynthesisLanguage("zh-CN");
                speechConfig.setSpeechSynthesisVoiceName("zh-CN-Kangkang-Apollo");
            } else {
                speechConfig.setSpeechSynthesisLanguage("zh-HK");
                speechConfig.setSpeechSynthesisVoiceName("zh-HK-Danny-Apollo");
            }
        }
        speechConfig.requestWordLevelTimestamps();
        return speechConfig;
    }
}
