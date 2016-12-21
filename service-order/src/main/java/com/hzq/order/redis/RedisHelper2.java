package com.hzq.order.redis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


@Component
public class RedisHelper2 {
    private static final Logger logger = LoggerFactory.getLogger(RedisHelper2.class);

    private static ObjectFactory<Jedis> jedisFactory;

    @Autowired
    private void setJedisFactory(ObjectFactory<Jedis> jedisFactory) {
        RedisHelper2.jedisFactory = jedisFactory;
    }


    /**
     * 装饰器封装获取连接池对象，释放连接
     *
     * @param database 数据库id
     * @param fun      在数据库上执行的操作
     * @param <T>      操作的返回类型
     * @return 操作的结果
     */
    public static <T> T execute(int database, Function<Jedis, T> fun) {
        try (Jedis jedis = jedisFactory.getObject()) {
            jedis.select(database);
            return fun.apply(jedis);
        }
    }

    public static void execute(int database, Consumer<Jedis> fun) {
        try (Jedis jedis = jedisFactory.getObject()) {
            jedis.select(database);
            fun.accept(jedis);
        }
    }

    @Deprecated
    private static <T> T execute(Function<Jedis, T> fun) {
        return execute(0, fun);
    }

    /**
     * 其实我的内心深处是挣扎的，要不要写这个方法
     * 如果已有的封装不适用，建议进来util加个方法
     *
     * @return 未封装的Jedis对象
     */
    @Deprecated
    public static Jedis getJedis() {
        return jedisFactory.getObject();
    }

    /**
     * 默认获取管道对象静态工厂
     *
     * @return
     */
    @Deprecated
    public static PipelineWrapper getPipelineWrapper() {
        return new PipelineWrapper();
    }

    /**
     * 选择redis库，获取管道对象静态工厂
     *
     * @param dataSource 选择redis库，获取管道对象静态工厂
     * @return
     */
    @Deprecated
    public static PipelineWrapper getPipelineWrapper(int dataSource) {
        return new PipelineWrapper(dataSource);
    }

    /**
     * 默认获取事务对象静态工厂
     *
     * @return
     */
    @Deprecated
    public static MultiWrapper getMultiWrapper() {
        return new MultiWrapper();
    }

    /**
     * 选择redis库，获取事务对象静态工厂
     *
     * @param dataSource
     * @return
     */
    @Deprecated
    public static MultiWrapper getMultiWrapper(int dataSource) {
        return new MultiWrapper(dataSource);
    }

    public static <T> T pipelined(int database, Function<Pipeline, T> fun) {
        return execute(database, jedis -> (fun.apply(jedis.pipelined())));
    }

    public static <T> T multi(int database, Function<Transaction, T> fun) {
        return execute(database, jedis -> (fun.apply(jedis.multi())));
    }

    @Deprecated
    public static String set(final String key, final String value) {
        return set(0, key, value);
    }

    /**
     * @param key   键
     * @param value 传入对象，转化为json
     * @return
     */
    @Deprecated
    public static String set(final String key, final Object value) {
        return set(0, key, value);
    }

    public static String set(final int dataSource, final String key, final String value) {
        return execute(dataSource, jedis -> (jedis.set(key, value)));
    }

    public static String set(final int dataSource, final String key, final Object value) {
        return set(dataSource, key, JSON.toJSONString(value));
    }

    /**
     * 设置键值时同时设置它的生存时间。
     *
     * @param dataSource 数据库。
     * @param key        键。
     * @param value      指。
     * @param seconds    生存时间。
     * @return 是否成功。
     */
    public static String setEx(final int dataSource, final String key, final String value, final int seconds) {
        return execute(dataSource, jedis -> (jedis.setex(key, seconds, value)));
    }

