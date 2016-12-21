package com.hzq.order.redis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Component
public class RedisHelper {

    private static Logger logger = LoggerFactory.getLogger(RedisHelper.class);
    private static ObjectFactory<Jedis> jedisFactory;

    @Autowired
    private void setJedisFactory(ObjectFactory<Jedis> jedisFactory) {
        this.jedisFactory = jedisFactory;
    }

    public static Jedis getJedis() {
        return jedisFactory.getObject();
    }


    public static String set(String key, String value) {
        return set(0, key, value);
    }

    public static String set(int dataSource, String key, String value) {
        Jedis jedis = null;
        String response = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            response = jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return response;
    }


    public static String set(String key, Object value) {
        return set(0, key, value);
    }

    public static String set(int dateSource, String key, Object value) {
        Jedis jedis = null;
        String response = null;
        try {
            String objectJson = JSON.toJSONString(value);
            jedis = getJedis();
            if (dateSource != 0) {
                jedis.select(dateSource);
            }
            response = jedis.set(key, objectJson);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return response;
    }


    public static String get(String key) {
        return get(0, key);
    }

    public static String get(int dataSource, String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            value = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    public static <T> T get(String key, Class<T> clazz) {
        return get(0, key, clazz);
    }

    public static <T> T get(int dataSource, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            String value = jedis.get(key);
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }


    public static boolean del(String key) {
        return del(0, key);
    }

    public static boolean del(int dataSource, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            jedis.del(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    public static Long hset(String key, String field, String value) {
        return hset(0, key, field, value);
    }

    public static Long hset(int dataSource, String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            Long count = jedis.hset(key, field, value);
            return count;
        } catch (JedisException e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }

    public static boolean hset(String key, String field, Object value) {
        return hset(0, key, field, value);
    }

    public static boolean hset(int dataSource, String key, String field, Object value) {
        Jedis jedis = null;
        try {
            String objectJson = JSON.toJSONString(value);
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            jedis.hset(key, field, objectJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }


    public static String hget(String key, String field) {
        return hget(0, key, field);
    }

    public static String hget(int dataSource, String key, String field) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            value = jedis.hget(key, field);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    public static <T> T hget(String key, String field, Class<T> clazz) {
        return hget(0, key, field, clazz);
    }

    public static <T> T hget(int dataSource, String key, String field, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            String value = jedis.hget(key, field);
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }

    public static boolean hdel(String key, String field) {
        return hdel(0, key, field);
    }

    public static boolean hdel(int dataSource, String key, String field) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            jedis.hdel(key, field);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }


    public static boolean rpush(String key, String value) {
        return rpush(0, key, value);
    }

    public static boolean rpush(int dataSource, String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            jedis.rpush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    public static boolean rpush(String key, Object value) {
        return rpush(0, key, value);
    }

    public static boolean rpush(int dataSource, String key, Object value) {
        Jedis jedis = null;
        try {
            String objectJson = JSON.toJSONString(value);
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            jedis.rpush(key, objectJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    public static List<String> lrange(String key, int start, int end) {
        return lrange(0, key, start, end);
    }

    public static List<String> lrange(int dataSource, String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            List<String> lists = jedis.lrange(key, start, end);
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            jedis.close();
        }
    }


    public static boolean ltrim(String key, int start, int end) {
        return ltrim(0, key, start, end);
    }

    public static boolean ltrim(int dataSource, String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            jedis.ltrim(key, start, end);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    public static Long decr(String key) {
        return decr(0, key);
    }


    public static Long decr(int dataSource, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            return jedis.decr(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            jedis.close();
        }
    }

    public static Long incr(String key) {
        return incr(0, key);
    }

    public static Long incr(int dataSource, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            return jedis.incr(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            jedis.close();
        }
    }

    public static boolean expire(String key, int seconds) {
        return expire(0, key, seconds);
    }

    public static boolean expire(int dataSource, String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            jedis.expire(key, seconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }


    public static boolean hexists(String key, String field) {
        return hexists(0, key, field);
    }

    public static boolean hexists(int dataSource, String key, String field) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            boolean flag = jedis.hexists(key, field);
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }


    public static boolean delKeys(String key) {
        return delKeys(0, key);
    }

    public static boolean delKeys(int dataSource, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (dataSource != 0) {
                jedis.select(dataSource);
            }
            Set<String> keys = jedis.keys(key);
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String Keys = it.next();
                jedis.del(Keys);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            jedis.close();
        }
    }

    public static boolean isKeyExist(String key, int second) {
        return compareAndSetRequest("", "", key, 15);
    }

    public static boolean compareAndSetRequest(String bussiness, String actionName, String message) {
        return compareAndSetRequest(bussiness, actionName, message, 30);
    }

    public static boolean compareAndSetRequest(String bussiness, String actionName, String message, Integer time) {
        String key = bussiness + actionName + message;
        Jedis jedis = null;
        boolean isExist = false;
        try {
            jedis = getJedis();
            jedis.select(0);
            isExist = jedis.setnx(key, "1") == 0;
            if (!isExist)
                jedis.expire(key, time);
        } catch (Exception e) {
            logger.error("compareAndSetRequest with exception:", e);
        } finally {
            jedis.close();
        }
        return isExist;
    }
}


