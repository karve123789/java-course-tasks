package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Название продукта не может быть пустым")
    private String name;

    private String description;

    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше нуля")
    private BigDecimal price;

    @NotNull(message = "Количество на складе не может быть пустым")
    @Min(value = 0, message = "Количество на складе не может быть отрицательным")
    private Integer quantityInStock;

}