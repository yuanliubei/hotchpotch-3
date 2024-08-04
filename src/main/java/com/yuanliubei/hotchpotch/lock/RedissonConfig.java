package com.yuanliubei.hotchpotch.lock;

import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        //此示例是单机的，可以是主从、sentinel、集群等模式
        SingleServerConfig singleServerConfig = config
                .useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
        singleServerConfig.setUsername(redisProperties.getUsername());
        singleServerConfig.setPassword(redisProperties.getPassword());//设置密码
        return Redisson.create(config);
    }
}