    /**
     * 设置键值时同时设置它的生存时间。
     *
     * @param dataSource 数据库。
     * @param key        键。
     * @param value      OBJECT类型值
     * @param seconds    生存时间。
     * @return 是否成功。
     */
    public static String setEx(final int dataSource, final String key, final Object value, final int seconds) {
        return setEx(dataSource, key, JSON.toJSONString(value), seconds);
    }


    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
     *
     * @param dataSource 数据库。
     * @param key        键。
     * @param value      String类型值
     * @return 0 不存在，1 设置成功且存在
     */
    public static Long setNx(final int dataSource, final String key, final String value) {
        return execute(dataSource, jedis -> (jedis.setnx(key, value)));
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
     *
     * @param dataSource 数据库。
     * @param key        键。
     * @param value      Object类型值,然后做fastjson转换为string
     * @return 0 不存在，1 设置成功且存在
     */
    public static Long setNx(final int dataSource, final String key, final Object value) {
        return execute(dataSource, jedis -> (jedis.setnx(key, JSON.toJSONString(value))));
    }

    @Deprecated
    public static String get(final String key) {
        return get(0, key);
    }

    public static String get(final int dataSource, final String key) {
        return execute(dataSource, jedis -> (jedis.get(key)));
    }

    @Deprecated
    public static <T> T get(final String key, final Class<T> clazz) {
        return get(0, key, clazz);
    }

    public static <T> T get(final int dataSource, final String key, final Class<T> clazz) {
        return JSON.parseObject(get(dataSource, key), clazz);
    }

    public static Long del(final int dataSource, final String key) {
        return execute(dataSource, jedis -> (jedis.del(key)));
    }

    @Deprecated
    public static Long del(final String key) {
        return del(0, key);
    }

    @Deprecated
    public static Long hset(final String key, final String field, final String value) {
        return hset(0, key, field, value);
    }

    public static Long hset(final int dataSource, final String key, final String field, final String value) {
        return execute(dataSource, jedis -> (jedis.hset(key, field, value)));
    }

    @Deprecated
    public static Long hset(final String key, final String field, final Object value) {
        return hset(0, key, field, value);
    }

    public static Long hset(final int dataSource, final String key, final String field, final Object value) {
        return hset(dataSource, key, field, JSON.toJSONString(value));
    }

    @Deprecated
    public static String hget(final String key, final String field) {
        return hget(0, key, field);
    }

    public static String hget(final int dataSource, final String key, final String field) {
        return execute(dataSource, jedis -> (jedis.hget(key, field)));
    }

    @Deprecated
    public static <T> T hget(final String key, final String field, final Class<T> clazz) {
        return hget(0, key, field, clazz);
    }

    public static <T> T hget(final int dataSource, final String key, final String field, final Class<T> clazz) {
        return JSON.parseObject(hget(dataSource, key, field), clazz);
    }

    public static Long hdel(final int dataSource, final String key, final String field) {
        return execute(dataSource, jedis -> (jedis.hdel(key, field)));
    }

    @Deprecated
    public static Long hdel(final String key, final String field) {
        return hdel(0, key, field);
    }

    @Deprecated
    public static Long rpush(final String key, final String value) {
        return rpush(0, key, value);
    }

    public static Long rpush(final int dataSource, final String key, final String value) {
        return execute(dataSource, jedis -> (jedis.rpush(key, value)));
    }

    @Deprecated
    public static Long rpush(final String key, final Object value) {
        return rpush(0, key, value);
    }

    public static Long rpush(final int dataSource, final String key, final Object value) {
        return rpush(dataSource, key, JSON.toJSONString(value));
    }

    public static Long lpush(final String key, final Object value) {
        return lpush(key, JSON.toJSONString(value));
    }


    public static Long lpush(final int dataSource, final String key, final Object value) {
        return lpush(dataSource, key, JSON.toJSONString(value));
    }

    @Deprecated
    public static Long lpush(final String key, final String value) {
        return lpush(0, key, value);
    }

    public static Long lpush(final int dataSource, final String key, final String value) {
        return execute(dataSource, jedis -> (jedis.lpush(key, value)));
    }


    @Deprecated
    public static Long lpushEx(final String key, final Object value, final int seconds) {
        return lpushEx(0, key, value, seconds);
    }


    public static Long lpushEx(final int dataSource, final String key, final Object value, final int seconds) {
        return lpushEx(0, key, JSON.toJSONString(value), seconds);
    }

    @Deprecated
    public static Long lpushEx(final String key, final String value, final int seconds) {
        return lpushEx(0, key, value, seconds);
    }


    public static Long lpushEx(final int dataSource, final String key, final String value, final int seconds) {
        return multi(dataSource, tx -> {
            tx.expire(key, seconds);  // prevent immediate expire
            Response<Long> response = tx.lpush(key, value);
            tx.expire(key, seconds);
            tx.exec();
            return response.get();
        });
    }


    public static String lpop(final int dataSource, final String key) {
        return execute(dataSource, jedis -> (jedis.lpop(key)));
    }

    @Deprecated
    public static List<String> lrange(final String key, final int start, final int end) {
        return lrange(0, key, start, end);
    }

    public static List<String> lrange(final int dataSource, final String key, final int start, final int end) {
        return execute(dataSource, jedis -> (jedis.lrange(key, start, end)));
    }

    @Deprecated
    public static String ltrim(final String key, final int start, final int end) {
        return ltrim(0, key, start, end);
    }

    public static String ltrim(final int dataSource, final String key, final int start, final int end) {
        return execute(dataSource, jedis -> (jedis.ltrim(key, start, end)));
    }

    @Deprecated
    public static Long decr(final String key) {
        return decr(0, key);
    }

    public static Long decr(final int dataSource, final String key) {
        return execute(dataSource, jedis -> (jedis.decr(key)));
    }

    @Deprecated
    public static Long incr(final String key) {
        return incr(0, key);
    }

    public static Long incr(final int dataSource, final String key) {
        return execute(dataSource, jedis -> (jedis.incr(key)));
    }

    @Deprecated
    public static Long expire(final String key, final int seconds) {
        return expire(0, key, seconds);
    }

    public static Long expire(final int dataSource, final String key, final int seconds) {
        return execute(dataSource, jedis -> (jedis.expire(key, seconds)));
    }

    @Deprecated
    public static Boolean hexists(final String key, final String field) {
        return hexists(0, key, field);
    }

    public static Boolean hexists(final int dataSource, final String key, final String field) {
        return execute(dataSource, jedis -> (jedis.hexists(key, field)));
    }

    private static void scanBatch(final int dataSource, final ScanParams scanParams, BiConsumer<Jedis, List<String>> fun) {
        execute(dataSource, jedis -> {
            String cursor = "0";
            do {
                ScanResult<String> result = jedis.scan(cursor, scanParams);
                fun.accept(jedis, result.getResult());
                cursor = result.getStringCursor();
            } while (!cursor.equals("0"));
        });
    }

    private static void scan(final int dataSource, final ScanParams scanParams, BiConsumer<Jedis, String> fun) {
        scanBatch(dataSource, scanParams, (jedis, list) -> list.forEach(e -> fun.accept(jedis, e)));
    }

    private static List<String> scan(final int dataSource, final ScanParams scanParams) {
        List<String> result = new ArrayList<>();
        scanBatch(dataSource, scanParams, (jedis, list) -> result.addAll(list));
        return result;
    }

    public static void delKeys(final int dataSource, final String pattern) {
        scanBatch(dataSource, new ScanParams().match(pattern), (jedis, list) -> jedis.del(list.toArray(new String[list.size()])));
    }


    /**
     * 通道调用示例
     */
    private void pipeDemo() {
        long begin = System.currentTimeMillis();
        pipelined(0, pipeline -> {
            for (int i = 0; i < 5000; i++) {
                pipeline.set("a1", String.valueOf(i));
                pipeline.set("a2", String.valueOf(i));
                pipeline.set("a3", String.valueOf(i));
            }
            return pipeline.syncAndReturnAll();
        });
        System.out.println(System.currentTimeMillis() - begin);
    }

    /**
     * 事务调用示例
     *
     * @return
     */
    private List<Object> mulitDemo() {
        return multi(0, tx -> {
            for (int i = 0; i < 5; i++) {
                tx.set("a1", String.valueOf(i));
                tx.set("a2", String.valueOf(i));
                tx.set("a3", String.valueOf(i));
            }
            return tx.exec();
        });
    }

    private void noPipeDemo() {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            set(0, "a1", String.valueOf(i));
            set(0, "a2", String.valueOf(i));
            set(0, "a3", String.valueOf(i));
        }
        System.out.println(System.currentTimeMillis() - begin);
    }

