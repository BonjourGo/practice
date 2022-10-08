package com.bonjour.practice.common.mapper;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @authur tc
 * @date 2022/10/8 10:24
 */
public interface BaseMapperPro<T> extends BaseMapper<T> {

    int updateAllById(@Param("et") T entity);

    int updateAll(@Param("et") T entity, @Param("ew") UpdateWrapper<T> updateWrapper);

    int insertBatch(@Param("list") List<T> list);

    default void insertBatchSplit(List<T> list, int batchSize) {
        for(int i = 0; i < list.size(); i += batchSize) {
            this.insertBatch(list.subList(i, Math.min(i + batchSize, list.size())));
        }

    }

    default void insertBatchSplit(List<T> list) {
        this.insertBatchSplit(list, 1000);
    }

    default QueryChainWrapper<T> query() {
        return new QueryChainWrapper(this);
    }

    default UpdateChainWrapper<T> updateWrapper() {
        return new UpdateChainWrapper(this);
    }
}
