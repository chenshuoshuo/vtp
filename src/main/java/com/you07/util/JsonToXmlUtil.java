package com.you07.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName JsonToXmlUtil
 * @Description TODO
 * @Author Administrator
 * @Date 2019/11/29 9:24
 * @Version 1.0
 **/
public class JsonToXmlUtil {
    public static String jsonToXmlUtil(String jsonString) {
        JSONObject jsonObj = null;
        jsonObj = JSON.parseObject(jsonString);
        String xmlResult = null;
        // 创建dom对象
        Document document = DocumentHelper.createDocument();
        // 设置编码格式
        document.setXMLEncoding("utf-8");
        // 添加父元素
        Element addElement = document.addElement("Message");
        // 添加子元素
        Element thisElement = addElement.addElement("Public");
        // 得到json数据中key的集合：[BBB, AAA, TxnSeq, TxnBatchNo, CardNo]
        Set<String> keySet = jsonObj.keySet();
        Map<Object, Object> map = new HashMap<Object, Object>(keySet.size());
        for (String key : keySet) {
            map.put(key, jsonObj.get(key));
            // 将map转为JSON格式
            // {"AAA":[{"hello":"nihao","hey":"hai","world":[{"c":"cat","d":"dog","e":"elepahant"}]}]}
            JSONObject j = (JSONObject) JSON.toJSON(map);
            Element childrenElement = thisElement.addElement(key);
            // 判断此时key的value是否是json数组
            Object json = JSON.toJSON(j.get(key));
            if (json instanceof JSONArray) {
                // 处理json数组
                jsonArrayToXml((JSONArray) json, childrenElement);
                // 判断此时key的value是JSONObject：{"hello":"nihao","hey":"hai","world"："oo"}
            } else if (json instanceof JSONObject) {
                JSONObject object = (JSONObject) JSON.toJSON(j.get(key));
                for (String key2 : object.keySet()) {
                    Element childrenElement2 = childrenElement.addElement(key2);
                    // 再次判断
                    if (JSON.toJSON(j.get(key2)) instanceof JSONArray) {
                        jsonArrayToXml((JSONArray) JSON.toJSON(j.get(key2)), childrenElement2);
                    } else {
                        childrenElement2.addAttribute("value", object.getString(key2));
                    }
                }
            } else {
                // 不是json数组则为key添加值
                childrenElement.addAttribute("value", map.get(key).toString());
            }
        }
        xmlResult = document.asXML();
        return xmlResult;
    }

    /**
     * 处理json数组
     * @param jsonArray 传入的json数组
     * @param childrenElement  上层的dom元素
     */
    public static void jsonArrayToXml(JSONArray jsonArray, Element childrenElement) {
        // 遍历json数组：[{"hello":"nihao","hey":"hai","world":[{"c":"cat","d":"dog","e":"elepahant"}]}]
        for (int i = 0; i < jsonArray.size(); i++) {
            // 以此例子为例：第一次遍历获取jsonArray.get(i)
            // {"hello":"nihao","hey":"hai","world":[{"c":"cat","d":"dog","e":"elepahant"}]}
            // 第二次遍历：{"c":"cat","d":"dog","e":"elepahant"}
            // 将或得到的Object类型的字符串转化为json格式
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            // 继续遍历
            for (String key : jsonObject.keySet()) {
                Element addElement = childrenElement.addElement(key);
                Object arry = JSON.toJSON(jsonObject.get(key));
                if (arry instanceof JSONArray) {
                    // 递归
                    jsonArrayToXml((JSONArray) arry, addElement);
                } else {
                    addElement.addAttribute("value", jsonObject.get(key).toString());
                }
            }
        }
    }
}
