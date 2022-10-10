package com.bonjour.practice.module.product.controller;

import com.bonjour.practice.common.entity.Product;
import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "产品api")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    @ApiOperation("/添加商品")
    public Result addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return Result.ok();
    }
}
