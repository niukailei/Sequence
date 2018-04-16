package com.dinfo.sequence.redis.redispro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by winston.wang on 2017/1/3.
 */
@Service(value = "DateTypeRedis")
@ConfigurationProperties(prefix = "redis")
public class DateTypeRedisPro implements RedisPro {

    private String dateTypeCon;

    private String password;
    
    
    @Override
    public String getConStr() {
        return getDateTypeCon();
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getDateTypeCon() {
        return dateTypeCon;
    }

    public void setDateTypeCon(String dateTtypeCon) {
        this.dateTypeCon = dateTtypeCon;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
