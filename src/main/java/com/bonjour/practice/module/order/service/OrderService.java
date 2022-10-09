package com.bonjour.practice.module.order.service;

import com.bonjour.practice.common.entity.Order;

public interface OrderService {

    /**
     * 下单
     * @param order
     */
    void order(Order order);
}
