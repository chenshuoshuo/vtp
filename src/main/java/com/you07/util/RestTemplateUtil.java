package com.you07.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.you07.config.RestTemplateInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Collections;

/**
 * @author wells
 * @ClassNmae RestTemplateUtil
 * @Description TODO
 * @Date 2019/6/3 11:13
 * @Version 1.0
 **/
@Component
public class RestTemplateUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static String username;
    private static String password;

    @Value("${oauth.username}")
    public void setUsername(String username) {
        RestTemplateUtil.username = username;
    }

    @Value("${oauth.password}")
    public void setPassword(String password) {
        RestTemplateUtil.password = password;
    }

    private static RestTemplateInterceptor getAccessToken() throws IOException {
        //不需要通过tokenURL获取accessToken

        //通过配置文件加密
        return new RestTemplateInterceptor(
                Base64.getEncoder().encodeToString((username + ":" + password).getBytes("utf-8")));


    }

    public static JSONObject getJSONObjectForCmGis(String url) {
        RestTemplateInterceptor interceptor = null;
        try {
            interceptor = getAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
//        String requestUrl = url + requestMap + "?" + requestParam;
        if (interceptor != null) {
            restTemplate.setInterceptors(Collections.singletonList(interceptor));
        }
        JSONObject responseJson = restTemplate.getForEntity(url, JSONObject.class).getBody();
        return responseJson;
    }
}
