package com.bonjour.practice.common.config;

import com.alibaba.fastjson.JSON;
import com.bonjour.practice.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @authur tc
 * @date 2024/6/28 11:33
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MapperIntercepter implements Interceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
//        log.info("type {}", invocation.getArgs()[0]);
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // mappedStatement.getId(),mappedStatement.getSqlCommandType().toString(),invocation.getArgs()[1]
        log.info("{}", mappedStatement.getId());
        log.info("{}", mappedStatement.getSqlCommandType().toString());
        log.info("{}", invocation.getArgs()[1]);
        Map map = new HashMap();
        map.put("mapperId", mappedStatement.getId());
        map.put("type", mappedStatement.getSqlCommandType().toString());
        map.put("sql", invocation.getArgs()[1]);
        redisUtil.setCacheObject("sql", JSON.toJSONString(map));
        Object result = invocation.proceed();
        long end = System.currentTimeMillis();
        long time = (end - start) / 1000;
        System.out.println(time + "s");
        return result;
    }

    @Override
    public Object plugin(Object o) {
        if (o instanceof Executor) {
            return Plugin.wrap(o, this);
        }
        return o;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
