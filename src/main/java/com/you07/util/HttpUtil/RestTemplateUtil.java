package com.you07.util.HttpUtil;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.you07.config.datasource.RestTemplateInterceptor;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

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
        // 获取默认的请求客户端
        CloseableHttpClient client = HttpClients.createDefault();
//         通过HttpPost来发送post请求
        HttpPost httpPostMethod = new HttpPost("https://testgis.you07.com/cmccr-server/oauth/token?grant_type=password&username=free&password=123456");

        httpPostMethod.setHeader("authorization", "Basic Y21jY3ItaDU6Y21jY3ItaDU=");
        httpPostMethod.setHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = null;
        response = client.execute(httpPostMethod);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity, "UTF-8");
        response.close();
        AccessTokenResponse tokenResponse = objectMapper.readValue(str, AccessTokenResponse.class);

        if (!StringUtils.isNotBlank(tokenResponse.getAccess_token()))
            throw new IOException("获取token失败");

        System.out.println(username + ":" + password);
        //通过配置文件加密
        /*return new RestTemplateInterceptor(
                Base64.getEncoder()
                        .encodeToString((username + ":" + password).getBytes("utf-8")));*/

        return new RestTemplateInterceptor(tokenResponse.getAccess_token());

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
            restTemplate.setInterceptors(Collections.singletonList((ClientHttpRequestInterceptor)interceptor));
        }
        JSONObject responseJson = restTemplate.getForEntity(url, JSONObject.class).getBody();
        return responseJson;
    }
}