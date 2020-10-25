package com.mujio.redisdemo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description: RedisUtils封装了redis的常用方法
 * @Author: GZY
 * @Date: 2020/4/21 0021
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @Description: expire缓存失效时间设置
     * @Param: [key, time] 键，时间（秒）
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * @Description: hasKey判断key是否存在
     * @Param: [key]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: del删除指定key的数据
     * @Param: [key]
     * @return: void
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    @SuppressWarnings("unchecked")//告诉编译器忽略 unchecked 警告信息，如使用List，ArrayList等未进行参数化产生的警告信息
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * @Description: get获取指定key的值
     * @Param: [key]
     * @return: java.lang.Object
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * @Description: set 存值
     * @Param: [key, value]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: set 存值并设置过期时间
     * @Param: [key, value, time]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: incr设置键按 step 递增（step 小于0时，则为递减）
     * @Param: [key, delta]
     * @return: long
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public long incr(String key, long step) {
        return redisTemplate.opsForValue().increment(key, step);
    }


    /**
     * @Description: hget
     * @Param: [key, item]
     * @return: java.lang.Object
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * @Description: hget
     * @Param: [key]
     * @return: java.util.Map<java.lang.Object, java.lang.Object>
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public Map<Object, Object> hget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * @Description: hset
     * @Param: [key, map]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean hset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: hset
     * @Param: [key, map, time]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean hset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: hset 向键为 key 的 hash 表中放入 (item,value) 数据,如果不存在将创建
     * @Param: [key, item, value]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: hset 向键为 key 的 hash 表中放入 (item,value) 数据,如果不存在将创建
     * 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @Param: [key, item, value, time]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: hdel 向键为 key 的 hash 表中删除键为 item 的数据,如果不存在将创建
     * @Param: [key, item]
     * @return: void
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * @Description: hHasKey 判断hash表中是否有该项的值
     * @Param: [key, item]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/21 0021
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * @Description: hincr 递增 如果不存在,就会创建一个 并把新增后的值返回(step为负数时，为递减)
     * @Param: [key, item, step]
     * @return: double
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public double hincr(String key, String item, double step) {
        return redisTemplate.opsForHash().increment(key, item, step);
    }

    /**
     * @Description: sGet 根据key获取Set中的所有值
     * @Param: [key]
     * @return: java.util.Set<java.lang.Object>
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: sHasKey 根据键值从一个set中查询,是否存在
     * @Param: [key, value]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: sSet 存值
     * @Param: [key, values]
     * @return: long
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @Description: sSetAndTime 存值并设置过期时间
     * @Param: [key, time, values]
     * @return: long
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @Description: sGetSetSize 获取set大小
     * @Param: [key]
     * @return: long
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @Description: setRemove 移除指定键值
     * @Param: [key, values]
     * @return: long
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @Description: lGet 获取列表
     * @Param: [key, start, end]
     * @return: java.util.List<java.lang.Object>
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: lGetListSize 获取列表长度
     * @Param: [key]
     * @return: long
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @Description: lGetIndex  索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @Param: [key, index]
     * @return: java.lang.Object
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: lSet 向set存值
     * @Param: [key, value]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: lSet 存值并设置过期时间
     * @Param: [key, value, time]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: lSet 向列表存多个值
     * @Param: [key, value]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: lSet 存多个值并设置过期时间
     * @Param: [key, value, time]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: lUpdateIndex 根据索引修改list中的某条数据
     * @Param: [key, index, value]
     * @return: boolean
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: lRemove 移除N个值为value的元素
     * @Param: [key, count, value]
     * @return: long
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @Description: keys 根据正则查找
     * @Param: [pattern]
     * @return: java.util.Set
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * @Description: convertAndSend 向指定频道发布消息
     * @Param: [channel, message]
     * @return: void
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }


    /**
     * @Description: addToListRight 绑定 listKey 对应的列表，进行批操作
     * @Param: [listKey, expireEnum, values]
     * @return: void
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public void addToListRight(String listKey, Status.ExpireEnum expireEnum, Object... values) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //插入数据
        boundValueOperations.rightPushAll(values);
        //设置过期时间
        boundValueOperations.expire(expireEnum.getTime(), expireEnum.getTimeUnit());
    }

    /**
     * @Description: rangeList 根据起始结束序号遍历Redis中的list
     * @Param: [listKey, start, end]
     * @return: java.util.List<java.lang.Object>
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public List<Object> rangeList(String listKey, long start, long end) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //查询数据
        return boundValueOperations.range(start, end);
    }

    /**
     * @Description: rifhtPop 弹出指定key对应列表的最右边值
     * @Param: [listKey]
     * @return: java.lang.Object
     * @Author: GZY
     * @Date: 2020/4/22 0022
     */
    public Object rifhtPop(String listKey) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        return boundValueOperations.rightPop();
    }

    //=========BoundListOperations 用法 End============

    public void watch(String key) {
        redisTemplate.watch(key);
    }

    public void unwatch(String key) {
        redisTemplate.unwatch();
    }
}
