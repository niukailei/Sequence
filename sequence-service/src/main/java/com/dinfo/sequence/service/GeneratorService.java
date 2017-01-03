package com.dinfo.sequence.service;


import com.dinfo.common.model.Response;
import com.dinfo.log.Loggers;
import com.dinfo.sequence.dto.SequenceId;
import com.dinfo.sequence.redis.lua.LuaDeal;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.dinfo.sequence.redis.Redis;
import com.dinfo.sequence.redis.Sequence;
import com.dinfo.sequence.redis.SequenceManager;
import com.dinfo.sequence.redis.Constant;
import com.dinfo.sequence.redis.SequenceType;
import com.dinfo.sequence.redis.RedisDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * 序列号生成接口实现类
 */
@Service
public class GeneratorService {

  private static final Logger logger = Loggers.get("sequence");

  /**
   * 序列号管理
   */
  @Autowired
  private  SequenceManager sequenceManager;

  /**
   * 失败重试次数
   */
  @Value("${maximumAttempts}")
  private final int maximumAttempts=Constant.DEFAULT_MAX_ATTEMPTS;


  /**
   * 生成一个id,失败重试
   * @return An optional ID
   */
  public Response<SequenceId> generateId() {

    Response<List<SequenceId>> result = generateIdBatch(1);

    if (result.isSuccess()) {
      return Response.ok(result.getData().get(0));
    }
    return Response.notOk(result.getErr());
  }

  /**
   *  尽最大可能生成一批id
   */
    public Response<List<SequenceId>> generateIdBatch(final long batchSize) {
      return generateIdBatch(SequenceType.LongType,"LongType",-1,batchSize);
    }

    public Response<SequenceId> generateDateStrId(final String keyName, final Integer keySize) {
        Response<List<SequenceId>> result = generateDateStrIdBatch(keyName,keySize,1);

        if (result.isSuccess()) {
            return Response.ok(result.getData().get(0));
        }
        return Response.notOk(result.getErr());
    }

    public Response<List<SequenceId>> generateDateStrIdBatch(final String keyName, final Integer keySize, long batchSize) {
        return generateIdBatch(SequenceType.DateStrType,keyName,keySize,batchSize);
    }

    /**
     * 生成优惠劵信息
     * @param sequenceType 序列类型
     * @param keyName redis key
     * @param batchSize 生成数量
     * @param keySize 生成序列号长度,－1表示不设置
     * @return list id
     */
    public Response<List<SequenceId>> generateIdBatch(final SequenceType sequenceType, final String keyName, final Integer keySize, final long batchSize) {

        try{
            //验证请求参数是否合法
            if (batchSize <= 0 || batchSize > sequenceType.getMaxBatchSize()) {
                return Response.notOk("The batch size is less than 1  or is greater than the supported maximum of "
                        +sequenceType.getMaxBatchSize());
            }

            for (int retries = 0; retries < maximumAttempts; retries++) {
                try {
                    Response<Sequence> sequenceResp=sequenceManager.getSequence(sequenceType);
                    if(!sequenceResp.isSuccess()){
                        return Response.notOk(sequenceResp.getErr());
                    }
                    Sequence sequence=sequenceResp.getData();
                    Response<List<SequenceId>> result = generateIds(sequence,keyName,keySize,batchSize);
                    if (result.isSuccess()) {
                        return result;
                    }

                    Thread.sleep(retries * retries);
                } catch (Throwable  e) {
                    logger.warn("Failed to generate ID. Underlying exception was: {}", Throwables.getStackTraceAsString(e));
                }
            }

            String errorMessage=String.format("No ID generated. ID generation failed after %s retries.", maximumAttempts);
            logger.error(errorMessage);
            return Response.notOk(errorMessage);

        }catch (Throwable e){
            logger.error("failed to generateIdBatch, cause: {}",Throwables.getStackTraceAsString(e));
            return Response.notOk("failed to generateIdBatch error");
        }
    }

    /**
     * 生成Sequence
     * @param sequence 序列号生成信息
     * @param batchSize The number IDs to return.
     */
    private Response<List<SequenceId>> generateIds(final Sequence sequence, final String keyName, final Integer keySize, final long batchSize) {

        Response<RedisDto> response;
        try{
            Redis redis=sequence.getRoundRobinRedis().getNextRedis();
            List<String>  luaInputParam= sequence.getSequenceType().getLuaDeal().buildLuaInputParam(batchSize,keyName);
            if(redis.isSupportLoadLuaScript()){
                response=executeOrLoadLuaScript(redis,sequence.getLuaScript(),sequence.getLuaScriptSha(),luaInputParam);
            }else{
                response=executeScript(redis,sequence.getLuaScript(),luaInputParam);
            }
            if (!response.isSuccess()) {
                return Response.notOk(response.getErr());
            }
            RedisDto redisDto = response.getData();
            LuaDeal luaDeal=sequence.getSequenceType().getLuaDeal();
            List<SequenceId> sequenceIdList =luaDeal.buildIds(redisDto,keySize);
            Preconditions.checkArgument(sequenceIdList.size()==batchSize);
            return Response.ok(sequenceIdList);
        }catch (Throwable e){
            logger.error("failed to generateIds, cause: {}",Throwables.getStackTraceAsString(e));
            return Response.notOk("failed to generateIds error");
        }
    }

    /**
     * redis执行或加载lua脚本
     * @return reidsDto
     */
    private Response<RedisDto> executeOrLoadLuaScript(Redis redis,String luaScript,String luaScriptSha,final List<String> luaInputParam) {

        Response<RedisDto> response = redis.evalLuaScript(luaScriptSha, luaInputParam);
        if (response.isSuccess()) {
            return response;
        }
        Response<String> redisReponse=redis.loadLuaScript(luaScript);
        if(!redisReponse.isSuccess()){
            return Response.notOk(redisReponse.getErr());
        }
        return redis.evalLuaScript(luaScriptSha, luaInputParam);
    }
    /**
     * redis执行lua脚本
     * @return reidsDto
     */
    private Response<RedisDto> executeScript(Redis redis,String luaScript,final List<String> luaInputParam) {
        return redis.evalLuaScript(luaScript, luaInputParam);
    }

}
