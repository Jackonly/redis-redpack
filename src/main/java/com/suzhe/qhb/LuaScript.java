package com.suzhe.qhb;

public interface LuaScript {

    /**
     *
     *
     *
     *
     */
    public static String getHbLua =
            //查询用户是否已抢过红包，如果用户已抢过红包，则直接返回nil 
            "if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then\n"   +
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
                    //记录用户已抢过userIdRecordKey  hset userIdRecordKey  userid 1
                    "redis.call('hset', KEYS[3], KEYS[4], '1');\n"  +
                    //将抢红包的结果详情存入hongBaoDetailListKey
                    "redis.call('lpush', KEYS[2], re);\n" +
                    "return re;\n"  +
                    "else\n"  +
                    "return '0';" +
                    "end\n"  +
                    "end\n"  +
                    "return nil";

}
