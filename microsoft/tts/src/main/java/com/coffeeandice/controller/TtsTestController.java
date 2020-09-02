package com.coffeeandice.controller;

import com.coffeeandice.util.SpeechSynthesizerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Classname TtsTestController
 * @Description TODO
 * @Date 2020/9/2 16:51
 * @Created by CoffeeAndIce
 */
@Controller
public class TtsTestController {
    @RequestMapping(value = "/voice/test")
    public String dsvds(HttpServletRequest request) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        String text = request.getParameter("text");
        String path = request.getParameter("path");
        String rate = request.getParameter("rate");
        String volume = request.getParameter("volume");
        String pitch = request.getParameter("pitch");
        Map rateMap = null;
        System.out.println(text + "-- " + path);
        if (StringUtils.isNotEmpty(rate) || StringUtils.isNotEmpty(volume) || StringUtils.isNotEmpty(pitch)) {
            rateMap = new HashMap<>();
            rateMap.put("rate", rate);
            rateMap.put("volume", volume);
            rateMap.put("pitch", pitch);
        }
        StringBuffer transform = null;

        if (null == rateMap) {
            transform = new SpeechSynthesizerUtil().transform(text, path, "hk",
                    "man");
        } else {
            transform = new SpeechSynthesizerUtil().transform(text, path, "hk",
                    "man", 3, rateMap);
        }
        return path;
    }
}
