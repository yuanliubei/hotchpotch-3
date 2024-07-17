package com.yuanliubei.hotchpotch.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yuanliubei.hotchpotch.model.domain.BaseEntity;
import com.yuanliubei.hotchpotch.utils.JacksonUtil;
import com.yuanliubei.hotchpotch.utils.LoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author yuanlb
 * @since 2024/7/17
 */
@Component
@Slf4j
public class AutoFillConfig implements MetaObjectHandler {

    private static final String CREATE_BY = "createBy";
    private static final String CREATE_NAME = "createName";
    private static final String CREATE_TIME = "createTime";

    private static final String UPDATE_BY = "updateBy";
    private static final String UPDATE_NAME = "updateName";
    private static final String UPDATE_TIME = "updateTime";


    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.isNull(metaObject)) {
            return;
        }
        boolean flag = metaObject.getOriginalObject() instanceof BaseEntity;
        if (BooleanUtils.isNotTrue(flag)) {
            return;
        }
        BaseEntity baseEntity = (BaseEntity)metaObject.getOriginalObject();

        LocalDateTime now = LocalDateTime.now();
        Long userId = LoginHelper.getUserId();
        String userName = LoginHelper.getUserName();
        if (Objects.isNull(baseEntity.getCreateBy()) && Objects.nonNull(userId)) {
            baseEntity.setCreateBy(userId);
            baseEntity.setCreateName(userName);
        }
        if (Objects.isNull(baseEntity.getCreateTime())) {
            baseEntity.setCreateTime(now);
        }
        if (Objects.isNull(baseEntity.getUpdateBy()) && Objects.nonNull(userId)) {
            baseEntity.setUpdateBy(userId);
            baseEntity.setUpdateName(userName);
        }
        if (Objects.isNull(baseEntity.getUpdateTime())) {
            baseEntity.setUpdateTime(now);
        }
        //TODO 逻辑删除的字段必须通过javabean申明或者设置数据库默认值，有点不理解
        setFieldValByName("deleted", Boolean.FALSE, metaObject);
        log.info("auto fill after , entity = {}", JacksonUtil.toJson(baseEntity));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        Object updateBy = getFieldValByName(UPDATE_BY, metaObject);
        if (Objects.isNull(updateBy)) {
            setFieldValByName(UPDATE_BY, LoginHelper.getUserId(), metaObject);
        }
        Object updateName = getFieldValByName(UPDATE_NAME, metaObject);
        if (Objects.isNull(updateName)) {
            setFieldValByName(UPDATE_NAME, LoginHelper.getUserName(), metaObject);
        }
        Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
        if (Objects.isNull(updateTime)) {
            setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
        }
    }
}
