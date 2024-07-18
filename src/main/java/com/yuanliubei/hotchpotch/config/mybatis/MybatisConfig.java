package com.yuanliubei.hotchpotch.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataChangeRecorderInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuanlb
 * @since 2024/7/17
 */
@Configuration
@MapperScan("com.yuanliubei.hotchpotch.model.dao")
public class MybatisConfig {

    /**
     * 配置插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        /*
         *执行数据库操作时自动记录数据变动，并且可以根据配置的安全阈值来控制操作
         */
        interceptor.addInnerInterceptor(dataChangeRecorderInnerInterceptor());
        /*
         *用于防止恶意的全表更新和删除操作
         */
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        /*
         * 分页插件
         */
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public DataChangeRecorderInnerInterceptor dataChangeRecorderInnerInterceptor() {
        DataChangeRecorderInnerInterceptor innerInterceptor = new DataChangeRecorderInnerInterceptor();
        innerInterceptor.setBatchUpdateLimit(100);
        return innerInterceptor;
    }

    /**
     * 全局配置
     */
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        //自动填充
        globalConfig.setMetaObjectHandler(new AutoFillMetaHandler());
        return globalConfig;
    }
}
