package com.bonjour.practice.common.mabtis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;

import java.util.List;

/**
 * @authur tc
 * @date 2023/12/29 16:49
 */
public class MybatisPlusSqlInjector extends LogicSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> methodList = super.getMethodList();
        methodList.add(new MybatisPlusInsertBatch());   //mysql批量插入
        return methodList;
    }
}
