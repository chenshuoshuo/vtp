package com.you07.config;

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

    public RestTemplateInterceptor(){}

    public RestTemplateInterceptor(String accessToken) {
        this.accessToken = accessToken;
        System.out.println(accessToken);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();

        // 加入自定义字段
        headers.add("authorization", "Basic " + accessToken);

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
