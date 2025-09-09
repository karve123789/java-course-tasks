package com.example.serviceorders.controller.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateOrderRequest {

    private String userId;
    private BigDecimal totalPrice;

}