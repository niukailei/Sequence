-- print(KEYS)
--[[
tag是hashtag，也是slot的值，保证key都在一个slot中，否则redis.call报错
logical_shard_id是逻辑分片ID，等于tag
--]]
local tag = KEYS[6]
local logical_shard_id = tonumber(string.gsub((string.gsub(tag, "{", "")), "}", ""), 10)
local max_sequence = tonumber(string.gsub(KEYS[1], tag, ""), 10)
local min_logical_shard_id = tonumber(string.gsub(KEYS[2], tag, ""), 10)
local max_logical_shard_id = tonumber(string.gsub(KEYS[3], tag, ""), 10)
local num_ids = tonumber(string.gsub(KEYS[4], tag, ""), 10)
local sequence_key = KEYS[5]

local lock_key = tag..'longtype-lock'

--[[
验证锁是否存在，如果存在表明在1毫秒已经分配完序列号
--]]
if redis.call('EXISTS', lock_key) == 1 then
  -- redis.log(redis.LOG_INFO, 'sequence: Cannot generate ID, waiting for lock to expire.')
  return redis.error_reply('sequence: Cannot generate ID, waiting for lock to expire.')
end

--[[
按分配的序列号数量incrby
--]]
local end_sequence = redis.call('INCRBY', sequence_key, num_ids)
local start_sequence = end_sequence - num_ids + 1

 --[[
  如果在1毫秒已经分配完序列号,则加锁限制不产生id
 --]]
if end_sequence >= max_sequence then
  -- redis.log(redis.LOG_INFO, 'sequence: Rolling sequence back to the start, locking for 1ms.')
  redis.call('SET', sequence_key, '-1')
  redis.call('PSETEX', lock_key, 1, 'lock')
  end_sequence = max_sequence
end

--[[
  获取时间
--]]
local time = redis.call('TIME')

return {
  start_sequence,
  end_sequence, -- Doesn't need conversion, the result of INCR or the variable set is always a number.
  logical_shard_id,
  tonumber(time[1]),
  tonumber(time[2])
}
