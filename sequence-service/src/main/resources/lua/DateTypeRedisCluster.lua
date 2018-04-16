
local tag = KEYS[6]
local logical_shard_id = tonumber(string.gsub((string.gsub(tag, "{", "")), "}", ""), 10)
local max_sequence = tonumber(string.gsub(KEYS[1], tag, ""), 10)
local min_logical_shard_id = tonumber(string.gsub(KEYS[2], tag, ""), 10)
local max_logical_shard_id = tonumber(string.gsub(KEYS[3], tag, ""), 10)
local num_ids = tonumber(string.gsub(KEYS[4], tag, ""), 10)
local sequence_key = KEYS[5]


--[[
验证序列号是否达到最大值,如果已经达到,则为了不生成重复的ID,不在生成
--]]
if redis.call('EXISTS', sequence_key) == 1 then
  if tonumber(redis.call('GET', sequence_key)) >= max_sequence then
    redis.log(redis.LOG_INFO, 'sequence: Cannot generate ID,max_sequence limit')
    return redis.error_reply('sequence: Cannot generate ID,max_sequence limit')
  end
end


--[[
按分配的序列号数量incrby
--]]
local end_sequence = redis.call('INCRBY', sequence_key, num_ids)
local start_sequence = end_sequence - num_ids + 1

if start_sequence==1 then
redis.call('EXPIRE', sequence_key,48*3600)
end

--[[
验证序列号是否达到最大值,如果已经达到,则为了不生成重复的ID,不在生成
--]]
if end_sequence >= max_sequence then
  end_sequence = max_sequence
  redis.log(redis.LOG_INFO, 'sequence: Cannot generate ID,max_sequence limit')
  return redis.error_reply('sequence: Cannot generate ID,max_sequence limit')
end

--[[
  获取时间
--]]
local time = redis.call('TIME')

return {
  start_sequence,
  end_sequence,
  logical_shard_id,
  tonumber(time[1]),
  tonumber(time[2])
}
