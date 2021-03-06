package com.dinfo.sequence.dto;

import java.io.Serializable;

/**
 *  返回结果
 */
public class SequenceId implements Serializable {

  private static final long serialVersionUID = 1573180159942984174L;

  /**
   * id
   */
  private  long id;

  /**
   * id 生成时间
   */
  private  long time;

  public SequenceId() {
  
  }
  
  public SequenceId(final long id, final long time) {
    this.id = id;
    this.time = time;
  }

  public long getId() {
    return id;
  }

  public long getTime() {
    return time;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setTime(long time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return "SequenceId{" +
            "id=" + id +
            ", time=" + time +
            '}';
  }
}
