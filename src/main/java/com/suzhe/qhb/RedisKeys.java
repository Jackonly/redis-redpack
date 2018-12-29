package com.suzhe.qhb;

public class RedisKeys {

    /**
     * 获取红包池子的key
     * @param orderId
     * @return
     */
    public String getHbPoolKey(String orderId){
        return "hb:pool:" + orderId;
    }

    /**
     * 获取红包领取用户记录key
     * @param orderId
     * @return
     */
    public String getHbRdKey(String orderId){
        return "hb:rd:" + orderId;
    }

    /**
     * 红包与用户信息列表key
     * @param orderId
     * @return
     */
    public String getDetailListKey(String orderId){
        return "hb:detailList:" + orderId;
    }

}
