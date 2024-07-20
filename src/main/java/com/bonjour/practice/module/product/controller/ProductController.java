package com.bonjour.practice.module.product.controller;

import com.alibaba.fastjson.JSON;
import com.bonjour.practice.common.annotations.Statics;
import com.bonjour.practice.common.entity.Product;
import com.bonjour.practice.common.utils.RedisUtil;
import com.bonjour.practice.common.utils.Result;
import com.bonjour.practice.module.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Api(tags = "产品api")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SqlSession session;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/addProduct")
    @ApiOperation("/添加商品")
//    @Statics
    public Result addProduct(@RequestBody Product product) {
//        BigDecimal bigDecimal = new BigDecimal();
//        bigDecimal.add()
        productService.addProduct(product);
        return Result.ok();
    }

    @GetMapping("/getById")
    @ApiOperation("/getById")
    public Result getById(Long id) {
        Product product = productService.getById(id);
        return Result.ok(product);
    }

    @GetMapping("/insert")
    @ApiOperation("/insert")
    public Result insert() {
        String s = (String) redisUtil.getCacheObject("sql");
        Map map = (Map) JSON.parse(s);
        session.insert((String) map.get("mapperId"), map.get("sql"));
        return Result.ok();
    }
}
