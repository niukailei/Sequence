package com.dinfo.sequence.redis;

import java.util.concurrent.TimeUnit;

/**
 * Created by winston on 15/11/3.
 * @author winston
 */
public class Constant {

    /**
     * 如果生成不成功，默认最大的重试次数
     */
    public static final int DEFAULT_MAX_ATTEMPTS = 5;

    /**
     * 时间转换
     */
    public static final long ONE_MILLI_IN_MICRO_SECS = TimeUnit.MICROSECONDS.convert(1, TimeUnit.MILLISECONDS);


}
