package com.dinfo.sequence.redis.lua.impl;

import com.dinfo.sequence.dto.SequenceId;
import com.dinfo.sequence.redis.Constant;
import com.dinfo.sequence.redis.RedisDto;
import com.dinfo.sequence.redis.lua.LuaDeal;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * Created by winston on 15/11/3.
 * @author winston
 */
public class LongTypeLuaDeal implements LuaDeal {
    @Override
    public List<String> buildLuaInputParam(final long batchSize, final String keyName) {
        return Arrays.asList(String.valueOf(LongTypeConstant.MAX_SEQUENCE),
                String.valueOf(LongTypeConstant.MIN_LOGICAL_SHARD_ID),
                String.valueOf(LongTypeConstant.MAX_LOGICAL_SHARD_ID),
                String.valueOf(batchSize),keyName);
    }

    @Override
    public List<SequenceId> buildIds(final RedisDto redisDto, final Integer keySize) throws Exception{

        long timestamp = (redisDto.getTimeSeconds() * Constant.ONE_MILLI_IN_MICRO_SECS) +
                (redisDto.getTimeMicroseconds() / Constant.ONE_MILLI_IN_MICRO_SECS);
        long logicalShardId = redisDto.getLogicalShardId();
        validateLogicalShardId(logicalShardId);
        validateKeySize(keySize);
        List<SequenceId> sequenceIds = Lists.newArrayList();
        for (long i = redisDto.getStartSequence(); i <= redisDto.getEndSequence(); i ++) {
            long id = ((timestamp - LongTypeConstant.CUSTOM_EPOCH) << LongTypeConstant.TIMESTAMP_SHIFT) | (logicalShardId << LongTypeConstant.LOGICAL_SHARD_ID_SHIFT)|i;
            sequenceIds.add(new SequenceId(id, timestamp));
        }
        return sequenceIds;
    }
    /**
     * 验证logicalShardId是否合法
     * @param logicalShardId The logical shard ID as retrieved from Redis.
     */
    private void validateLogicalShardId(final long logicalShardId) throws Exception{
        if (logicalShardId < LongTypeConstant.MIN_LOGICAL_SHARD_ID || logicalShardId > LongTypeConstant.MAX_LOGICAL_SHARD_ID) {
            throw new Exception(
                    "The logical shard ID set in Redis is less than " + String.valueOf(LongTypeConstant.MIN_LOGICAL_SHARD_ID)
                            + " or is greater than the supported maximum of "
                            + String.valueOf(LongTypeConstant.MAX_LOGICAL_SHARD_ID));
        }
    }
    /**
     * 验证logicalShardId是否合法
     * @param keySize
     */
    private void validateKeySize(final Integer keySize) throws Exception{
        if(keySize!=-1){
            throw new Exception("keySize unsupport:"+keySize);
        }
    }
}
