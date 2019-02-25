package com.you07.config;

import com.you07.util.CutHtml;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@WebFilter(filterName = "cleanXSSAndSQL", urlPatterns = "/**")
public class MyWebFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        System.out.println(req.getRequestURL());
        //Xss/SQL注入过滤
        Map<String, String[]> paramMap = req.getParameterMap();
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

        System.out.println("url:"+req.getRequestURL());
    }

    @Override
    public void destroy() {

    }
}
