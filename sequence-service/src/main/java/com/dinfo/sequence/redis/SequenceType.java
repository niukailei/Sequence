package com.dinfo.sequence.redis;

import com.dinfo.sequence.redis.lua.LuaDeal;
import com.dinfo.sequence.redis.lua.impl.DateStrTypeLuaDeal;
import com.dinfo.sequence.redis.lua.impl.LongTypeLuaDeal;

/**
 * Created by winston on 15/11/3.
 * @author winston
 */
public enum SequenceType {

    /**
     *根据snowflake原理生成（日期Long（41）＋shareId（6）＋squenceId（16）
     *
     */
    LongType(1,"LongTypeRedis", "/lua/LongTypeRedis.lua",1000,new LongTypeLuaDeal(),"长整型序列号生成"),

    /**
     *当前日期＋序列号,格式:YYYYMMDD＋shareId（6）＋
     */
    DateStrType(2,"DateTypeRedis", "/lua/DateTypeRedis.lua",500,new DateStrTypeLuaDeal(),"日期字符序列号生成");

    /**
     * 类型
     */
    private int type;

    /**
     * redis属性配置的key
     */
    private String redisProKey;

    /**
     * lua执行路径及文件名
     */
    private String luaPath;

    /**
     * 每次最大的请求数
     */
    private int maxBatchSize;

    /**
     * lua处理接口
     */
    private LuaDeal luaDeal;

    /**
     * 描述
     */
    private String desc;


    SequenceType(int type, String redisProKey, String luaPath, int maxBatchSize, LuaDeal luaDeal, String desc) {
        this.type = type;
        this.redisProKey = redisProKey;
        this.luaPath = luaPath;
        this.maxBatchSize = maxBatchSize;
        this.luaDeal = luaDeal;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getRedisProKey() {
        return redisProKey;
    }

    public String getDesc() {
        return desc;
    }

    public String getLuaPath() {
        return luaPath;
    }

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public LuaDeal getLuaDeal() {
        return luaDeal;
    }
}
