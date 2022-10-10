package com.bonjour.practice.common.mapper;

import com.bonjour.practice.common.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @authur tc
 * @date 2022/10/10 14:59
 */
@Mapper
public interface ProductMapper extends BaseMapperPro<Product> {

    /**
     * 更新库存
     * @param number
     * @param productId
     */
    void updateStock(@Param("number") Integer number, @Param("productId") Long productId);
}