    /**
     * 封装管道对象，隐藏开启关闭管道方法
     */
    @Deprecated
    public static class PipelineWrapper {
        private Jedis jedis;
        private Pipeline pipeline;

        public PipelineWrapper() {
            jedis = jedisFactory.getObject();
            pipeline = jedis.pipelined();
        }

        public PipelineWrapper(int dataSource) {
            jedis = jedisFactory.getObject();
            jedis.select(dataSource);
            pipeline = jedis.pipelined();
        }

        public Pipeline getPipeline() {
            return pipeline;
        }

        public void close() {
            pipeline.sync();
            jedis.close();
        }

        public List<Object> closeAndReturnAll() {
            try {
                return pipeline.syncAndReturnAll();
            } finally {
                jedis.close();
            }
        }
    }

    /**
     * 封装事务对象，隐藏开启关闭管道方法
     */
    @Deprecated
    public static class MultiWrapper {
        private Jedis jedis;
        private Transaction multi;

        public MultiWrapper() {
            jedis = jedisFactory.getObject();
            multi = jedis.multi();
        }

        public MultiWrapper(int dataSource) {
            jedis = jedisFactory.getObject();
            jedis.select(dataSource);
            multi = jedis.multi();
        }

        public Jedis getJedis() {
            return jedis;
        }

        public Transaction getMulti() {
            return multi;
        }

        public List<Object> exec() {
            try {
                return multi.exec();
            } finally {
                jedis.close();
            }
        }

        public List<Response<?>> execGetResponse() {
            try {
                return multi.execGetResponse();
            } finally {
                jedis.close();
            }
        }
    }


}
