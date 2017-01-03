package com.dinfo.sequence.redis.redispro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by winston.wang on 2017/1/3.
 */
@Service(value = "LongTypeRedis")
@ConfigurationProperties(prefix = "redis")
public class LongTypeRedisPro implements RedisPro{

    private String longTtypeCon;
    @Override
    public String getConStr() {
        return getLongTtypeCon();
    }

    public String getLongTtypeCon() {
        return longTtypeCon;
    }

    public void setLongTtypeCon(String longTtypeCon) {
        this.longTtypeCon = longTtypeCon;
    }
}
