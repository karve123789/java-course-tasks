package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private Long customerId;
    private List<Long> productIds;
    private String shippingAddress;
}