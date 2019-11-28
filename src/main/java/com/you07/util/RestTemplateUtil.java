package com.you07.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;

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
    private static AccessTokenResponse tokenResponse = new AccessTokenResponse();

    @Autowired
    public RestTemplateUtil(LocationSystemConfigDao systemConfigDao) {
        RestTemplateUtil.systemConfigDao = systemConfigDao;
    }

    /**
     * egan
     * 向cmips发送请求
     *
     * @param uri 相对路径
     * @date 2019/9/9 10:38
     **/
    public static JSONObject getJSONObjectForCmIps(String uri) {
        // 获取默认的请求客户端
        //判断token是否过期
        if (tokenResponse.getAccess_token() == null || (System.currentTimeMillis() - tokenResponse.getTokenTime() >= (Long.valueOf(tokenResponse.getExpires_in())*1000)))
        {
            //拿token
            try {
                CloseableHttpClient client = HttpClients.createDefault();
//         通过HttpPost来发送post请求
                HttpPost httpPostMethod = new HttpPost(tokenUrl + "?grant_type=" + grantType + "&client_id=" + clientId + "&client_secret=" + clientSecret);
                CloseableHttpResponse response = client.execute(httpPostMethod);
                String str = EntityUtils.toString(response.getEntity(), "UTF-8");
                response.close();

                tokenResponse = objectMapper.readValue(str, AccessTokenResponse.class);
                //记录获得Token的时间
                tokenResponse.setTokenTime(System.currentTimeMillis());

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        LocationSystemConfig systemConfig = systemConfigDao.loadDefault();
        RestTemplateInterceptor interceptor = new RestTemplateInterceptor(tokenResponse.getAccess_token(), "Bearer");
        return sendRequest(systemConfig.getIpsApi() + uri, interceptor);
    }

    /**
     * egan
     * 向cmgis发送请求
     *
     * @param uri 相对路径
     * @date 2019/9/9 10:38
     **/
    public static JSONObject getJSONObjectForCmGis(String uri) {
        LocationSystemConfig systemConfig = systemConfigDao.loadDefault();
        RestTemplateInterceptor interceptor = new RestTemplateInterceptor(systemConfig.getGisMapToken(), "Basic");
        return sendRequest(systemConfig.getLqMapGisUrl() + uri, interceptor);
    }

    public static JSONObject postJSONObjectFormCmGis(String uri, Object postData) {
        LocationSystemConfig systemConfig = systemConfigDao.loadDefault();
        RestTemplateInterceptor interceptor = new RestTemplateInterceptor(systemConfig.getGisMapToken(), "Basic");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(interceptor));
        String url = systemConfig.getLqMapGisUrl() + uri;
        System.out.println(url + "\n" + JSONObject.toJSONString(postData));
        JSONObject jsonObject =  restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
        return jsonObject;
    }

    private static JSONObject sendRequest(String url, RestTemplateInterceptor interceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        if (interceptor != null) {
            restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(interceptor));
        }
        System.out.println(url);
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
