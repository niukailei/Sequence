package com.dinfo.sequence.redis.redispro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by winston.wang on 2017/1/3.
 */
@Service(value = "DateTypeRedis")
@ConfigurationProperties(prefix = "redis")
public class DateTypeRedisPro implements RedisPro {

    private String dateTtypeCon;
    @Override
    public String getConStr() {
        return getDateTtypeCon();
    }

    public String getDateTtypeCon() {
        return dateTtypeCon;
    }

    public void setDateTtypeCon(String dateTtypeCon) {
        this.dateTtypeCon = dateTtypeCon;
    }
}
