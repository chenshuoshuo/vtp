package com.you07.config;

import com.you07.util.CutHtml;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MyWebFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println(httpServletRequest.getRequestURL());
        //Xss/SQL注入过滤
        Map<String, String[]> paramMap = httpServletRequest.getParameterMap();
        Set<String> keySet = paramMap.keySet();
        for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Object[] str = paramMap.get(key);
            if(str != null){
                for(int i = 0; i < str.length; i++){
                    if(StringUtils.isNotBlank(str[i].toString())) {
                        str[i] = CutHtml.getDefaultInstance().cleanXSSAndSQL(str[i].toString());
                    }
                }
            }
        }

        System.out.println("url:"+httpServletRequest.getRequestURL());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
