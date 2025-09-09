package com.example.serviceorders.service;

import com.example.serviceorders.controller.dto.CreateOrderRequest;

public interface OrderService {

    void createOrder(CreateOrderRequest request);
}