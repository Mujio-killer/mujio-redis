package com.mujio.redisdemo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @Description: RedisConfig
 * @Author: GZY
 * @Date: 2020/4/21 0021
 * @Description: redis配置相关
 * @Param:
 * @return:
 * @Author: GZY
 * @Date: 2020/4/21 0021
 */

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //配置redis连接
        redisTemplate.setConnectionFactory(factory);

        //序列化和反序列化基本设置
        //使用Jackson库将对象序列化为JSON字符串。优点是速度快，序列化后的字符串短小精悍。但缺点也非常致命，那就是此类的构造函数中有一个类型参数，必须提供要序列化对象的类型信息(.class对象)
        Jackson2JsonRedisSerializer jksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper oMapper = new ObjectMapper();

        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有,包括private和public
        oMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类
        // oMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);//此方法被证实存在漏洞
        oMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jksonSeial.setObjectMapper(oMapper);

        //设置redis的序列化模式
        //序列化redis的key,通常都用String的序列化类，因为redis的key为String类型
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //序列化redis的value，按照上文中序列化对象的具体设置来序列化value值，覆盖了redis的除hash外的其他类型
        redisTemplate.setValueSerializer(jksonSeial);

        //设置hash的key和value序列化模式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jksonSeial);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

/**
 * @Description: 对hash类型的数据操作
 * @Param:
 * @return:
 * @Author: GZY
 * @Date: 2020/4/21 0021
 */

    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

/**
 * @Description: 对redis字符串类型操作
 * @Param:
 * @return:
 * @Author: GZY
 * @Date: 2020/4/21 0021
 */

    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

/**
 * @Description: 对redis列表类型操作
 * @Param:
 * @return:
 * @Author: GZY
 * @Date: 2020/4/21 0021
 */

    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String ,Object> redisTemplate){
        return redisTemplate.opsForList();
    }


/**
 * @Description: 对redis的集合类型
 * @Param:
 * @return:
 * @Author: GZY
 * @Date: 2020/4/21 0021
 */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String ,Object> redisTemplate){
        return redisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String ,Object> redisTemplate){
        return redisTemplate.opsForZSet();
    }

}
