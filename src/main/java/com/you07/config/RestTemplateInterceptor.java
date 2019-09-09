package com.you07.config;

import com.you07.util.AccessTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author wells
 * @ClassNmae RestTemplateInterceptor
 * @Description TODO
 * @Date 2019/6/3 11:15
 * @Version 1.0
 **/
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private String accessToken;

    private String type;

    public RestTemplateInterceptor() {
    }

    public RestTemplateInterceptor(String accessToken, String type) {
        this.accessToken = accessToken;
        this.type = type;
        System.out.println(accessToken);
    }

    public RestTemplateInterceptor(AccessTokenResponse tokenResponse) {
        //第一个字母大写
        accessToken = new String(tokenResponse.getToken_type().substring(0, 1)).toUpperCase() + tokenResponse.getToken_type().substring(1) + " " + tokenResponse.getAccess_token();
        System.out.println(accessToken);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();

        // 加入自定义字段
//        headers.add("authorization", "Basic " + accessToken);
        headers.add("authorization", type + " " + accessToken);

        // 保证请求继续被执行
        return execution.execute(request, body);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
