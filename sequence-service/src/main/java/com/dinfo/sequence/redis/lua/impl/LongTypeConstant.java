package com.dinfo.sequence.redis.lua.impl;

/**
 * 根据snowflake原理生成
 * Created by winston on 15/11/2.
 * @author winston
 */
public class LongTypeConstant {

    /**
     * 定制的毫秒数
     */
    public static final long CUSTOM_EPOCH = 1401277473L;

    /**
     * shard id 占用的位数
     */
    public static final int LOGICAL_SHARD_ID_BITS = 6;

    /**
     * 自动序列号占用的位数
     */
    public static final int SEQUENCE_BITS = 16;

    /**
     * 时间占用的移位数
     */
    public static final int TIMESTAMP_SHIFT = SEQUENCE_BITS + LOGICAL_SHARD_ID_BITS;

    /**
     * share id 移位数
     */
    public static final int LOGICAL_SHARD_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 最大的序列号
     */
    public static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    /**
     * 最大share id
     */
    public static final long MAX_LOGICAL_SHARD_ID = ~(-1L << LOGICAL_SHARD_ID_BITS);
    /**
     * 最小share id
     */
    public static final long MIN_LOGICAL_SHARD_ID = 0L;

}
