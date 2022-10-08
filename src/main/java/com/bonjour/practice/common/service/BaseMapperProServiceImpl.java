package com.bonjour.practice.common.service;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.bonjour.practice.common.mapper.BaseMapperPro;
import com.bonjour.practice.common.utils.SpringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @authur tc
 * @date 2022/10/8 10:38
 */
public class BaseMapperProServiceImpl implements BaseMapperProService {

    public BaseMapperProServiceImpl() {
    }

    // 插入
    @Override
    public <E, M extends BaseMapperPro<E>> void insert(E entity, Class<M> mapperClz) {
        ((BaseMapperPro) SpringUtil.getBean(mapperClz)).insert(entity);
    }

    // 批量插入
    @Override
    public <E, M extends BaseMapperPro<E>> void insert(List<E> list, Class<M> mapperClz) {
        ((BaseMapperPro) SpringUtil.getBean(mapperClz)).insertBatchSplit(list);
    }

    // 更新
    @Override
    public <E, M extends BaseMapperPro<E>> void updateById(E entity, Class<M> mapperClz) {
        ((BaseMapperPro) SpringUtil.getBean(mapperClz)).updateById(entity);
    }

    // 批量更新
    @Override
    public <E, M extends BaseMapperPro<E>> void updateById(List<E> list, Class<M> mapperClz) {
        M mapper = (M) SpringUtil.getBean(mapperClz);
        list.forEach(mapper::updateById);
    }

    // 更新
    @Override
    public <E, M extends BaseMapperPro<E>> void updateAllById(E entity, Class<M> mapperClz) {
        ((BaseMapperPro) SpringUtil.getBean(mapperClz)).updateAllById(entity);
    }

    @Override
    public <E, M extends BaseMapperPro<E>> void updateAllById(List<E> list, Class<M> mapperClz) {
        M mapper = (M) SpringUtil.getBean(mapperClz);
        list.forEach(mapper::updateAllById);
    }

    @Override
    public <E, M extends BaseMapperPro<E>> UpdateChainWrapper<E> update(Class<M> mapperClz) {
        M mapper = (M) SpringUtil.getBean(mapperClz);
        return new UpdateChainWrapper(mapper);
    }

    @Override
    public <E, M extends BaseMapperPro<E>> QueryChainWrapper<E> query(Class<M> mapperClz) {
        return ((BaseMapperPro) SpringUtil.getBean(mapperClz)).query();
    }

    @Override
    public <E, M extends BaseMapperPro<E>> M getMapper(Class<M> mapperClz) {
        return (M) SpringUtil.getBean(mapperClz);
    }
}
