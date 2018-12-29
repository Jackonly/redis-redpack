package com.suzhe.qhb;

public interface LuaScript {

    /**
     * 脚本调用方式
     * Object object = jedisUtils.eval(LuaScript.getHbLua,//lua脚本
     * 4,//参数个数
     *  RedisKeys.getHbPoolKey(orderId),//对应脚本里的KEYS[1]
     *  RedisKeys.getDetailListKey(orderId),//对应脚本里的KEYS[2]
     *  RedisKeys.getHbRdKey(orderId),//对应脚本里的KEYS[3]
     *  String.valueOf(userId));//对应脚本里的KEYS[4]
     *
     *
     */
    public static String getHbLua =
            //查询用户是否已抢过红包，如果用户已抢过红包，则直接返回nil 
            "if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then\n"   +
                    //如果抢过红包 返回“1”
                    "return '1';\n" +
                    "else\n"  +
                    //从红包池取出一个小红包
                    "local hb = redis.call('rpop', KEYS[1]);\n"  +
                    //判断红包池的红包是否为不空
                    "if hb then\n"  +
                    "local x = cjson.decode(hb);\n"  +
                    //将红包信息与用户ID信息绑定，表示该用户已抢到红包 
                    "x['userId'] = KEYS[4];\n"  +
                    "local re = cjson.encode(x);\n"  +
                    //记录用户已抢过 比如 hset hb:rd:{orderId}  {userId}  1
                    "redis.call('hset', KEYS[3], KEYS[4], '1');\n"  +
                    //将抢红包的结果详情存入hb:detailList:{orderId}
                    "redis.call('lpush', KEYS[2], re);\n" +
                    "return re;\n"  +
                    "else\n"  +
                    //如果红包已被抢完 返回“0”
                    "return '0';" +
                    "end\n"  +
                    "end\n"  +
                    "return nil";

}
