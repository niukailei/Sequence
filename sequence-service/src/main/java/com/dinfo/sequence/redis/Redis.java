package com.dinfo.sequence.redis;


import com.dinfo.common.model.Response;

import java.util.List;

/**
 *reids接口
 */
public interface Redis {
  /**
   * 加载lua脚本
   * @param luaScript lua脚本
   * @return
   */
  Response<String> loadLuaScript(final String luaScript);

  /**
   * 执行luasha脚本
   * @param luaScriptSha lua脚本的sha
   * @param arguments 参数
   * @return 返回值
   */
  Response<RedisDto> evalLuaShaScript(final String luaScriptSha, final List<String> arguments);

  /**
   * 执行lua脚本
   * @param luaScript lua脚本
   * @param arguments 参数
   * @return 返回值
   */
  Response<RedisDto> evalLuaScript(final String luaScript, final List<String> arguments);

  /**
   * 是否支持脚本加载名利
   * @return 结果
   */
  Boolean isSupportLoadLuaScript();

}
