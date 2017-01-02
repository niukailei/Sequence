package com.dinfo.sequence.redis;

import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.List;

/**
 * 随机redisClient
 */
public class RoundRobinRedis {

  private final List<Redis> redisServers;

  private Iterator<Redis> redisPoolIterator;

  /**
   *
   * @param redisServers A list of redis servers to use.
   */
  public RoundRobinRedis(final List<Redis> redisServers) {
    Preconditions.checkNotNull(redisServers);
    Preconditions.checkArgument(redisServers.size() > 0);

    this.redisServers = redisServers;
    this.redisPoolIterator = redisServers.iterator();
  }

  /**
   *
   * @return The instance of Redis as pulled from the pool.
   */
  public Redis getNextRedis() {
    if (!redisPoolIterator.hasNext()) {
      redisPoolIterator = redisServers.iterator();
    }

    return  redisPoolIterator.next();
  }
}
