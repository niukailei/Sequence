package com.dinfo.sequence.redis;

import com.dinfo.common.model.Response;
import com.dinfo.core.RedisClient;
import com.google.common.base.Throwables;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * redis接口实现类
 */
public class JedisImpl implements Redis {
  /**
   * reids地址匹配模式
   */
  private static final Pattern SERVER_FORMAT = Pattern.compile("^([^:]+):([0-9]+)$");

  /**
   * redis实例
   */
  private final RedisClient redisClient;

  /**
   * 构造函数
   */
  public JedisImpl(final String hostAndPort) throws Exception{
    this.redisClient = jedisPoolFromServerAndPort(hostAndPort);
  }

  /**
   * 增加密码
   * @param hostAndPort
   * @param password 密码
   * @throws Exception
   */
  public JedisImpl(final String hostAndPort,final String password) throws Exception{
    this.redisClient = jedisPoolFromServerAndPort(hostAndPort,password);
  }

  /**
   * redis加载lua脚本
   * @param luaScript lua脚本.
   * @return lua脚本的SHA
   */
  @Override
  public Response<String> loadLuaScript(final String luaScript) {

    return Response.notOk("unsupport redis command:loadLuaScript");
  }
  /**
   * 执行lua脚本
   * @param luaScriptSha lua脚本的SHA.
   * @param arguments 执行参数.
   * @return 执行结果
   */
  @Override
  public Response<RedisDto> evalLuaShaScript(final String luaScriptSha, final List<String> arguments) {

    return Response.notOk("unsupport redis command:evalLuaScript");
  }

  @Override
  public Response<RedisDto> evalLuaScript(String luaScript, List<String> arguments) {
    String[] args = arguments.toArray(new String[arguments.size()]);
    try {
      @SuppressWarnings("unchecked")
      List<Long> results = (List<Long>) redisClient.eval(luaScript, arguments.size(), args);
      return Response.ok(new RedisDto(results));
    } catch (Throwable e) {
      return Response.notOk(Throwables.getStackTraceAsString(e));
    }
  }

  @Override
  public Boolean isSupportLoadLuaScript() {
    return  Boolean.FALSE;
  }


  /**
   *
   * @param hostAndPortAndPwd redis地址和端口，格式为"host:port".
   * @return  RedisClient
   */
  private RedisClient jedisPoolFromServerAndPort(final String hostAndPortAndPwd) throws Exception{
    Matcher matcher = SERVER_FORMAT.matcher(hostAndPortAndPwd);

    if (!matcher.matches()) {
      throw new Exception("HostAndPort is error:"+hostAndPortAndPwd);
    }
    return new RedisClient(matcher.group(1), Integer.valueOf(matcher.group(2)));
  }

  private RedisClient jedisPoolFromServerAndPort(final String hostAndPortAndPwd,final String password) throws Exception{
    Matcher matcher = SERVER_FORMAT.matcher(hostAndPortAndPwd);

    if (!matcher.matches()) {
      throw new Exception("HostAndPort is error:"+hostAndPortAndPwd);
    }
    return new RedisClient(matcher.group(1), Integer.valueOf(matcher.group(2)),password);
  }
}
