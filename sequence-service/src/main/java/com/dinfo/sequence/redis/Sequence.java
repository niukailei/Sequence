package com.dinfo.sequence.redis;


/**
 * 序列号生成信息类
 * Created by winston on 15/11/3.
 */
public class Sequence {

    /**
     * lua脚本
     */
    private  String luaScript;

    /**
     * lua脚本的sha1
     */
    private  String luaScriptSha;

    /**
     * redis链接信息
     */
    private String redisConnectStr;

    /**
     * redisClient
     */
    private RoundRobinRedis roundRobinRedis;

    /**
     * lua处理接口
     */
    private SequenceType sequenceType;

    public Sequence(String luaScript, String luaScriptSha,String redisConnectStr,
                    RoundRobinRedis roundRobinRedis, SequenceType sequenceType) {
        this.luaScript = luaScript;
        this.luaScriptSha = luaScriptSha;
        this.redisConnectStr = redisConnectStr;
        this.roundRobinRedis = roundRobinRedis;
        this.sequenceType = sequenceType;
    }

    public String getLuaScript() {
        return luaScript;
    }

    public String getLuaScriptSha() {
        return luaScriptSha;
    }

    public String getRedisConnectStr() {
        return redisConnectStr;
    }

    public RoundRobinRedis getRoundRobinRedis() {
        return roundRobinRedis;
    }

    public SequenceType getSequenceType() {
        return sequenceType;
    }
}
