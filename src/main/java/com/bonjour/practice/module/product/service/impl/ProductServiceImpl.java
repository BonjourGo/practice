package com.bonjour.practice.module.product.service.impl;

import com.bonjour.practice.common.entity.Product;
import com.bonjour.practice.common.mapper.ProductMapper;
import com.bonjour.practice.common.service.CommonService;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.module.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CommonService commonService;

    @Autowired
    private ProductMapper productMapper;


    @Override
    public void addProduct(Product product) {
        product.setId(RedisUtil.getIncrLongId("productId"));
//        product.setId(1L);
        product.setProductStatus("1");
        try {
            Thread.sleep(1000);
            int i = productMapper.insert(product);
        } catch (Exception e) {
            log.error(e.toString());
        }
//        System.out.println(2 / 0);
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }
}
