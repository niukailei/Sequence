package com.dinfo.sequence.redis;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * The response from the sequence ID generation script.
 *
 * It has four fields, all equally important to generate an ID:
 *   * Where sequence generation starts from
 *   * Where sequence generation ends
 *   * The logical shard ID
 *   * The current time in seconds
 *   * The current time in microseconds
 *
 */
public class RedisDto {

  private static final int START_SEQUENCE_INDEX = 0;

  private static final int END_SEQUENCE_INDEX = 1;

  private static final int LOGICAL_SHARD_ID_INDEX = 2;

  private static final int TIME_SECONDS_INDEX = 3;

  private static final int TIME_MICROSECONDS_INDEX = 4;

  private final long startSequence;

  private final long endSequence;

  private final long logicalShardId;

  private final long timeSeconds;

  private final long timeMicroseconds;

  /**
   * @param results
   * 构造函数
   */
  public RedisDto(final List<Long> results) {
    Preconditions.checkNotNull(results);
    this.startSequence = results.get(START_SEQUENCE_INDEX);
    this.endSequence = results.get(END_SEQUENCE_INDEX);
    this.logicalShardId = results.get(LOGICAL_SHARD_ID_INDEX);
    this.timeSeconds = results.get(TIME_SECONDS_INDEX);
    this.timeMicroseconds = results.get(TIME_MICROSECONDS_INDEX);
  }

  public long getStartSequence() {
    return startSequence;
  }

  public long getEndSequence() {
    return endSequence;
  }

  public long getLogicalShardId() {
    return logicalShardId;
  }

  public long getTimeSeconds() {
    return timeSeconds;
  }

  public long getTimeMicroseconds() {
    return timeMicroseconds;
  }

}
