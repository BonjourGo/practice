package com.bonjour.practice.common.service;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.bonjour.practice.common.mapper.BaseMapperPro;

import java.util.List;

/**
 * @authur tc
 * @date 2022/10/8 10:36
 */
public interface BaseMapperProService {

    <E, M extends BaseMapperPro<E>> void insert(E entity, Class<M> mapperClz);

    <E, M extends BaseMapperPro<E>> void insert(List<E> list, Class<M> mapperClz);

    <E, M extends BaseMapperPro<E>> void updateById(E entity, Class<M> mapperClz);

    <E, M extends BaseMapperPro<E>> void updateById(List<E> list, Class<M> mapperClz);

    <E, M extends BaseMapperPro<E>> void updateAllById(E entity, Class<M> mapperClz);

    <E, M extends BaseMapperPro<E>> void updateAllById(List<E> list, Class<M> mapperClz);

    <E, M extends BaseMapperPro<E>> QueryChainWrapper<E> query(Class<M> mapperClz);

    <E, M extends BaseMapperPro<E>> UpdateChainWrapper<E> update(Class<M> mapperClz);

    <E, M extends BaseMapperPro<E>> M getMapper(Class<M> mapperClz);
}
