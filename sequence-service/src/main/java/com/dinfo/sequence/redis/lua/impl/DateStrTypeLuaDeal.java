package com.dinfo.sequence.redis.lua.impl;

import com.dinfo.common.date.Dates;
import com.dinfo.sequence.dto.SequenceId;
import com.dinfo.sequence.redis.lua.LuaDeal;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.dinfo.sequence.redis.Constant;
import com.dinfo.sequence.redis.RedisDto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by winston on 15/11/3.
 * @author winston
 */
public class DateStrTypeLuaDeal implements LuaDeal {

    @Override
    public List<String> buildLuaInputParam(final long batchSize,final String keyName) {
        String dataStr= Dates.format(new Date(),"yyyyMMdd");
        return Arrays.asList(String.valueOf(DateStrTypeConstant.MAX_SEQUENCE),
                String.valueOf(DateStrTypeConstant.MIN_LOGICAL_SHARD_ID),
                String.valueOf(DateStrTypeConstant.MAX_LOGICAL_SHARD_ID),
                String.valueOf(batchSize),dataStr+keyName);
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
            String currentDate=Dates.format(new Date(timestamp),"yyyyMMdd");
            String typeFormat=Joiner.on("").join("%0",keySize,"d");
            long id = new Long(Joiner.on("").join(currentDate,redisDto.getLogicalShardId(),String.format(typeFormat,i)));
            sequenceIds.add(new SequenceId(id, timestamp));
        }
        return sequenceIds;
    }
    /**
     * 验证logicalShardId是否合法
     * @param logicalShardId The logical shard ID as retrieved from Redis.
     */
    private void validateLogicalShardId(final long logicalShardId) throws Exception{
        if (logicalShardId < DateStrTypeConstant.MIN_LOGICAL_SHARD_ID || logicalShardId > DateStrTypeConstant.MAX_LOGICAL_SHARD_ID) {
            throw new Exception(
                    "The logical shard ID set in Redis is less than " + String.valueOf(DateStrTypeConstant.MIN_LOGICAL_SHARD_ID)
                            + " or is greater than the supported maximum of "
                            + String.valueOf(DateStrTypeConstant.MAX_LOGICAL_SHARD_ID));
        }
    }
    /**
     * 验证logicalShardId是否合法
     * @param keySize
     */
    private void validateKeySize(final Integer keySize) throws Exception{
        if(keySize<=0){
            throw new Exception("keySize unsupport:"+keySize);
        }
    }
}
