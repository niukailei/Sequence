-- print(KEYS)
local lock_key = 'longtype-lock'
local logical_shard_id_key = 'logical-shard-id'

local max_sequence = tonumber(KEYS[1])
local min_logical_shard_id = tonumber(KEYS[2])
local max_logical_shard_id = tonumber(KEYS[3])
local num_ids = tonumber(KEYS[4])
local sequence_key = KEYS[5]

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
local logical_shard_id = tonumber(redis.call('GET', logical_shard_id_key)) or -1

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
