package com.yuanliubei.hotchpotch.mapstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yuanliubei.hotchpotch.utils.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Component
public class ObjectNodeTypeMapper {
    public ObjectNode map(String str) {
        return (ObjectNode) JacksonUtil.fromJson(str);
    }

    public String map(ObjectNode node) {
        return JacksonUtil.toJson(node);
    }

    public List<ObjectNode> map2List(String str) {
        return StringUtils.isBlank(str) ? Collections.emptyList() :JacksonUtil.fromJson(str, new TypeReference<List<ObjectNode>>() {
        });
    }

    public String map(List<ObjectNode> list) {
        return JacksonUtil.toJson(list);
    }
}
