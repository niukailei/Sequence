package com.dinfo.sequence.redis.redispro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by winston.wang on 2017/1/3.
 */
@Service(value = "LongTypeRedis")
@ConfigurationProperties(prefix = "redis")
public class LongTypeRedisPro implements RedisPro{

    private String longTypeCon;

    private String password;
    
    
    @Override
    public String getConStr() {
        return getLongTypeCon();
    }

    @Override
    public String getPassword() {
        return password;
    }
    

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLongTypeCon() {
        return longTypeCon;
    }

    public void setLongTypeCon(String longTtypeCon) {
        this.longTypeCon = longTtypeCon;
    }


}
