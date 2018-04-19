package com.dinfo.sequence.redis;

import com.dinfo.common.model.Response;
import com.dinfo.log.Loggers;
import com.dinfo.sequence.redis.redispro.RedisPro;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 序列号管理类
 * Created by winston on 15/11/3.
 * @author winston
 */
@Service
public class SequenceManager {

    private static final Logger logger = Loggers.get("sequence");

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * 序列号生成信息类Map
     * key为SequenceType.redisProKey
     */
    private  Map<String,Sequence> sequenceMap=Maps.newConcurrentMap();

    /**
     * 获取序列号类型
     * @param sequenceType 序列号类型
     * @return 序列号类型
     */
    public Response<Sequence> getSequence(SequenceType sequenceType){
        Sequence sequence=sequenceMap.get(sequenceType.getRedisProKey());
        if(sequence==null){
            synchronized (SequenceManager.class){
                sequence=sequenceMap.get(sequenceType.getRedisProKey());
                if(sequence==null){
                   Response<Sequence> seResp= buildSequence(sequenceType);
                   if(!seResp.isSuccess()){
                       return Response.notOk(seResp.getErr());
                   }
                   sequence=seResp.getData();
                   sequenceMap.put(sequenceType.getRedisProKey(),sequence);
                }
            }
        }
        return Response.ok(sequence);
    }
    private Response<Sequence> buildSequence(SequenceType sequenceType){
        try {
            //生成redis－lua信息
            InputStream is = this.getClass().getResourceAsStream(sequenceType.getLuaPath());
            String luaScript = IOUtils.toString(is, "UTF-8");
            String luaScriptSha = Hex.encodeHexString(DigestUtils.sha1(luaScript));

            //生成redislient集合
            RedisPro redisPro=applicationContext.getBean(sequenceType.getRedisProKey(), RedisPro.class);
            List<Redis> redisList= Lists.newArrayList();
            String password =redisPro.getPassword();
            String conStr = redisPro.getConStr();
            int type = sequenceType.getType();
            //判断redis是否是集群,类型3和4为集群类型
            if(type == 1 ||  type == 2) {
              if(Strings.isNullOrEmpty(redisPro.getConStr())){
                return Response.notOk("redisConnectStr is null:"+sequenceType.getRedisProKey());
              }
              
              String[] redisConStrArray=conStr.split(",");

              for(String redisConStr:redisConStrArray) {
                  Redis redis;
                  if(StringUtils.isNotBlank(password)){
                      //增加密码
                      redis = new JedisImpl(redisConStr,password);
                  }else {
                      redis = new JedisImpl(redisConStr);
                  }
                  redisList.add(redis);
              }
            } else {
              Redis redis;
              if(StringUtils.isNotBlank(password)){
                //增加密码
                redis = new JedisImpl(conStr,password,true);
              }else {
                  redis = new JedisImpl(conStr,true);
              }
              redisList.add(redis);
            }
            RoundRobinRedis roundRobinRedis=new RoundRobinRedis(redisList);

            //生成Sequence
            Sequence sequence=new Sequence(luaScript,luaScriptSha,conStr,roundRobinRedis,sequenceType);
            return Response.ok(sequence);

        } catch (Exception e) {
            logger.error("failed to buildSequence, cause: {}", Throwables.getStackTraceAsString(e));
            return Response.notOk("failed to buildSequence");
        }
    }

}
