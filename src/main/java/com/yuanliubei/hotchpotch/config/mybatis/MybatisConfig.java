package com.yuanliubei.hotchpotch.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
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

}
