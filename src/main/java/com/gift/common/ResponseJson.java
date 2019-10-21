package com.gift.common;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import lombok.Data;

@Data
@Component
public class ResponseJson {
    private int code;
    private HashMap<?, ?> data;
    private String msg;
    
    /**
     * 返回错误信息
     * 
     * @return
     */
    public String responseError(String msg) {
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("code", "1");
        res.put("data", new HashMap<String, Object>());
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }
    
    /**
     * 返回成功信息
     * 
     * @return
     */
    public String responseSuccess(String msg, Object hashMap) {
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("code", "0");
        res.put("data", hashMap);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }
    
    /**
     * 返回未登录信息
     * 
     * @return
     */
    public String responseNoLogin() {
        HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("code", "401");
        res.put("data", new HashMap<String, Object>());
        res.put("msg", "请先登陆");
        return JSON.toJSONString(res);
    }
}
