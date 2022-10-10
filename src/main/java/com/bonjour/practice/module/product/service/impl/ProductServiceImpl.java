package com.bonjour.practice.module.product.service.impl;

import com.bonjour.practice.common.entity.Product;
import com.bonjour.practice.common.mapper.ProductMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.module.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CommonService commonService;


    @Override
    public void addProduct(Product product) {
//        product.setId(RedisUtil.getIncrLongId("productId"));
        product.setId(1L);
        product.setProductStatus("1");
        commonService.insert(product, ProductMapper.class);
    }
}
