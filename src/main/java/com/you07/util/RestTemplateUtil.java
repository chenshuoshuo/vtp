package com.you07.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.you07.config.RestTemplateInterceptor;
import com.you07.vtp.dao.LocationSystemConfigDao;
import com.you07.vtp.model.LocationSystemConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
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

    private static LocationSystemConfigDao systemConfigDao;

    private static String tokenUrl;
    private static String clientId;
    private static String clientSecret;
    private static String grantType;

    @Autowired
    public RestTemplateUtil(LocationSystemConfigDao systemConfigDao) {
        RestTemplateUtil.systemConfigDao = systemConfigDao;
    }

    /**
     * egan
     * 向cmips发送请求
     * @date 2019/9/9 10:38
     * @param uri 相对路径
     **/
    public static JSONObject getJSONObjectForCmIps(String uri) {
        // 获取默认的请求客户端
        AccessTokenResponse tokenResponse;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
//         通过HttpPost来发送post请求
            HttpPost httpPostMethod = new HttpPost(tokenUrl + "?grant_type=" + grantType + "&client_id=" + clientId + "&client_secret=" + clientSecret);
            CloseableHttpResponse response = client.execute(httpPostMethod);
            String str = EntityUtils.toString(response.getEntity(), "UTF-8");
            response.close();
            tokenResponse = objectMapper.readValue(str, AccessTokenResponse.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        LocationSystemConfig systemConfig = systemConfigDao.loadDefault();
        RestTemplateInterceptor interceptor = new RestTemplateInterceptor(tokenResponse.getAccess_token(), "Bearer");
        return sendRequest(systemConfig.getIpsApi() + uri, interceptor);
    }

    /**
     * egan
     * 向cmgis发送请求
     * @date 2019/9/9 10:38
     * @param uri 相对路径
     **/
    public static JSONObject getJSONObjectForCmGis(String uri) {
        LocationSystemConfig systemConfig = systemConfigDao.loadDefault();
        RestTemplateInterceptor interceptor = new RestTemplateInterceptor(systemConfig.getGisMapToken(), "Basic");
        return sendRequest(systemConfig.getLqMapGisUrl() + uri, interceptor);
    }

    private static JSONObject sendRequest(String url, RestTemplateInterceptor interceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        if (interceptor != null) {
            restTemplate.setInterceptors(Collections.singletonList(interceptor));
        }
        JSONObject responseJson = restTemplate.getForEntity(url, JSONObject.class).getBody();
        return responseJson;
    }

    @Value("${oauth.token}")
    public void setTokenUrl(String tokenUrl) {
        RestTemplateUtil.tokenUrl = tokenUrl;
    }

    @Value("${oauth.client-id}")
    public void setClientId(String clientId) {
        RestTemplateUtil.clientId = clientId;
    }

    @Value("${oauth.client-secret}")
    public void setClientSecret(String clientSecret) {
        RestTemplateUtil.clientSecret = clientSecret;
    }

    @Value("${oauth.grant-type}")
    public void setGrantType(String grantType) {
        RestTemplateUtil.grantType = grantType;
    }
}
