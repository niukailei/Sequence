package com.dinfo.sequence.redis.redispro;

/**
 * redis属性接口
 * Created by winston.wang on 2017/1/3.
 */
public interface RedisPro {
    /**
     * 获取redis连接属性
     * @return 联系信息
     */
    String getConStr();

    String getPassword();
}
