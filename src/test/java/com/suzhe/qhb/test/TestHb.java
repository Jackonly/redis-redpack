package com.suzhe.qhb.test;


import com.alibaba.fastjson.JSONObject;
import com.suzhe.qhb.JedisUtils;
import com.suzhe.qhb.LuaScript;
import org.junit.Test;

public class TestHb {

    @Test
    public void test1(){
        JedisUtils jedisUtils = new JedisUtils("118.89.196.99", 6379, "123456");

        for (int i = 1; i<=6; i++){
            JSONObject object = new JSONObject();
            object.put("id", i); //红包ID
            object.put("money", i);   //红包金额
            jedisUtils.lpush("hb:pool:123456",object.toJSONString());
        }
    }

    @Test
    public void test2(){
        JedisUtils jedisUtils = new JedisUtils("118.89.196.99", 6379, "123456");
        Object object = jedisUtils.eval(LuaScript.getHbLua,4,"hb:pool:123456","hb:detailList:123456","hb:rd:123456","11111");
        if ("0".equals(object)){
            System.out.println("hb result:" + object + "已领完");
        }
        if ("1".equals(object)){
            System.out.println("hb result:" + object + "已领取");
        }
    }



}
