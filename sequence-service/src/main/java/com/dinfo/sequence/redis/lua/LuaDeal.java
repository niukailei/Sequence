package com.dinfo.sequence.redis.lua;

import com.dinfo.sequence.dto.SequenceId;
import com.dinfo.sequence.redis.RedisDto;

import java.util.List;

/**
 * Created by winston on 15/11/3.
 * @author winston
 */
public interface LuaDeal {

    /**
     * 创建lua输入参数
     * @param batchSize 请求序列号大小
     * @return lua参数list
     */
    List<String> buildLuaInputParam(final long batchSize,final String keyName);


    /**
     * 生成id
     * @param redisDto redis执行结果
     * @return id列表
     * @throws Exception
     */
    List<SequenceId>  buildIds(final RedisDto redisDto, final Integer keySize) throws Exception;
}
